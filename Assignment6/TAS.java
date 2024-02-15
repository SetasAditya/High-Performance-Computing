import java.util.concurrent.atomic.AtomicBoolean;

public class TAS {
    private int value = 0;
    private AtomicBoolean state = new AtomicBoolean(false);

    public void increment() {
        while (true) {
            while (state.get()) {

            }
            if (state.compareAndSet(false, true)) {
                try {
                    value++;
                    return;
                } finally {
                    state.set(false);
                }
            }
        }
    }

    public int getValue() {
        return value;
    }

    public static void main(String[] args) throws InterruptedException {
        final int numThreads = Integer.parseInt(args[0]);
        final int numIncrements = Integer.parseInt(args[1]);
        TAS counter = new TAS();

        Thread[] threads = new Thread[numThreads];

        long start = System.nanoTime();

        for (int i = 0; i < numThreads; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < numIncrements; j++) {
                    counter.increment();
                }
            });
            threads[i].start();
        }

        for (int i = 0; i < numThreads; i++) {
            threads[i].join();
        }

        long end = System.nanoTime();
        double elapsedSeconds = (end - start) / 1000000000.0;
        int totalOps = numThreads * numIncrements;
        double throughput = (totalOps / (1000000.0 * elapsedSeconds)) * 1000; // Millions of Operations per second

        System.out.println(":num_threads:" + numThreads + " :totalOps:" + totalOps + " :throughput:" + throughput
                + " :Final_counter:" + counter.getValue() + "\n");
    }
}
