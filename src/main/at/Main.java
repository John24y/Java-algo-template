package main.at;

import java.io.*;
import java.util.*;

public class Main implements Runnable{
    int n;
    int[][] dp;
    int[][] card;
    public void solve() {
        n=nextInt();
        dp=new int[2][1<<n];
        card=new int[n][2];
        Arrays.fill(dp[0],-1);
        Arrays.fill(dp[1],-1);
        for (int i = 0; i < n; i++) {
            card[i][0]=nextInt();
            card[i][1]=nextInt();
        }
        int r = dfs((1 << n) - 1, 1);
        System.out.println(r==1?"Takahashi":"Aoki");
    }

    int dfs(int s, int turn) {
        if (dp[turn][s]!=-1) {
            return dp[turn][s];
        }
        for (int i = 0; i < n; i++) {
            if (((s>>i)&1)==0) continue;
            for (int j = 0; j < n; j++) {
                if (((s>>j)&1)==0 || i==j) continue;
                if (card[i][0]==card[j][0]||card[i][1]==card[j][1]){
                    int r=dfs(s-(1<<i)-(1<<j),1^turn);
                    if (r==0) {
                        dp[turn][s]=1;
                        return 1;
                    }
                }
            }
        }
        dp[turn][s]=0;
        return 0;
    }


    public static void main(String[] args) throws Exception {
        new Thread(null, new Main(), "CustomThread", 1024 * 1024 * 100).start();
    }

    @Override
    public void run() {
        int t = 1;
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