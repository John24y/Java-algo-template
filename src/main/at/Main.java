package main.at;

import java.io.*;
import java.util.*;

public class Main {
    void solve() {
        int n=readInt(),m=readInt();
        List<List<Integer>> S=new ArrayList<>();
        double[] C=new double[n+1];
        int[] P=new int[n+1];
        for (int i = 0; i < n; i++) {
            S.add(new ArrayList<>());
            C[i]=readInt();
            P[i]=readInt();
            int z=0;
            for (int j = 0; j < P[i]; j++) {
                int v=readInt();
                if (v==0){
                    z++;
                } else {
                    S.get(i).add(v);
                }
            }
            if (P[i]-z==0) {
                C[i]=P[i]=0;
            } else {
                C[i]=C[i]* (double) P[i]/(double)(P[i]-z);
                P[i]-=z;
            }
        }

        double[] dp=new double[m+1];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                
                for (int k = 0; k < P[i]; k++) {

                }
            }
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

