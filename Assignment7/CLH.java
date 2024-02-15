import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class CLH {
    private final AtomicReference<QNode> tail;
    private final ThreadLocal<QNode> myNode;
    private final ThreadLocal<QNode> myPred;

    public CLH() {
        tail = new AtomicReference<>(new QNode());
        myNode = ThreadLocal.withInitial(QNode::new);
        myPred = ThreadLocal.withInitial(() -> null);
    }

    public void lock() {
        QNode qnode = myNode.get();
        qnode.locked = true;
        QNode pred = tail.getAndSet(qnode);
        myPred.set(pred);
        while (pred.locked) {
            // spin
        }
    }

    public void unlock() {
        QNode qnode = myNode.get();
        qnode.locked = false;
        myNode.set(myPred.get());
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

    static class QNode {
        boolean locked = false;
    }

    public static void main(String[] args) {
        final int numThreads = Integer.parseInt(args[0]);
        final int numIncrements = Integer.parseInt(args[1]);

        AtomicInteger counter = new AtomicInteger(0);
        CLH lock = new CLH();

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
