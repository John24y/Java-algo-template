package main.nk;
import java.io.*;
import java.util.*;

public class Main {
    long inf = Long.MIN_VALUE;
    int n;
    String s;
    int[] v;
    int[] c;
    long[][] dp;
    long[][] dp2;

    long dfs(int l, int r) {
        if (r<l) return 0;
        if (dp[l][r]!=inf){
            return dp[l][r];
        }
        long res=inf;
        for (int i = r - 1; i >= l; i-=2) {
            long cost=0;
            if (s.charAt(i)==')') {
                cost+=c[i];
            }
            if (s.charAt(r)=='(') {
                cost+=c[r];
            }
            res=Math.max(res, (long)v[r]*v[i]-cost+dfs(i+1,r-1)+dfs(l,i-1));
        }
        dp[l][r]=res;
        return res;
    }

    long dfs2(int r, int canLeft) {
        if (r<0){
            return 0;
        }
        if (r==0) {
            if (canLeft==1) {
                return 0;
            } else {
                return s.charAt(0)=='(' ? -c[0] : 0;
            }
        }
        if (dp2[canLeft][r]!=inf) {
            return dp2[canLeft][r];
        }
        long res=inf;
        if (canLeft==1) {
            res=Math.max(res,(s.charAt(r)==')' ? -c[r] : 0) + dfs2(r-1,canLeft));// 左开不匹配
        }
        res = Math.max(res, (s.charAt(r)=='(' ? -c[r] : 0) + dfs2(r-1,0)); // 右开不匹配
        for (int i = r-1; i >= 0; i-=2) {
            long cost=0;
            if (s.charAt(i)==')') {
                cost+=c[i];
            }
            if (s.charAt(r)=='(') {
                cost+=c[r];
            }
            res=Math.max(res, (long)v[r]*v[i]-cost+dfs2(i-1,canLeft)+dfs(i+1,r-1));
        }
        dp2[canLeft][r]=res;
        return res;
    }

    void solve() {
        n=nextInt();
        s=next();
        v=new int[n];
        c=new int[n];
        dp=new long[n][n];
        dp2=new long[2][n];
        Arrays.fill(dp2[0],inf);
        Arrays.fill(dp2[1],inf);
        for (int i = 0; i < n; i++) {
            Arrays.fill(dp[i],inf);
        }
        for (int i = 0; i < n; i++) {
            v[i]=nextInt();
        }
        for (int i = 0; i < n; i++) {
            c[i]=nextInt();
        }
        long res = dfs2(n - 1, 1);
        System.out.println(res);
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

