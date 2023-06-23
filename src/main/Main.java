package main;
import java.io.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;
import java.util.stream.Collectors;


class FFT {
    static final double PI = Math.PI;

    static int[] ployMul(int[] a, int[] b) {
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
        int[] ans = new int[t+1];
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
public class Main {

    public static void main(String[] args) throws IOException {
        int n=nextInt(),m=nextInt();
        int[] a=new int[n+1];
        int[] b=new int[m+1];
        for (int i = 0; i <= n; ++i) {
            a[i]=nextInt();
        }
        for (int i = 0; i <= m; ++i) {
            b[i]=nextInt();
        }
        int[] c=FFT.ployMul(a,b);
        for (int i = 0; i < c.length; i++) {
            out.print(c[i]+" ");
        }
        out.flush();
    }

    static PrintWriter out = new PrintWriter(System.out, true);
    static InputReader in = new InputReader(System.in);
    static String next() { return in.next(); }
    static int nextInt() { return Integer.parseInt(in.next()); }
    static long nextLong() { return Long.parseLong(in.next()); }
    static class InputReader {
        public BufferedReader reader;
        public StringTokenizer tokenizer;

        public InputReader(InputStream stream) {
            reader = new BufferedReader(new InputStreamReader(stream), 32768);
            tokenizer = null;
        }

        public String next() {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {
                    tokenizer = new StringTokenizer(reader.readLine());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return tokenizer.nextToken();
        }
    }
}

