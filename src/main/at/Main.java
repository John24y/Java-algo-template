package main.at;

import java.util.*;
import java.io.*;

class IntervalTree {
    TreeMap<Long,long[]> map=new TreeMap<>();

    //[l,r]
    void add(long l, long r) {
        //map.put(l, r);

    }
}

public class Main {
    public void solve() throws Exception {
        int n=nextInt();
        int[] X=new int[n];
        int[] Y=new int[n];
        for (int i = 0; i < n; i++) {
            X[i]=nextInt();
        }
        for (int i = 0; i < n; i++) {
            Y[i]=nextInt();
        }

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


