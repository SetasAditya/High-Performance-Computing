import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicBoolean;

public class TTAS {
    private final AtomicInteger counter = new AtomicInteger(0);
    private final AtomicBoolean state = new AtomicBoolean(false);

    public void lock() {
        while (true) {
            while (state.get()) {
                // spin
            }
            if (!state.getAndSet(true)) {
                return;
            }
        }
    }

    public void unlock() {
        state.set(false);
    }

    public void incrementCounter() {
        lock();
        try {
            counter.incrementAndGet();
        } finally {
            unlock();
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

    public static void main(String[] args) {
        final int numThreads = Integer.parseInt(args[0]);;
        final int numIncrements = Integer.parseInt(args[1]);;

        TTAS lock = new TTAS();

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
        double throughput = totalOps / elapsedSeconds;


        System.out.print(":num_threads:"+numThreads+" :totalOps:"+totalOps+" :throughput:"+throughput+" :Final_counter:"+lock.getCounterValue() +"\n" );
    }
}
