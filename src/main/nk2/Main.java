package main.nk2;

import java.io.*;

import java.io.IOException;
import java.util.*;

public class Main {
    static int n;
    private static int toi(long v) {
        return n - (int) ((Integer.MAX_VALUE>>3)&v);
    }
    public static void main(String[] args) throws IOException {
        FastInput in = new FastInput(System.in);
        n=in.ri();
        int[][] ar=new int[2][n];
        boolean[] vis=new boolean[n];
        long res=0;
        long[][] qs = new long[4][n];
        for (int i = 0; i < n; i++) {
            ar[0][i]=in.ri();
            ar[1][i]=in.ri();
            int ci = n-i;
            qs[0][i]= (long) (ar[0][i] + ar[1][i]) << 30 | ci;
            qs[1][i]= (long) (ar[0][i] - ar[1][i]) << 30 | ci;
            qs[2][i]= (long) (-ar[0][i] + ar[1][i]) << 30 | ci;
            qs[3][i]= (long) (-ar[0][i] - ar[1][i]) << 30 | ci;
        }
        for (int i = 0; i < 4; i++) {
            Arrays.sort(qs[i]); //
        }
        int[] top = new int[]{n-1,n-1,n-1,n-1};
        long x=0,y=0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < 4; j++) {
                while (vis[toi(qs[j][top[j]])]) {
                    top[j]--;
                }
            }
            int seli = toi(qs[0][top[0]]);
            long dis = Math.abs(ar[0][seli]-x)+Math.abs(ar[1][seli]-y);
            for (int j = 1; j < 4; j++) {
                int ni = toi(qs[j][top[j]]);
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
    static class FastInput {
        private final InputStream is;
        private byte[] buf = new byte[1 << 13];
        private int bufLen;
        private int bufOffset;
        private int next;

        public FastInput(InputStream is) {
            this.is = is;
        }

        public void populate(int[] data) {
            for (int i = 0; i < data.length; i++) {
                data[i] = readInt();
            }
        }

        private int read() {
            while (bufLen == bufOffset) {
                bufOffset = 0;
                try {
                    bufLen = is.read(buf);
                } catch (IOException e) {
                    bufLen = -1;
                }
                if (bufLen == -1) {
                    return -1;
                }
            }
            return buf[bufOffset++];
        }

        public void skipBlank() {
            while (next >= 0 && next <= 32) {
                next = read();
            }
        }

        public int ri() {
            return readInt();
        }

        public int[] ri(int n) {
            int[] ans = new int[n];
            populate(ans);
            return ans;
        }

        public int readInt() {
            boolean rev = false;

            skipBlank();
            if (next == '+' || next == '-') {
                rev = next == '-';
                next = read();
            }

            int val = 0;
            while (next >= '0' && next <= '9') {
                val = val * 10 - next + '0';
                next = read();
            }

            return rev ? val : -val;
        }

    }
    static PrintWriter out = new PrintWriter(System.out, true);
}

