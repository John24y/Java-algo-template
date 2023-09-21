package main.at;
import java.util.*;
import java.io.*;
class BipartiteMatching {
    List<List<Integer>> g;
    int[] pa, pb, was;
    int n, m, res, iter;

    public BipartiteMatching(int _n, int _m) {
        n = _n;
        m = _m;
        pa = new int[n];
        pb = new int[m];
        was = new int[n];
        Arrays.fill(pa, -1);
        Arrays.fill(pb, -1);
        g = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            g.add(new ArrayList<>());
        }
        res = 0;
        iter = 0;
    }

    public void add(int from, int to) {
        g.get(from).add(to);
    }

    public boolean dfs(int v) {
        was[v] = iter;
        for (int u : g.get(v)) {
            if (pb[u] == -1) {
                pa[v] = u;
                pb[u] = v;
                return true;
            }
        }
        for (int u : g.get(v)) {
            if (was[pb[u]] != iter && dfs(pb[u])) {
                pa[v] = u;
                pb[u] = v;
                return true;
            }
        }
        return false;
    }

    public int solve() {
        while (true) {
            iter++;
            int add = 0;
            for (int i = 0; i < n; i++) {
                was=new int[n];
                Arrays.fill(was, -1);
                if (pa[i] == -1 && dfs(i)) {
                    add++;
                }
            }
            if (iter>1 && add>0) {
                throw new RuntimeException();
            }
            if (add == 0) {
                break;
            }
            res += add;
        }
        return res;
    }
}



public class Main {
        public void solve() throws Exception {
        int n=nextInt(),m=nextInt();
        List<List<List<Integer>>> ar=new ArrayList<>();
        for (int i = 0; i < n; i++) {
            ar.add(new ArrayList<>());
            for (int j = 0; j < 10; j++) {
                ar.get(i).add(new ArrayList<>());
            }
            String s=next();
            for (int j = 0; j < m; j++) {
                ar.get(i).get(s.charAt(j)-'0').add(j);
            }
        }
        int lo=0,hi=n*m;
        while (lo<=hi){
            int mid=lo+hi>>1;
            boolean succ=false;
            outer:
            for (int d = 0; d <= 9; d++) {
                List<List<Integer>> graph = new ArrayList<>(n);
                int bc=0;
                Map<Integer,Integer> bs=new HashMap<>();
                for (int i = 0; i < n; i++) {
                    graph.add(new ArrayList<>());
                    List<Integer> li = ar.get(i).get(d);
                    int sz=li.size();
                    if (li.isEmpty()) {
                        continue outer;
                    }
                    for (int j = 0; j < n; j++) {
                        int time=li.get(j%sz)+(j/sz)*m;
                        if (time<=mid){
                            if (!bs.containsKey(time)) {
                                bs.put(time,bc++);
                            }
                            graph.get(i).add(bs.get(time));
                        }
                    }
                }

                BipartiteMatching matching = new BipartiteMatching(n, bs.size());
                for (int i = 0; i < n; i++) {
                    for (int j : graph.get(i)) {
                        matching.add(i, j);
                    }
                }
                if (matching.solve()==n) {
                    succ=true;
                    break;
                }
            }
            if (succ) {
                hi=mid-1;
            } else {
                lo=mid+1;
            }
        }
        out.println(lo>n*m?-1:lo);
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


