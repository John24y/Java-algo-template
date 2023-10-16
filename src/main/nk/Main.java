package main.nk;

import java.io.*;
import java.util.*;

import java.io.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

class NTT {
    private static final long P = 998244353, G = 3, Gi = fastpow(G, P-2);

    /**
     * 多项式A*B, a[i]表示多项式A的i次项系数
     * @return C=A*B,c[i]表示i次项的系数
     */
    static int[] polyMul(int[] a, int[] b, int[] nullableRes) {
        int rA=rankOf(a);
        int rB=rankOf(b);
        int N = a.length - 1, M = b.length - 1;
        int t = N + M;
        N = Math.max(N, M);
        int limit = 1, L = 0;
        // 为什么lim要大于 n << 1，因为答案是n+m次项的，虽然a最高是n次项，但对a FFT 之后，要代入至少n+m个点
        while (limit <= (N << 1)) {
            limit <<= 1;
            ++L;
        }

        int[][] A = new int[2][limit + 1];
        int[] rev = new int[limit + 1];
        System.arraycopy(a, 0, A[0], 0, a.length);
        System.arraycopy(b, 0, A[1], 0, b.length);

        for(int i = 0; i < limit; i++) {
            rev[i] = (rev[i >> 1] >> 1) | ((i & 1) << (L - 1));
        }

        NTT(A[0], limit, 1, rev);
        NTT(A[1], limit, 1, rev);
        for(int i = 0; i < limit; i++) A[0][i] = (int) (((long)A[0][i] * (long)A[1][i]) % P);
        NTT(A[0], limit, -1, rev);

        int[] ans = nullableRes == null ? new int[t + 1] : nullableRes;
        //assert ans.length > nullableRes
        long inv = fastpow(limit, P - 2);
        for(int i = 0; i <= t; i++) ans[i] = (int) (((long) A[0][i] * inv) % P);
        return ans;
    }

    /**
     * 多项式A*A, a[i]表示多项式A的i次项系数
     * @return C=A*A,c[i]表示i次项的系数
     */
    static int[] polySquare(int[] a) {
        int N = a.length - 1;
        int t = N + N;
        int limit = 1, L = 0;
        while (limit <= (N << 1)) {
            limit <<= 1;
            ++L;
        }

        int[] A = new int[limit + 1];
        int[] rev = new int[limit + 1];
        System.arraycopy(a, 0, A, 0, a.length);

        for(int i = 0; i < limit; i++) {
            rev[i] = (rev[i >> 1] >> 1) | ((i & 1) << (L - 1));
        }

        NTT(A, limit, 1, rev);
        for(int i = 0; i < limit; i++) A[i] = (int) (((long)A[i] * (long)A[i]) % P);
        NTT(A, limit, -1, rev);

        int[] ans = new int[t + 1];
        long inv = fastpow(limit, P - 2);
        for(int i = 0; i <= t; i++) ans[i] = (int) (((long) A[i] * inv) % P);
        return ans;
    }

    private static void NTT(int[] A, int limit, int type, int[] rev) {
        for(int i = 0; i < limit; i++){
            if(i < rev[i]) {
                int temp = A[i];
                A[i] = A[rev[i]];
                A[rev[i]] = temp;
            }
        }
        for(int mid = 1; mid < limit; mid <<= 1) {
            long Wn = fastpow( type == 1 ? G : Gi , (P - 1) / (mid << 1));
            for(int j = 0; j < limit; j += (mid << 1)) {
                long w = 1;
                for(int k = 0; k < mid; k++, w = (w * Wn) % P) {
                    long x = A[j + k], y = w * (long) A[j + k + mid] % P;
                    A[j + k] = (int) ((x + y) % P);
                    A[j + k + mid] = (int) ((x - y + P) % P);
                }
            }
        }
    }

    private static long fastpow(long a, long k) {
        long base = 1;
        while(k != 0) {
            if((k & 1) != 0)
                base = (base * a) % P;
            a = (a * a) % P;
            k >>= 1;
        }
        return base % P;
    }

    private static int rankOf(int[] p) {
        int r = p.length - 1;
        while (r >= 0 && p[r] == 0) {
            r--;
        }
        return Math.max(0, r);
    }

    private static int[] mulBF(int[] a, int[] b) {
        int rA = rankOf(a);
        int rB = rankOf(b);
        if (rA > rB) {
            {
                int tmp = rA;
                rA = rB;
                rB = tmp;
            }
            {
                int[] tmp = a;
                a = b;
                b = tmp;
            }
        }
        int[] c = new int[rA + rB + 1];
        for (int i = 0; i <= rA; i++) {
            for (int j = 0; j <= rB; j++) {
                c[i + j] = (int) ((c[i + j] + (long) a[i] * b[j]) % P);
            }
        }
        return c;
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
        int[] c=NTT.polyMul(a,b, null);
        for (int i = 0; i < n+m+1; i++) {
            out.print((i>=c.length?0:c[i])+" ");
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

