package template;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Author Create by CROW
 * @Date 2023/4/15
 */
public class Matrix {

    /**
     * 按对角线遍历矩阵，返回按顺序遍历每条线时的下标
     */
    public static List<List<int[]>> diagTraverse(int rowCount, int colCount) {
        int m=rowCount, n=colCount;
        List<List<int[]>> res = new ArrayList<>();
        // 按左边缘遍历
        for (int i = 0; i < m; i++) {
            List<int[]> list = new ArrayList<>();
            for (int ii = i, j = 0; ii >= 0 && j < n; ii--, j++) {
                list.add(new int[] {ii, j});
            }
            res.add(list);
        }

        // 按下边缘遍历
        for (int j = 1; j < n; j++) {
            List<int[]> list = new ArrayList<>();
            for (int i = n - 1, jj = j; i>=0 && jj<n; jj++, i--) {
                list.add(new int[] {i, jj});
            }
            res.add(list);
        }
        return res;
    }

    public static int[][] rotateRight(int[][] matrix) {
        int m = matrix.length, n = matrix[0].length;
        int[][] res = new int[n][m];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                res[j][m-i-1]=matrix[i][j];
            }
        }
        return res;
    }

    public static int[][] rotateLeft(int[][] matrix) {
        int m = matrix.length, n = matrix[0].length;
        int[][] res = new int[n][m];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                res[n-1-j][i]=matrix[i][j];
            }
        }
        return res;
    }

    /**
     * 顺时针旋转矩阵90度，矩阵长宽需要相等
     */
    public static void rotateRightInplace(int[][] matrix) {
        int n = matrix.length;
        // 先转置矩阵
        for (int i = 0; i < n; i++) {
            for (int j = i; j < n; j++) {
                int temp = matrix[i][j];
                matrix[i][j] = matrix[j][i];
                matrix[j][i] = temp;
            }
        }
        // 再将每行翻转
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n / 2; j++) {
                int temp = matrix[i][j];
                matrix[i][j] = matrix[i][n - 1 - j];
                matrix[i][n - 1 - j] = temp;
            }
        }
    }

    /**
     * 逆时针旋转矩阵90度，矩阵长宽需要相等
     */
    public static void rotateLeftInplace(int[][] matrix) {
        int n = matrix.length;
        // 先转置矩阵
        for (int i = 0; i < n; i++) {
            for (int j = i; j < n; j++) {
                int temp = matrix[i][j];
                matrix[i][j] = matrix[j][i];
                matrix[j][i] = temp;
            }
        }
        // 再将每列翻转
        for (int j = 0; j < n; j++) {
            for (int i = 0; i < n / 2; i++) {
                int temp = matrix[i][j];
                matrix[i][j] = matrix[n - 1 - i][j];
                matrix[n - 1 - i][j] = temp;
            }
        }
    }

}
