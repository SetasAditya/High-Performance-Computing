import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicInteger;

public class QueueLockWithTimeout {
    private AtomicReference<QNode> tail;
    private ThreadLocal<QNode> myNode;
    private ThreadLocal<QNode> myParent;
    private ThreadLocal<Long> startTime;

    private static final long TIMEOUT = 1000; // 1 second timeout

    public QueueLockWithTimeout() {
        this.tail = new AtomicReference<>(null);
        this.myNode = ThreadLocal.withInitial(QNode::new);
        this.myParent = ThreadLocal.withInitial(() -> null);
        this.startTime = ThreadLocal.withInitial(System::currentTimeMillis);
    }

    public boolean tryLock() {
        QNode qnode = myNode.get();
        qnode.locked = true;

        QNode pred = tail.getAndSet(qnode);
        myParent.set(pred);

        if (pred != null) {
            pred.next = qnode;
            while (qnode.locked && (System.currentTimeMillis() - startTime.get() < TIMEOUT)) {
            }
            if (qnode.locked) {
                // Timeout occurred, need to remove self from the queue and return false
                QNode parent = myParent.get();
                if (parent != null) {
                    parent.next = null;
                } else {
                    tail.compareAndSet(qnode, null);
                }
                return false;
            }
        }
        return true;
    }

    public void lock() {
        while (!tryLock()) {
        }
    }

    public void unlock() {
        QNode qnode = myNode.get();
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

    private static class QNode {
        private volatile boolean locked = false;
        private volatile QNode next = null;
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

    public static void main(String[] args) {
        final int numThreads = Integer.parseInt(args[0]);
        final int numIncrements = Integer.parseInt(args[1]);
        AtomicInteger counter = new AtomicInteger(0);

        QueueLockWithTimeout lock = new QueueLockWithTimeout();

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
        double timeTaken=(end-start)/1000000000.0;
        double throughput=(lock.getCounterValue(counter)/(1000000.0*timeTaken))*1000;// Millions of Operations per second
        int totalOps = numThreads * numIncrements;

        System.out.print(":num_threads:" + numThreads + " :totalOps:" + totalOps + " :throughput:" + throughput + " :Final_counter:" + lock.getCounterValue(counter) + "\n");
    }
}