package main.at;

import java.io.*;
import java.util.StringTokenizer;

public class Main {

    long[][] dp(long[][] a, int m) {
        int n=a.length;
        long[][] pre=new long[n+1][n+1];
        long[][] dp=new long[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                pre[i+1][j+1]=pre[i][j+1]+pre[i+1][j]-pre[i][j]+a[i][j];
                if (i+1>=m&&j+1>=m){
                    long area=pre[i+1][j+1]-pre[i-m+1][j+1]-pre[i+1][j-m+1]+pre[i-m+1][j-m+1];
                    dp[i][j]=Math.max(area, Math.max(j-1>=0?dp[i][j-1]:0, i-1>=0?dp[i-1][j]:0));
                }
            }
        }
        return dp;
    }

    void revx(long[][] a) {
        int n=a.length;
        for (int i = 0; i < n; i++) {
            int j=n-i-1;
            if (i>=j) break;
            long[] t=a[i];
            a[i]=a[j];
            a[j]=t;
        }
    }

    void revy(long[][] a) {
        int n=a.length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int k=n-j-1;
                if (j>=k) break;
                long t=a[i][j];
                a[i][j]=a[i][k];
                a[i][k]=t;
            }
        }
    }

    void slv() {
        int n=nextInt(),m=nextInt();
        long[][] a=new long[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                a[i][j]=nextInt();
            }
        }
        long[][] dp1 = dp(a, m);
        revy(a);
        long[][] dp2 = dp(a, m);
        revy(dp2);

        revx(a);
        long[][] dp4 = dp(a, m);
        revx(dp4);
        revy(dp4);

        revy(a);
        long[][] dp3 = dp(a, m);
        revx(dp3);

        long res=0;
        for (int i = 0; i < n-1; i++) {
            for (int j = 0; j < n-1; j++) {
                long d1=dp1[i][j];
                long d2=dp2[i][j+1];
                long d3=dp3[i+1][j];
                long d4=dp4[i+1][j+1];
                res=Math.max(res, d1+d2+d3);
                res=Math.max(res, d1+d2+d4);
                res=Math.max(res, d1+d3+d4);
                res=Math.max(res, d2+d3+d4);
            }
        }
        out.println(res);
    }

    public static void main(String[] args) throws IOException {
        new Main().slv();
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