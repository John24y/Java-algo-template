package main.nk2;
import main.LCUtils;

import java.io.*;
import java.util.*;


import java.util.*;

class M1 {
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

    long[] solve(int n, int q, int[] gc, int[][] b, long[][] qs) {
        long[] res=new long[q];
        this.n=n;
        this.b=b;
        int[] bmx=new int[n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < gc[i]; j++) {
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
            if ((MX/gc[i])<ww) {
                break;
            } else {
                ww*=(long)gc[i];
            }
//            if (ww>MX) break;
        }

        for (int i = 0; i < q; i++) {
            long h=qs[i][0];
            long l=qs[i][1];
            long r=qs[i][2];
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
                res[i]=-1;
            } else {
                res[i]=lo;
            }
        }
        return res;
    }

}

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

    public static long[] solve(int n, int q, int[] gc, int[][] bb, long[][] qs) {
        long[] res=new long[q];
        long cur = 1;
        for (int i = 0; i < n; i++) {
            a[i] = gc[i];
            t[i] = cur;
            if (cur != INF) {
                if (cur > INF / a[i]) cur = INF;
                else cur *= a[i];
            }
            b[i] = new ArrayList<>();
            dp[i] = new ArrayList<>();
            for (int j = 0; j < a[i]; j++) {
                b[i].add(bb[i][j]);
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
        for (int i = 0; i < q; i++) {
            h = (int) qs[i][0];
            l = qs[i][1];
            r = qs[i][2];
            ans = -1;
            dfs(n - 1, 0, l, r, h);
            res[i]=ans;
        }
        return res;
    }
}

public class Main {

    void solve() {
        int n=nextInt(),q=nextInt();
        int[] a=new int[n];
        int[][] b =new int[n][50];
        for (int i = 0; i < n; i++) {
            a[i]=nextInt();
            for (int j = 0; j < a[i]; j++) {
                b[i][j]=nextInt();
            }
        }
        long[][] qs=new long[q][3];
        for (int i = 0; i < q; i++) {
            long h=nextLong(),l=nextLong(),r=nextLong();
            qs[i][0]=h;
            qs[i][1]=l;
            qs[i][2]=r;
        }
        long[] r2 = M2.solve(n, q, a, b, qs);
        long[] r1 = new M1().solve(n, q, a, b, qs);
        for (int i = 0; i < q; i++) {
            if (r1[i]!=r2[i]) {
                throw new RuntimeException(String.format("n %s,q %s, a %s, b %s, qs %s",
                        n,q,Arrays.toString(a), Arrays.deepToString(b), Arrays.deepToString(qs)));
            }
            out.println(r2[i]);
        }
    }

    void solve2() {
        int n=2,q=99475;
        int[] a=new int[]{22,19};
        int[][] b =new int[][] {
                new int[]{198, 63, 218, 745, 226, 491, 966, 915, 770, 584, 304, 415, 828, 73, 159, 221, 292, 425, 707, 667, 572, 781, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                new int[]{124, 236, 370, 406, 552, 645, 720, 778, 890, 933, 1133, 1292, 1377, 1573, 1625, 1668, 1754, 1867, 2064, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
        };
        long[][] qs= new long[][]{
                new long[] {841, 26, 26}
        };
        q = qs.length;

        long[] r2 = M2.solve(n, q, a, b, qs);
        long[] r1 = new M1().solve(n, q, a, b, qs);
        for (int i = 0; i < q; i++) {
            if (r1[i]!=r2[i]) {
                throw new RuntimeException(String.format("n %s,q %s, a %s, b %s, qs %s",
                        n,q,Arrays.toString(a), Arrays.deepToString(b), Arrays.deepToString(qs)));
            }
            out.println(r2[i]);
        }
    }

    public static void main(String[] args) throws Exception {
        int t=1;
        for (int i = 0; i < t; i++) {
            new Main().solve2();
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

