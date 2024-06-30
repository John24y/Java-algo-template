package main.nk2;


import java.io.*;
import java.util.*;

public class Main {

    void solve() {
        int n=nextInt(),p=nextInt();

        int[] pow2 = new int[n + 1];
        pow2[0] = 1;
        for (int i = 0; i < n; i++) {
            pow2[i + 1] = (int) ((long) pow2[i] * 2 % p);
        }

        int[][] dp = new int[n + 1][n + 1];
        dp[0][0] = 1;
        for (int i = 1; i <= n; i++) {
            for (int j = 0; j <= i; j++) {
                dp[i][j] = (int) ((long) dp[i - 1][j] * (pow2[j] - 1) % p);
                if (j > 0) {
                    dp[i][j] += dp[i - 1][j - 1];
                    dp[i][j] %= p;
                }
            }
        }

        int[] res = new int[n + 1];
        res[0] = 1;
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= i; j++) {
                res[i] = (int) ((res[i] + (long) res[i - j] * dp[i][j] % p) % p);
            }
        }

        System.out.println(Arrays.toString(res));
    }

    public static void main(String[] args) throws Exception {
        int t=1;
        for (int i = 0; i < t; i++) {
            new Main().solve();
        }
        out.flush();
    }

    static PrintWriter out = new PrintWriter(System.out, false);
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

