package main.at;

import java.io.*;
import java.util.*;

public class Main {
    List<List<Integer>> g=new ArrayList<>();
    int[] cnt;
    void solve() {
        int n=readInt();
        cnt=new int[n+1];
        for (int i = 0; i < n + 1; i++) {
            g.add(new ArrayList<>());
        }
        for (int i = 1; i < n; i++) {
            int a=readInt(),b=readInt();
            g.get(a).add(b);
            g.get(b).add(a);
        }
        dfs(1,0);
        dfs2(1,0,0);
        System.out.println(ans);
    }
    long ans=0;

    void dfs(int i, int p) {
        int res=1;
        for (Integer ch : g.get(i)) {
            if (ch==p)continue;
            dfs(ch,i);
            res+=cnt[ch];
        }
        cnt[i]=res;
    }

    void dfs2(int i, int p, int pCnt) {
        List<Integer> sz=new ArrayList<>();
        sz.add(pCnt);
        for (Integer ch : g.get(i)) {
            if (ch==p)continue;
            sz.add(cnt[ch]);
        }
        long sum=0;
        long[][] dp=new long[sz.size()+1][4];
        dp[0][0]=1;
        for (int j = 0; j < sz.size(); j++) {
            sum+=sz.get(j);
            dp[j+1][0]=1;
            for (int k = 1; k < 4; k++) {
                dp[j+1][k]+=dp[j][k-1]*sz.get(j);
                dp[j+1][k]+=dp[j][k];
            }
        }
        ans+=dp[sz.size()][3];
        for (Integer ch : g.get(i)) {
            if (ch==p)continue;
            dfs2(ch, i, (int)sum-cnt[ch]+1);
        }
    }

    public static void main(String[] args) throws Exception {
        new Main().solve();
        out.flush();
    }

    static PrintWriter out = new PrintWriter(System.out, false);
    static InputReader in = new InputReader(System.in);
    static String next() { return in.next(); }
    static int readInt() { return Integer.parseInt(in.next()); }
    static long readLong() { return Long.parseLong(in.next()); }
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

