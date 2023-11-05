package main.luogu;

import java.io.*;
import java.util.*;

public class Main {
    void slv() {
        int n=nextInt(),v=nextInt();
        int[][][] ck=new int[n][][];
        for (int i = 0; i < n; i++) {
            int c=nextInt();
            ck[i]=new int[c][2];
            for (int j = 0; j < c; j++) {
                ck[i][j][0]=nextInt();
                ck[i][j][1]=nextInt();
            }
        }
        int q=nextInt();
        for (int qq = 0; qq < q; qq++) {
            int l=nextInt(),r=nextInt();
            long[] dp=new long[v+1];
            Arrays.fill(dp,-1);
            dp[1]=0;
            for (int i = l-1; i < r; i++) {
                long[] ndp=new long[v+1];
                Arrays.fill(ndp,-1);
                for (int j = 1; j <= v; j++) {
                    if (dp[j]==-1) continue;
                    ndp[j]=Math.max(ndp[j],dp[j]);
                    for (int k = 0; k < ck[i].length; k++) {
                        if (ck[i][k][1]*j>v)continue;
                        ndp[ck[i][k][1]*j]=Math.max(ndp[ck[i][k][1]*j],dp[j]+ck[i][k][0]);
                    }
                }
                dp=ndp;
            }
            long res=0;
            for (int i = 0; i < v + 1; i++) {
                res=Math.max(res,dp[i]);
            }
            out.println(res);
        }
    }

    public static void main(String[] args) throws IOException {
        int t=1;
        for (int i = 0; i < t; i++) {
            new Main().slv();
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