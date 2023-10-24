import java.util.Arrays;

public class ParallelSummation {
    private static final int ARRAY_SIZE = 100000000; // Rozmiar tablicy
    private static final int NUM_THREADS = Runtime.getRuntime().availableProcessors(); // Liczba dostępnych rdzeni procesora

    public static void main(String[] args) throws InterruptedException {
        // Inicjalizacja tablic
        int[] array1 = new int[ARRAY_SIZE];
        int[] array2 = new int[ARRAY_SIZE];

        Arrays.fill(array1, 1); // Wypełnienie tablicy wartościami 1
        Arrays.fill(array2, 2); // Wypełnienie tablicy wartościami 2

        // Sumowanie z użyciem jednego wątku
        long startTime = System.currentTimeMillis();
        long sumSingleThread = sumArray(array1);
        long endTime = System.currentTimeMillis();
        long singleThreadTime = endTime - startTime;

        System.out.println("Suma z użyciem jednego wątku: " + sumSingleThread);
        System.out.println("Czas wykonania (jeden wątek): " + singleThreadTime + " ms");

        // Sumowanie z użyciem dwóch wątków
        startTime = System.currentTimeMillis();
        long sumTwoThreads = parallelSumArray(array2, 2);
        endTime = System.currentTimeMillis();
        long twoThreadTime = endTime - startTime;

        System.out.println("Suma z użyciem dwóch wątków: " + sumTwoThreads);
        System.out.println("Czas wykonania (dwa wątki): " + twoThreadTime + " ms");

        // Sumowanie z użyciem kilku wątków
        startTime = System.currentTimeMillis();
        long sumMultipleThreads = parallelSumArray(array2, NUM_THREADS);
        endTime = System.currentTimeMillis();
        long multipleThreadTime = endTime - startTime;

        System.out.println("Suma z użyciem " + NUM_THREADS + " wątków: " + sumMultipleThreads);
        System.out.println("Czas wykonania (" + NUM_THREADS + " wątki): " + multipleThreadTime + " ms");
    }

    // Sumowanie tablicy za pomocą jednego wątku
    private static long sumArray(int[] array) {
        long sum = 0;
        for (int num : array) {
            sum += num;
        }
        return sum;
    }

    // Sumowanie tablicy za pomocą podanej liczby wątków
    private static long parallelSumArray(int[] array, int numThreads) throws InterruptedException {
        int segmentSize = array.length / numThreads;
        SumThread[] threads = new SumThread[numThreads];

        for (int i = 0; i < numThreads; i++) {
            int start = i * segmentSize;
            int end = (i == numThreads - 1) ? array.length : start + segmentSize;
            threads[i] = new SumThread(array, start, end);
            threads[i].start();
        }

        long sum = 0;

        for (SumThread thread : threads) {
            thread.join();
            sum += thread.getPartialSum();
        }

        return sum;
    }

    // Wątek sumujący fragment tablicy
    private static class SumThread extends Thread {
        private final int[] array;
        private final int start;
        private final int end;
        private long partialSum;

        public SumThread(int[] array, int start, int end) {
            this.array = array;
            this.start = start;
            this.end = end;
        }

        public long getPartialSum() {
            return partialSum;
        }

        @Override
        public void run() {
            for (int i = start; i < end; i++) {
                partialSum += array[i];
            }
        }
    }
}