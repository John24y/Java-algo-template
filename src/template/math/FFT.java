package template.math;

import java.io.IOException;

/**
 * 快速傅里叶变换 O(nlogn)时间的多项式乘法。
 * ● 避免创建Complex对象，用数组存储所有复数。且使用double[2][N]而不是double[N][2]，后者和创建Complex对象的性能是一样的，
 * 要改成double[2][N]才能减少创建对象，分配连续内存，提高CPU缓存行命中率。实测效果从11s提升到了3.6s。
 * ● 用快读快写。不要用Stream拼接输出结果，可以快约0.5s
 *
 * @Author Create by jiaxiaozheng
 * @Date 2023/6/23
 */
class FFT {
    static final double PI = Math.PI;

    /**
     * 多项式A*B, a[i]表示多项式A的i次项系数
     * @return C=A*B,c[i]表示i次项的系数
     */
    static int[] polyMul(int[] a, int[] b) {
        int n = a.length - 1, m = b.length - 1;
        int t = n + m;
        n = Math.max(n, m);
        int lim = 1, l = -1;
        while (lim <= (n << 1)) {
            lim <<= 1;
            ++l;
        }

        double[][] F = new double[2][lim + 1];
        for (int i = 0; i < a.length; ++i) {
            F[0][i] = a[i];
        }
        for (int i = 0; i < b.length; ++i) {
            F[1][i] = b[i];
        }

        int[] rev = new int[lim + 1];
        for (int i = 1; i <= lim; ++i) {
            rev[i] = (rev[i >> 1] >> 1) | ((i & 1) << l);
        }

        FFT(F, lim, 1, rev);
        for (int i = 0; i <= lim; ++i) {
            mul(F, i, F, i, F, i);
        }

        FFT(F, lim, -1, rev);
        int[] ans = new int[t + 1];
        for (int i = 0; i <= t; ++i) {
            ans[i] = (int) (F[1][i] / 2 + 0.5);
        }
        return ans;
    }

    private static void FFT(double[][] a, int lim, int op, int[] rev) {
        for (int i = 1; i <= lim; ++i) {
            if (i < rev[i]) {
                swap(a, i, a, rev[i]);
            }
        }
        double[][] tmp = new double[2][4];
        for (int n = 1; n < lim; n <<= 1) {
            tmp[0][0] = Math.cos(PI / n); //tmp[0]：旋转乘数 rt
            tmp[1][0] = op * Math.sin(PI / n);
            for (int j = 0; j < lim; j += (n << 1)) {
                tmp[0][1] = 1;  //tmp[1]：当前单位根 w
                tmp[1][1] = 0;
                for (int k = 0; k < n; ++k) {
                    copy(a, j | k, tmp, 2); //tmp[2]：x=a[j | k]
                    mul(tmp, 1, a, j | k | n, tmp, 3); //tmp[3]：y=w*[j | k | n]
                    add(tmp, 2, tmp, 3, a, j | k); // x+y
                    sub(tmp, 2, tmp, 3, a, j | k | n); // x-y
                    mul(tmp, 1, tmp, 0, tmp, 1); // w*=rt
                }
            }
        }

        if (op == 1) return;
        for (int i = 0; i <= lim; ++i) {
            a[0][i] /= lim;
            a[1][i] /= lim;
        }
    }

    private static void mul(double[][] s1, int s1_, double[][] s2, int s2_, double[][] res, int res_) {
        double a = s1[0][s1_] * s2[0][s2_] - s1[1][s1_] * s2[1][s2_];
        double b = s1[0][s1_] * s2[1][s2_] + s1[1][s1_] * s2[0][s2_];
        res[0][res_] = a;
        res[1][res_] = b;
    }

    private static void add(double[][] s1, int s1_, double[][] s2, int s2_, double[][] res, int res_) {
        double a = s1[0][s1_] + s2[0][s2_];
        double b = s1[1][s1_] + s2[1][s2_];
        res[0][res_] = a;
        res[1][res_] = b;
    }

    private static void sub(double[][] s1, int s1_, double[][] s2, int s2_, double[][] res, int res_) {
        double a = s1[0][s1_] - s2[0][s2_];
        double b = s1[1][s1_] - s2[1][s2_];
        res[0][res_] = a;
        res[1][res_] = b;
    }

    private static void swap(double[][] s1, int s1_, double[][] s2, int s2_) {
        double a = s1[0][s1_];
        double b = s1[1][s1_];
        s1[0][s1_] = s2[0][s2_];
        s1[1][s1_] = s2[1][s2_];
        s2[0][s2_] = a;
        s2[1][s2_] = b;
    }

    private static void copy(double[][] s1, int s1_, double[][] s2, int s2_) {
        s2[0][s2_] = s1[0][s1_];
        s2[1][s2_] = s1[1][s1_];
    }

}
