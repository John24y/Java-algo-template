package main.nk;
import java.io.*;
import java.util.*;

class UnionFind {
    int[] fa;
    int[] sz;//包含节点数量
    int[] edgeCnt;//包含边数

    public UnionFind(int n) {
        fa=new int[n+1];
        sz=new int[n+1];
        edgeCnt=new int[n+1];
        for (int i = 0; i <= n; i++) {
            fa[i]=i;
            sz[i]=1;
        }
    }

    int find(int x) {
        if (fa[x]!=x){
            fa[x]=find(fa[x]);
        }
        return fa[x];
    }

    void union(int i, int j) {
        int f1=find(i);
        int f2=find(j);
        edgeCnt[f1]++;
        if (f1==f2)return;
        fa[f2]=f1;
        sz[f1]+=sz[f2];
        edgeCnt[f1]+=edgeCnt[f2];
    }

}
class Pair<K, V> {
    private K key;
    private V value;

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }
}
public class Main {

    void solve() {
        int n = nextInt();
        int m = nextInt();

        List<Pair<Integer, List<Integer>>> costs = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            int k = nextInt();
            int cost = nextInt();
            List<Integer> vertices = new ArrayList<>();
            for (int j = 0; j < k; j++) {
                vertices.add(nextInt());
            }
            costs.add(new Pair<>(cost, vertices));
        }

        costs.sort(new Comparator<Pair<Integer, List<Integer>>>() {
            @Override
            public int compare(Pair<Integer, List<Integer>> o1, Pair<Integer, List<Integer>> o2) {
                return Integer.compare(o1.getKey(), o2.getKey());
            }
        });
        UnionFind uf = new UnionFind(n + 1);
        long result = 0;

        for (Pair<Integer, List<Integer>> pair : costs) {
            long cost = pair.getKey();
            List<Integer> vertices = pair.getValue();
            Set<Integer> groups = new HashSet<>();
            for (int vertex : vertices) {
                groups.add(uf.find(vertex));
            }
            result += (long) (groups.size() - 1) * cost;
            int firstVertexRoot = uf.find(vertices.get(0));
            for (int vertex : vertices) {
                uf.union(firstVertexRoot, uf.find(vertex));
            }
        }

        Set<Integer> finalGroups = new HashSet<>();
        for (int v = 1; v <= n; v++) {
            finalGroups.add(uf.find(v));
        }

        if (finalGroups.size() > 1) {
            System.out.println(-1);
        } else {
            System.out.println(result);
        }
    }

    public static void main(String[] args) throws Exception {
        int t=1;
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

