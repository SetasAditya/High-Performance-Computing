import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicInteger;

public class HierarchicalMCSLock {
    private AtomicReference<Node> tail;
    private ThreadLocal<Node> myNode;
    private ThreadLocal<Node> myParent;

    public HierarchicalMCSLock() {
        this.tail = new AtomicReference<>(null);
        this.myNode = ThreadLocal.withInitial(() -> new Node(0, null));
        this.myParent = ThreadLocal.withInitial(() -> null);
    }

    public void lock() {
        Node qnode = myNode.get();
        qnode.locked = true;

        Node pred = tail.getAndSet(qnode);
        myParent.set(pred);

        if (pred != null) {
            pred.next = qnode;
            while (qnode.locked) {
                Node parent = myParent.get();
                if (parent != null) {
                    parent.child = qnode;
                    while (qnode.locked) {
                    }
                    parent.child = null;
                }
            }
        }
    }

    public void unlock() {
        Node qnode = myNode.get();
        if (qnode.next == null) {
            if (tail.compareAndSet(qnode, null)) {
                return;
            }
            while (qnode.next == null) {
            }
        }
        qnode.next.locked = false;
        qnode.next = null;
    }

    public void incrementCounter(AtomicInteger counter) {
        lock();
        try {
            counter.incrementAndGet();
        } finally {
            unlock();
        }
    }

    public int getCounterValue(AtomicInteger counter) {
        lock();
        try {
            return counter.get();
        } finally {
            unlock();
        }
    }

    private static class Node {
        private volatile boolean locked = false;
        private volatile Node next = null;
        private volatile Node child = null;
        private final int level;

        private Node(int level, Node parent) {
            this.level = level;
            if (parent != null) {
                parent.child = this;
            }
        }
    }

    public static void main(String[] args) {
        final int numThreads = Integer.parseInt(args[0]);
        final int numIncrements = Integer.parseInt(args[1]);
        AtomicInteger counter = new AtomicInteger(0);

        HierarchicalMCSLock lock = new HierarchicalMCSLock();

        Thread[] threads = new Thread[numThreads];

        long start = System.nanoTime();

        for (int i = 0; i < numThreads; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < numIncrements; j++) {
                    lock.incrementCounter(counter);
                }
            });
            threads[i].start();
        }

        for (int i = 0; i < numThreads; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        long end = System.nanoTime();
        double timeTaken = (end - start) / 1000000000.0;
        double throughput = (lock.getCounterValue(counter) / (1000000.0 * timeTaken)) * 1000; // Millions of Operations per second
        int totalOps = numThreads * numIncrements;

        System.out.print(":num_threads:" + numThreads + " :totalOps:" + totalOps + " :throughput:" + throughput + " :Final_counter:" + lock.getCounterValue(counter) + "\n");
    }
}
