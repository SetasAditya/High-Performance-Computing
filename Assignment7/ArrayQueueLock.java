import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class ArrayQueueLock {
    private AtomicBoolean[] flag;
    private AtomicInteger tail;
    private AtomicInteger head;

    public ArrayQueueLock(int capacity) {
        flag = new AtomicBoolean[capacity];
        for (int i = 0; i < capacity; i++) {
            flag[i] = new AtomicBoolean(false);
        }
        tail = new AtomicInteger(0);
        head = new AtomicInteger(0);
        flag[0].set(true);
    }

    public void lock() {
        int myIndex = tail.getAndIncrement() % flag.length;
        while (!flag[myIndex].get()) {
            // spin
        }
    }

    public void unlock() {
        int myIndex = head.getAndIncrement() % flag.length;
        flag[myIndex].set(false);
        flag[(myIndex + 1) % flag.length].set(true);
    }

    public void incrementCounter(AtomicInteger counter) {
        counter.getAndIncrement();
    }

    public int getCounterValue(AtomicInteger counter) {
        return counter.get();
    }

    public static void main(String[] args) {
        final int numThreads = Integer.parseInt(args[0]);
        final int numIncrements = Integer.parseInt(args[1]);

        AtomicInteger counter = new AtomicInteger(0);
        ArrayQueueLock lock = new ArrayQueueLock(numThreads);

        Thread[] threads = new Thread[numThreads];

        long start = System.nanoTime();

        for (int i = 0; i < numThreads; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < numIncrements; j++) {
                    lock.lock();
                    try {
                        lock.incrementCounter(counter);
                    } finally {
                        lock.unlock();
                    }
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
