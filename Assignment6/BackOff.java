import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicBoolean;

public class BackOff {
    private final AtomicInteger counter = new AtomicInteger(0);
    private final AtomicBoolean state = new AtomicBoolean(false);

    public void lock() {
        int delay = 1;
        while (true) {
            while (state.get()) {
            }
            if (!state.getAndSet(true)) {
                return;
            }
            sleep(random() % delay);
            if (delay < 100) {
                delay = 2 * delay;
            }
        }
    }

    public void unlock() {
        state.set(false);
    }

    public void incrementCounter() {
        while (true) {
            int delay = 1;
            while (state.get()) {
                sleep(random() % delay);
                if (delay < 100) {
                    delay = 2 * delay;
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

    private int random() {
        return (int) (Math.random() * 1000);
    }

    public static void main(String[] args) {
        final int numThreads = Integer.parseInt(args[0]);
        final int numIncrements = Integer.parseInt(args[1]);

        BackOff lock = new BackOff();

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
