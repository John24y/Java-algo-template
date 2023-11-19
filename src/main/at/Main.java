package main.at;

import java.io.*;
import java.nio.channels.IllegalSelectorException;
import java.util.*;
import java.util.stream.Collectors;


public class Main {

    public void solve() throws Exception {
        int n=nextInt();
        int m=nextInt();
        String s=next();
        String t=next();
        dp=new Boolean[n][m+1];
        if (dfs(s,t,n,m,0,0)){
            out.println("Yes");
        } else {
            out.println("No");
        }
    }

    Boolean[][] dp;

    private boolean dfs(String s, String t, int n, int m, int i, int off) {
        if (i>=n)return true;
        if (i+m-off>n) return false;
        if (dp[i][off]!=null) {
            return dp[i][off];
        }

        for (int j = off; j < m; j++) {
            if (i + j - off >= n || s.charAt(i + j - off) != t.charAt(j)) {
                break;
            }
            if (dfs(s, t, n, m, i + j - off + 1, 0)) {
                dp[i][off] = true;
                return true;
            }
            if (j == m - 1) {
                for (int k = 0; k < m; k++) {
                    if (dfs(s, t, n, m, i + j - off + 1, k)) {
                        dp[i][off] = true;
                        return true;
                    }
                }
            }
        }
        dp[i][off] = false;
        return false;
    }

    public static void main(String[] args) throws Exception {
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

