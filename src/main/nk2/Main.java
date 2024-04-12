package main.nk2;
import java.io.*;
import java.util.*;


import java.util.*;

class M2 {
    static final long INF = 1L << 61;
    static final int N = 105;
    static int n, q;
    static int[] a = new int[N];
    static ArrayList<Integer>[] b = new ArrayList[N];
    static ArrayList<Long>[] dp = new ArrayList[N];
    static long[] t = new long[N];
    static int h;
    static long l, r;
    static long ans;

    public static void dfs(int x, long cur, long l, long r, int h) {
        if (x == -1) {
            ans = cur;
            return;
        }
        int y = 0;
        while (cur + t[x] <= l) {
            cur += t[x];
            y++;
        }
        while (y < a[x] && cur <= r) {
            if (dp[x].get(y) >= h) {
                dfs(x - 1, cur, l, r, h - b[x].get(y));
                if (ans != -1) return;
            }
            cur += t[x];
            y++;
        }
    }

    public static void solve() {
        Scanner scanner = new Scanner(System.in);
        n = scanner.nextInt();
        q = scanner.nextInt();
        long cur = 1;
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
            t[i] = cur;
            if (cur != INF) {
                if (cur > INF / a[i]) cur = INF;
                else cur *= a[i];
            }
            b[i] = new ArrayList<>();
            dp[i] = new ArrayList<>();
            for (int j = 0; j < a[i]; j++) {
                b[i].add(scanner.nextInt());
            }
            if (i == 0) {
                for (int j = 0; j < a[i]; j++) {
                    dp[i].add((long) b[i].get(j));
                }
            } else {
                for (int j = 0; j < a[i]; j++) {
                    dp[i].add(0L);  // Initialize with zeros
                    for (int k = 0; k < a[i - 1]; k++) {
                        dp[i].set(j, Math.max(dp[i].get(j), dp[i - 1].get(k) + b[i].get(j)));
                    }
                }
            }
        }
        while (q-- > 0) {
            h = scanner.nextInt();
            l = scanner.nextLong();
            r = scanner.nextLong();
            ans = -1;
            dfs(n - 1, 0, l, r, h);
            System.out.println(ans);
        }
        scanner.close();
    }

    public static void main(String[] args) {
        solve();
    }
}

public class Main {

    void solve() {
         M2.solve();
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

