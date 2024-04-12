package main.nk;
import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.*;

public class Main {
    long MX = (long) 1e18;
    long[] pre;
    long[] w;
    int n;
    int[][] b;
    long chMax(long v) {
        long res=0;
        long suf=0;
        for (int i = n-1; i >= 0; i--) {
            int bit = w[i]!=-1 ? ((int) (v/w[i])) : 0;
            if (bit>0) {
                int mb = 0;
                for (int j = 0; j < bit; j++) {
                    mb = Math.max(b[i][j], mb);
                }
                res = Math.max(res, suf + pre[i] + mb);
            }
            suf+=b[i][bit];
            v-=(long)bit*w[i];
        }
        if (v!=0) {
            throw new RuntimeException();
        }
        res=Math.max(res, suf);
        return res;
    }
    void solve() {
        n=nextInt();
        int q=nextInt();
        int[] gc=new int[n];
        b=new int[n][50];
        int[] bmx=new int[n];
        w=new long[n];
        for (int i = 0; i < n; i++) {
            gc[i]=nextInt();
            for (int j = 0; j < gc[i]; j++) {
                b[i][j]=nextInt();
                bmx[i]=Math.max(bmx[i],b[i][j]);
            }
        }
        pre=new long[n+1];
        for (int i = 0; i < n; i++) {
            pre[i+1]=pre[i]+bmx[i];
        }

        long ww=1;
        w=new long[n];
        Arrays.fill(w,-1);
        for (int i = 0; i < n; i++) {
            w[i]=ww;
            ww*=(long)gc[i];
            if (ww>MX) break;
        }

        for (int i = 0; i < q; i++) {
            long h=nextLong(),l=nextLong(),r=nextLong();
            long lo=l,hi=r;
            while (lo<=hi){
                long mid=lo+(hi-lo+1)/2;
                if (chMax(mid)>=h){
                    hi=mid-1;
                } else {
                    lo=mid+1;
                }
            }
            if (lo>r){
                out.println(-1);
            } else {
                out.println(lo);
            }
        }
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

