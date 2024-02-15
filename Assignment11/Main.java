import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);

    System.out.print("Enter the order of the matrix (m x m): ");
    int m = scanner.nextInt();

    System.out.print("Enter the value of n: ");
    int n = scanner.nextInt();

    System.out.print("Enter the number of threads (1, 2, 4, 6, 8, 10, 12, 14, 16, 18, or 20): ");
    int numThreads = scanner.nextInt();

    int[][] matrix = new int[m][m];
    int[][] unitMatrix = createUnitMatrix(m);

    System.out.println("Enter the matrix elements:");

    for (int i = 0; i < m; i++) {
      for (int j = 0; j < m; j++) {
        System.out.print("Enter element at position (" + (i+1) + ", " + (j+1) + "): ");
        matrix[i][j] = scanner.nextInt();
      }
    }

    SenseBarrier barrier = new SenseBarrier(numThreads);
    MatrixSumCalculator[] calculators = new MatrixSumCalculator[numThreads];
    Thread[] threads = new Thread[numThreads];

    int startIndex = 0;
    int endIndex = n / numThreads - 1;

    for (int i = 0; i < numThreads; i++) {
      calculators[i] = new MatrixSumCalculator(matrix, unitMatrix, startIndex, endIndex, barrier);
      threads[i] = new Thread(calculators[i]);
      threads[i].start();

      startIndex = endIndex + 1;
      endIndex = (i == numThreads - 2) ? n - 1 : endIndex + n / numThreads;
    }

    int[][] totalSumMatrix = new int[m][m];

    for (int i = 0; i < numThreads; i++) {
      try {
        threads[i].join();
        totalSumMatrix = addMatrices(totalSumMatrix, calculators[i].getPartialSumMatrix());
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    System.out.println("Total sum matrix:");
    printMatrix(totalSumMatrix);

    scanner.close();
  }

  private static int[][] createUnitMatrix(int size) {
    int[][] unitMatrix = new int[size][size];
    for (int i = 0; i < size; i++) {
      unitMatrix[i][i] = 1;
    }
    return unitMatrix;
  }

  private static int[][] addMatrices(int[][] matrix1, int[][] matrix2) {
    int m = matrix1.length;
    int n = matrix1[0].length;
    int[][] result = new int[m][n];

    for (int i = 0; i < m; i++) {
      for (int j = 0; j < n; j++) {
        result[i][j] = matrix1[i][j] + matrix2[i][j];
      }
    }

    return result;
  }

  private static void printMatrix(int[][] matrix) {
    int m = matrix.length;
    int n = matrix[0].length;

    for (int i = 0; i < m; i++) {
      for (int j = 0; j < n; j++) {
        System.out.print(matrix[i][j] + " ");
      }
      System.out.println();
    }
  }
}
