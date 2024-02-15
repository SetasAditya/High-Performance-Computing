import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicInteger;

public class MCS {
    private final AtomicReference<QNode> tail;
    private final ThreadLocal<QNode> myNode;
    private final ThreadLocal<Boolean> myWait;

    public MCS() {
        tail = new AtomicReference<>(null);
        myNode = ThreadLocal.withInitial(QNode::new);
        myWait = ThreadLocal.withInitial(() -> false);
    }
    
    public void lock() {
        QNode qnode = myNode.get();
        QNode pred = tail.getAndSet(qnode);
        if (pred != null) {
            qnode.locked = true;
            pred.next = qnode;
            while (qnode.locked || myWait.get()) {
                // spin
            }
        }
    }
    
    public void unlock() {
        QNode qnode = myNode.get();
        if (qnode.next == null) {
            if (tail.compareAndSet(qnode, null)) {
                return;
            }
            while (qnode.next == null) {
                // spin
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
    
    static class QNode {
        boolean locked = false;
        QNode next = null;
    }
    
    public static void main(String[] args) {
        final int numThreads = Integer.parseInt(args[0]);
        final int numIncrements = Integer.parseInt(args[1]);
    
        AtomicInteger counter = new AtomicInteger(0);
        MCS lock = new MCS();
    
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