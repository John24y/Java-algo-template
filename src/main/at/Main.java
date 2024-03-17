package main.at;

import java.io.*;
import java.util.*;

public class Main implements Runnable {
    public static int gcd(int a, int b) {
        if (a == 0) {
            return b;
        }
        return gcd(b % a, a);
    }

    public static int[] simplify(int x, int y) {
        if (x == 0) return new int[]{0, 1};
        if (y == 0) return new int[]{1, 0};
        int gcd = gcd(Math.abs(x), Math.abs(y));
        x /= gcd;
        y /= gcd;
        if (x < 0) {
            x *= -1;
            y *= -1;
        }
        return new int[]{x, y};
    }

    void solve() {
        int n = nextInt();
        List<int[]> points = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            int x=nextInt(),y=nextInt();
            points.add(new int[]{x, y});
        }

        int maxAlign = 0;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                int dx = points.get(i)[0] - points.get(j)[0];
                int dy = points.get(i)[1] - points.get(j)[1];
                int[] simplified = simplify(dx, dy);
                dx = simplified[0];
                dy = simplified[1];
                int count = 2;
                for (int k = 0; k < n; k++) {
                    if (k == i || k == j) continue;
                    int tx = points.get(k)[0] - points.get(j)[0];
                    int ty = points.get(k)[1] - points.get(j)[1];
                    int[] simplifiedK = simplify(tx, ty);
                    if (simplifiedK[0] == dx && simplifiedK[1] == dy) {
                        count++;
                    }
                }
                maxAlign = Math.max(maxAlign, count);
            }
        }

        int target = n / 3;
        System.out.println(Math.min(target, n - maxAlign));
    }

    public static void main(String[] args) throws Exception {
        new Thread(null, new Main(), "CustomThread", 1024 * 1024 * 100).start();
    }

    @Override
    public void run() {
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

