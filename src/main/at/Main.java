package main.at;

import java.io.*;
import java.util.*;

public class Main implements Runnable {
    private static final int MIN = Integer.MIN_VALUE;

    void solve() {
        int n = nextInt();
        int k = nextInt();
        k = n - k;
        int[][] a = new int[n][2];
        for (int i = 0; i < n; i++) {
            a[i][0] = nextInt();
            a[i][1] = nextInt();
        }

        int[][] dp=new int[4][k+1];
        for (int i = 0; i <= k; i++) {
            dp[0][i]=MIN;
            dp[1][i]=-1;
            dp[2][i]=MIN;
            dp[3][i]=-1;
        }
        dp[0][0] = 0;

        for (int i = 0; i < n; i++) {
            for (int j = k; j > 0; j--) {
                int c = a[i][0];
                int v = MIN;
                if (dp[1][j - 1] == a[i][0]) {
                    v = dp[2][j - 1] + a[i][1];
                } else {
                    v = dp[0][j - 1] + a[i][1];
                }

                if (dp[1][j] == c) {
                    dp[0][j] = Math.max(dp[0][j], v);
                } else if (dp[3][j] == c) {
                    dp[2][j] = Math.max(dp[2][j], v);
                    if (dp[2][j]>dp[0][j]) {
                        int t0=dp[0][j];
                        int t1=dp[1][j];
                        dp[0][j]=dp[2][j];
                        dp[1][j]=dp[3][j];
                        dp[2][j]=t0;
                        dp[3][j]=t1;
                    }
                } else if (v >= 0) {
                    if (v > dp[0][j]) {
                        dp[2][j]=dp[0][j];
                        dp[3][j]=dp[1][j];
                        dp[0][j]=v;
                        dp[1][j]=c;
                    } else if (v > dp[2][j]) {
                        dp[2][j]=v;
                        dp[3][j]=c;
                    }
                }
            }
        }

        int r = Math.max(dp[0][k], dp[2][k]);
        System.out.println(r < 0 ? -1 : r);
    }

    public static void main(String[] args) throws Exception {
        new Thread(null, new Main(), "CustomThread", 1024 * 1024 * 100).start();
    }

    @Override
    public void run() {
        new Main().solve();
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

