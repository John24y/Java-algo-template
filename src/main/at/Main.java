package main.at;

import java.io.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Main implements Runnable {

    public int partition(List<Integer> list, int left, int right, Comparator<Integer> comparator) {
        int p = ThreadLocalRandom.current().nextInt(left, right + 1);
        Collections.swap(list, right, p);
        int pi = list.get(right);
        int j = 0;
        for (int i = left; i < right; i++) {
            if (comparator.compare(list.get(i), pi) <= 0) {
                Collections.swap(list, i, j);
                j++;
            }
        }
        Collections.swap(list, j, right);
        return j;
    }

    // [left,right]
    public int kth(List<Integer> list, int left, int right, int k, Comparator<Integer> comparator) {
        if (left == right) {
            return left;
        }
        int p = partition(list, left, right, comparator);
        if (p - left + 1 == k) {
            return p;
        } else if (p - left + 1 > k) {
            return kth(list, left, p - 1, k, comparator);
        } else {
            return kth(list, p + 1, right, k - (p - left + 1), comparator);
        }
    }

    public void solve() {
        int n=nextInt();
        List<Integer> rem = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            rem.add(i+1);
        }
        int next = n+1;
        while (rem.size() > 1) {
            List<Integer> nrem = new ArrayList<>();
            int kth = kth(rem, 0, rem.size() - 1, rem.size() / 2, new Comparator<Integer>() {
                @Override
                public int compare(Integer o1, Integer o2) {
                    out.println("? " + o1 + " " + o2);
                    out.flush();
                    return nextInt();
                }
            });
            for (int i = 0; i < (rem.size() / 2); i++) {
                out.println("+ " + rem.get(i) + " " + rem.get(rem.size() - i - 1));
                out.flush();
                int r = nextInt();
                if (r==-1) {
                    return;
                }
                nrem.add(next);
                next++;
            }
            if (rem.size()%2==1) {
                nrem.add(rem.get(rem.size() / 2));
            }
            rem = nrem;
        }
        out.println("!");
    }

    public static void main(String[] args) throws Exception {
        new Thread(null, new Main(), "CustomThread", 1024 * 1024 * 100).start();
    }

    @Override
    public void run() {
        int t = 1;
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