import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicBoolean;

public class HierarchicalBackoffLock {
    private final AtomicInteger counter = new AtomicInteger(0);
    private final AtomicBoolean state = new AtomicBoolean(false);
    private final int[] delays;

    public HierarchicalBackoffLock(int numLevels) {
        this.delays = new int[numLevels];
        for (int i = 0; i < numLevels; i++) {
            delays[i] = 1 << i;
        }
    }

    public void lock() {
        int level = 0;
        while (true) {
            while (state.get()) {
            }
            if (!state.getAndSet(true)) {
                return;
            }
            if (level < delays.length) {
                sleep(delays[level++]);
            } else {
                sleep(delays[delays.length - 1]);
            }
        }
    }

    public void unlock() {
        state.set(false);
    }

    public void incrementCounter() {
        int level = 0;
        while (true) {
            while (state.get()) {
                if (level < delays.length) {
                    sleep(delays[level++]);
                } else {
                    sleep(delays[delays.length - 1]);
                }
            }
            if (state.compareAndSet(false, true)) {
                try {
                    counter.incrementAndGet();
                    return;
                } finally {
                    unlock();
                }
            }
        }
    }

    public int getCounterValue() {
        lock();
        try {
            return counter.get();
        } finally {
            unlock();
        }
    }

    private void sleep(long duration) {
        try {
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) {
        final int numThreads = Integer.parseInt(args[0]);
        final int numIncrements = Integer.parseInt(args[1]);

        HierarchicalBackoffLock lock = new HierarchicalBackoffLock(5);

        Thread[] threads = new Thread[numThreads];

        long start = System.nanoTime();

        for (int i = 0; i < numThreads; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < numIncrements; j++) {
                    lock.incrementCounter();
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
        double elapsedSeconds = (end - start) / 1000000000.0;
        int totalOps = numThreads * numIncrements;
        double throughput=(totalOps/(1000000.0*elapsedSeconds))*1000;// Millions of Operations per second

        System.out.print(":num_threads:" + numThreads + " :totalOps:" + totalOps + " :throughput:" + throughput
                + " :Final_counter:" + lock.getCounterValue() + "\n");
    }
}
