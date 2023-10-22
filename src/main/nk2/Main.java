package main.nk2;

import java.io.*;
import java.util.*;

class BFSMatching {
    List<List<Integer>> g;
    int[] pa, pb, want;
    int n, m;

    public BFSMatching(int _n, int _m) {
        n = _n;
        m = _m;
        pa = new int[n];
        pb = new int[m];
        Arrays.fill(pa, -1);
        Arrays.fill(pb, -1);
        g = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            g.add(new ArrayList<>());
        }
    }

    public void add(int from, int to) {
        g.get(from).add(to);
    }

    public boolean bfs(int l) {
        //want[i]是尝试匹配右侧点i的左侧点
        want=new int[m];
        Arrays.fill(want, -1);
        //寻找匹配的子问题（左侧点），只要任意一个找到空位匹配，整个问题就有解
        ArrayDeque<Integer> q=new ArrayDeque<>(n);
        q.add(l);
        while (!q.isEmpty()) {
            l = q.pollFirst();
            for (Integer r : g.get(l)) {
                if (want[r]==-1) {
                    want[r] = l;
                    if (pb[r]==-1) {
                        //有解，回溯答案，更新匹配
                        int nextr = r;
                        while (nextr != -1) {
                            r = nextr;
                            l = want[nextr];
                            nextr = pa[l];
                            pa[l] = r;
                            pb[r] = l;
                        }
                        return true;
                    } else {
                        //每个左侧点至多加入一次队列
                        q.add(pb[r]);
                    }
                }
            }
        }
        return false;
    }

    public int solve() {
        int ans=0;
        for (int i = 0; i < n; i++) {
            if (bfs(i)) {
                ans++;
            }
        }
        return ans;
    }
}
public class Main {
    public static boolean[] isPrimes(int n) {
        boolean[] isPrime = new boolean[n + 1];
        Arrays.fill(isPrime, true);
        isPrime[0] = false;
        isPrime[1] = false;
        int i = 2;
        while (i * i <= n) {
            if (isPrime[i]) {
                for (int j = i * i; j <= n; j += i) {
                    isPrime[j] = false;
                }
            }
            i++;
        }
        return isPrime;
    }
    public void solve() throws Exception {
        boolean[] primes = isPrimes(600005);
        int n = nextInt();
        int[] a=new int[n];
        for (int i = 0; i < n; i++) {
            a[i]=nextInt();
        }
        BFSMatching matching = new BFSMatching(n, n);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i!=j&&a[i]%2==1&&primes[a[i]+a[j]]){
                    matching.add(i,j);
                }
            }
        }
        out.println(matching.solve());
    }

    public static void main(String[] args) throws Exception {
        new Main().solve();
        out.flush();
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

