package main;
import java.io.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Main {
    static int n;
    public static void main(String[] args) throws IOException {
        n=nextInt();
        int[][] ar=new int[2][n];
        boolean[] vis=new boolean[n];
        long res=0;
        long[][][] qs = new long[4][n][2];
        for (int i = 0; i < n; i++) {
            ar[0][i]=nextInt();
            ar[1][i]=nextInt();
            for (int j = 0; j < 4; j++) {
                qs[j][i][1]=i;
            }
            int ci = n-i;
            qs[0][i][0]= (long) (ar[0][i] + ar[1][i]) << 30 | ci;
            qs[1][i][0]= (long) (ar[0][i] - ar[1][i]) << 30 | ci;
            qs[2][i][0]= (long) (-ar[0][i] + ar[1][i]) << 30 | ci;
            qs[3][i][0]= (long) (-ar[0][i] - ar[1][i]) << 30 | ci;
        }
        for (int i = 0; i < 4; i++) {
            Arrays.sort(qs[i], Comparator.<long[]>comparingLong(x->x[0]));
        }

        int[] top = new int[]{n-1,n-1,n-1,n-1};
        long x=0,y=0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < 4; j++) {
                while (vis[(int) qs[j][top[j]][1]]) {
                    top[j]--;
                }
            }
            int seli = (int) qs[0][top[0]][1];
            long dis = Math.abs(ar[0][seli]-x)+Math.abs(ar[1][seli]-y);
            for (int j = 1; j < 4; j++) {
                int ni = (int) qs[j][top[j]][1];
                long dis1 = Math.abs(ar[0][ni]-x)+Math.abs(ar[1][ni]-y);
                if (dis1 > dis || dis1 == dis && ni < seli) {
                    seli = ni;
                    dis = dis1;
                }
            }
            vis[seli] = true;
            res += Math.abs(ar[0][seli]-x) + Math.abs(ar[1][seli]-y);
            x = ar[0][seli];
            y = ar[1][seli];
        }
        out.println(res);
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

