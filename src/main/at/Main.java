package main.at;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

class HeuUnionFind {
    int[] fa;
    int[] sz;//包含节点数量
    long[] offset; // 每个节点相比于当前根节点的增量, offset[i]=val[i]-val[root[i]]
    List<List<Integer>> vertex = new ArrayList<>();

    public HeuUnionFind(int n) {
        fa=new int[n+1];
        sz=new int[n+1];
        offset=new long[n+1];
        for (int i = 0; i <= n; i++) {
            fa[i]=i;
            sz[i]=1;
            vertex.add(new ArrayList<>());
            vertex.get(i).add(i);
        }
    }

    int find(int x) {
        if (fa[x]!=x){
            int r = find(fa[x]);
            fa[x]=r;
        }
        return fa[x];
    }

    //val[j]-val[i]=d
    boolean union(int i, int j, long d) {
        int f1=find(i);
        int f2=find(j);
        if (f1==f2) {
            return offset[j]-offset[i]==d;
        }
        if (sz[f1]<sz[f2]) {
            return union(j,i,-d);
        }
        long nd = d-offset[j]+offset[i];
        fa[f2]=f1;
        sz[f1]+=sz[f2];
        for (Integer v : vertex.get(f2)) {
            offset[v]+=nd;
            vertex.get(f1).add(v);
        }
        vertex.get(f2).clear();
        return true;
    }

}

public class Main {

    public void solve() throws Exception {
        int n=nextInt(),q=nextInt();
        HeuUnionFind find = new HeuUnionFind(n+1);
        for (int i = 1; i <= q; i++) {
            int a=nextInt(),b=nextInt();
            long d=nextLong();
            if (find.union(a,b,-d)){
                out.print((i)+" ");
            }
        }
        out.println();
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

