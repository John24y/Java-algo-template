package main.nk2;

import java.io.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Main {
    private static final int MAXN = 3000000 + 10, P = 998244353, G = 3, Gi = 332748118;
    private static int N, M, limit = 1, L;
    private static int[] r = new int[MAXN];
    private static long[] a = new long[MAXN], b = new long[MAXN];

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

    private static void NTT(long[] A, int type) {
        for(int i = 0; i < limit; i++)
            if(i < r[i]) {
                long temp = A[i];
                A[i] = A[r[i]];
                A[r[i]] = temp;
            }
        for(int mid = 1; mid < limit; mid <<= 1) {
            long Wn = fastpow( type == 1 ? G : Gi , (P - 1) / (mid << 1));
            for(int j = 0; j < limit; j += (mid << 1)) {
                long w = 1;
                for(int k = 0; k < mid; k++, w = (w * Wn) % P) {
                    long x = A[j + k], y = w * A[j + k + mid] % P;
                    A[j + k] = (x + y) % P;
                    A[j + k + mid] = (x - y + P) % P;
                }
            }
        }
    }

    public static void main(String[] args) {
        N = nextInt();
        M = nextInt();
        for(int i = 0; i <= N; i++) a[i] = (nextInt() + P) % P;
        for(int i = 0; i <= M; i++) b[i] = (nextInt() + P) % P;
        while(limit <= N + M) {
            limit <<= 1;
            L++;
        }
        for(int i = 0; i < limit; i++) r[i] = (r[i >> 1] >> 1) | ((i & 1) << (L - 1));
        NTT(a, 1);
        NTT(b, 1);
        for(int i = 0; i < limit; i++) a[i] = (a[i] * b[i]) % P;
        NTT(a, -1);
        long inv = fastpow(limit, P - 2);
        for(int i = 0; i <= N + M; i++)
            out.print(((a[i] * inv) % P) + " ");
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

