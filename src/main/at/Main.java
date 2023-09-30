package main.at;
import java.util.*;
import java.io.*;
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
        int f1=find(fa[i]);
        int f2=find(fa[j]);
        if (f1>f2){
            int t=f2;
            f2=f1;
            f1=t;
        }
        edgeCnt[f1]++;
        if (f1==f2)return;
        fa[f2]=f1;
        sz[f1]+=sz[f2];
        edgeCnt[f1]+=edgeCnt[f2];
    }

}

public class Main {
    List<List<int[]>> g;
    public void solve() throws Exception {
        int n = nextInt(), m = nextInt();
        g = new ArrayList<>();
        for (int i = 0; i < n + 1; i++) {
            g.add(new ArrayList<>());
        }
        List<int[]> edges = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            int a=nextInt()-1,b=nextInt()-1,w=nextInt();
            g.get(a).add(new int[]{b,w});
            g.get(b).add(new int[]{a,w});
            edges.add(new int[]{a,b,w});
        }

        int ans = (int) 2e9;
        for (List<int[]> ints : g) {
            int m1=Integer.MAX_VALUE,m2=Integer.MAX_VALUE;
            for (int[] ar : ints) {
                if (ar[1]<m1) {
                    m2=m1;
                    m1=ar[1];
                } else if (ar[1]<m2) {
                    m2=ar[1];
                }
            }
            if (ints.size()>=2) {
                ans=Math.min(ans, m1+m2);
            }
        }

        Collections.sort(edges, Comparator.comparingInt(x->x[2]));
        UnionFind find = new UnionFind(n*2);
        for (int[] edge : edges) {
            find.union(edge[0],edge[1]+n);
            find.union(edge[0]+n,edge[1]);
            if (find.find(edge[0])==find.find(edge[1])) {
                ans=Math.min(ans, edge[2]);
                break;
            }
            if (edge[2]>ans) {
                break;
            }
        }
        out.println(ans);
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


