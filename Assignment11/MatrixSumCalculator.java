public class MatrixSumCalculator implements Runnable {
  private int[][] matrix;
  private int[][] unitMatrix;
  private int startIndex;
  private int endIndex;
  private SenseBarrier barrier;
  private volatile int[][] partialSumMatrix;

  public MatrixSumCalculator(int[][] matrix, int[][] unitMatrix, int startIndex, int endIndex, SenseBarrier barrier) {
    this.matrix = matrix;
    this.unitMatrix = unitMatrix;
    this.startIndex = startIndex;
    this.endIndex = endIndex;
    this.barrier = barrier;
    this.partialSumMatrix = new int[matrix.length][matrix[0].length];
  }

  public int[][] getPartialSumMatrix() {
    return partialSumMatrix;
  }

  @Override
  public void run() {
    for (int i = startIndex; i <= endIndex; i++) {
      int[][] powerMatrix = power(matrix, i);
      partialSumMatrix = addMatrices(partialSumMatrix, powerMatrix);
    }

    barrier.await();
  }

  private int[][] power(int[][] matrix, int exponent) {
    if (exponent == 0) {
      return unitMatrix;
    } else if (exponent == 1) {
      return matrix;
    }

    int[][] result = matrix;
    for (int i = 2; i <= exponent; i++) {
      result = multiply(result, matrix);
    }
    return result;
  }

  private int[][] multiply(int[][] matrix1, int[][] matrix2) {
    int m = matrix1.length;
    int n = matrix1[0].length;
    int[][] result = new int[m][n];

    for (int i = 0; i < m; i++) {
      for (int j = 0; j < n; j++) {
        for (int k = 0; k < n; k++) {
          result[i][j] += matrix1[i][k] * matrix2[k][j];
        }
      }
    }

    return result;
  }

  private int[][] addMatrices(int[][] matrix1, int[][] matrix2) {
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
}
