import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicInteger;

public class CohortLock {
    private final AtomicReference<Node> tail;
    private final ThreadLocal<Node> myNode;
    private final ThreadLocal<Node> myParent;
    private final AtomicInteger level;
    private final ThreadLocal<Integer> myLevel;

    public CohortLock() {
        this.tail = new AtomicReference<>(null);
        this.myNode = ThreadLocal.withInitial(() -> new Node(null));
        this.myParent = ThreadLocal.withInitial(() -> null);
        this.level = new AtomicInteger(0);
        this.myLevel = ThreadLocal.withInitial(() -> 0);
    }

    public void lock() {
        Node qnode = myNode.get();
        qnode.locked = true;
        qnode.level = level.getAndIncrement();
        myLevel.set(qnode.level);

        Node pred = tail.getAndSet(qnode);
        myParent.set(pred);

        if (pred != null) {
            pred.next = qnode;

            while (qnode.locked) {
                Node parent = myParent.get();
                if (parent != null && parent.level == qnode.level - 1) {
                    parent.child = qnode;
                    while (qnode.locked) {}
                    parent.child = null;
                }
            }
        }
    }

    public void unlock() {
        Node qnode = myNode.get();
        qnode.locked = false;

        if (qnode.next == null) {
            if (tail.compareAndSet(qnode, null)) {
                level.set(qnode.level);
                return;
            }
            while (qnode.next == null) {}
        }

        qnode.next.locked = false;
        qnode.next = null;
        level.set(qnode.level);
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
        private int level;

        private Node(Node parent) {
            if (parent != null) {
                parent.child = this;
            }
        }
    }

    public static void main(String[] args) {
        final int numThreads = Integer.parseInt(args[0]);
        final int numIncrements = Integer.parseInt(args[1]);
        AtomicInteger counter = new AtomicInteger(0);

        CohortLock lock = new CohortLock();

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
