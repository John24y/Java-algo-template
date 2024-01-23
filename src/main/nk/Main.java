package main.nk;

import java.io.*;
import java.util.*;

public class Main {
    Map<Integer,Integer> nums=new HashMap<>();
    Map<Integer,Boolean> prec=new HashMap<>();
    void solve() {
        int n=nextInt(),m=nextInt();
        for (int j = 0; j < n; j++) {
            int v = Integer.parseInt(next(), 2);
            int c=nextInt();
            nums.put(v,nums.getOrDefault(v,0)+c);
            for (int i = 1; i <= m; i++) {
                prec.put((v>>>i)<<i, true);
            }
        }
        out.println(dfs(0,m-1)[1]);
    }

    int[] dfs(int pre, int i) {
        if (i==0){
            Integer c1 = nums.getOrDefault(pre, 0);
            Integer c2 = nums.getOrDefault(pre|1, 0);
            return new int[] {c1+c2,Math.min(c1,c2)};
        }
        if (!prec.getOrDefault(pre,false)){
            return new int[2];
        }
        int[] r1=dfs(pre,i-1);
        int[] r2=dfs(pre|(1<<i),i-1);
        return new int[] {r1[0]+r2[0], Math.min(r1[0], r2[0]) + r1[1]+r2[1]};
    }

    public static void main(String[] args) throws Exception {
        int t=nextInt();
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

