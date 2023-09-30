package main.nk;

import java.io.*;
import java.util.*;

class SumSegTree {

    static class Node {
        Node left;
        Node right;
        long sum;
        long lazy;
        int ls, rs;//debug用
    }

    int maxN;
    Node root;
    int mod;

    public SumSegTree(int maxN, int mod) {
        this.maxN = maxN;
        this.root = new Node();
        root.ls = 0;
        root.rs = maxN;
        this.mod = mod;
    }

    long trim(long v) {
        //可以加上取余
        return v%mod;
    }

    public void add(int l, int r, long val) {
        add(root, l, r, val, 0, maxN);
    }

    /**
     * 当前Node的范围: [ls,rs]
     */
    private void add(Node node, int l, int r, long val, int ls, int rs) {
        if (r<l){
            return;
        }
        if (r<0){
            throw new IllegalArgumentException("index:"+r);
        }
        if (l <= ls && rs <= r) {
            //[l,r]覆盖了当前子树
            node.sum = trim(node.sum+(rs - ls + 1) * val);
            node.lazy = trim(node.lazy + val);
            return;
        }

        pushDown(node, ls, rs);
        int mid = ls + rs >> 1;
        //左子树[ls,mid]
        //右子树[mid+1,rs]
        if (l <= mid) {
            add(node.left, l, r, val, ls, mid);
        }
        if (r >= mid + 1) {
            add(node.right, l, r, val, mid + 1, rs);
        }
        int cover=Math.min(r,rs)-Math.max(l,ls)+1;
        node.sum = trim(node.sum + val * cover);
    }

    void pushDown(Node node, int ls, int rs) {
        int mid = ls + rs >> 1;
        if (node.left == null) {
            node.left = new Node();
            node.left.ls = ls;
            node.left.rs = mid;
        }
        if (node.right == null) {
            node.right = new Node();
            node.right.ls = mid + 1;
            node.right.rs = rs;
        }
        //pushDown的时候并没有去下发lazy值,因为对于求sum来说,可以在query的时候把整条路径上的lazy都算上
    }

    public long sum(int l, int r) {
        return sum(root, l, r, 0, maxN);
    }

    private long sum(Node node, int l, int r, int ls, int rs) {
        if (r<l) {
            return 0;
        }
        if (r<0){
            throw new IllegalArgumentException("index:"+r);
        }
        if (l <= ls && rs <= r) {
            return node.sum;
        }
        pushDown(node, ls, rs);
        int mid = ls + rs >> 1;
        int cover=Math.min(r,rs)-Math.max(l,ls)+1;
        long res = trim(cover*node.lazy);
        if (l <= mid) {
            res += sum(node.left, l, r, ls, mid);
        }
        if (r >= mid + 1) {
            res += sum(node.right, l, r, mid + 1, rs);
        }
        return trim(res);
    }
}
class TreePathDecomposition {
    List<List<Integer>> graph;
    int[] deep,dfn,dfnOut,parent,top,size,hson;
    int n,root,seq;
    TreePathDecomposition(List<List<Integer>> graph, int root) {
        this.graph=graph;
        this.n=graph.size();
        this.root=root;
        deep=new int[n];
        dfn=new int[n];
        dfnOut=new int[n];
        parent=new int[n];
        top=new int[n];
        size=new int[n];
        hson=new int[n];
        dfs1(root, -1);
        dfs2(root, -1, root);
    }

    void dfs1(int i, int p) {
        size[i]+=1;
        deep[i]=p==-1?1:deep[p]+1;
        hson[i]=-1;
        int mx=0;
        for (Integer ch : graph.get(i)) {
            if (ch==p) continue;
            dfs1(ch, i);
            size[i]+=size[ch];
            if (size[ch]>mx) {
                hson[i]=ch;
                mx=size[ch];
            }
        }
    }

    void dfs2(int i, int p, int ptop) {
        dfn[i]=seq++;
        parent[i]=p;
        top[i]=ptop;
        if (hson[i]!=-1) {
            dfs2(hson[i], i, ptop);
            for (Integer ch : graph.get(i)) {
                if (ch == p || ch == hson[i]) continue;
                dfs2(ch, i, ch);
            }
        }
        dfnOut[i]=seq-1;
    }

    int lca(int u, int v) {
        while (top[u]!=top[v]) {
            if (deep[top[u]]>deep[top[v]]) {
                int t=u;
                u=v;
                v=t;
            }
            v=parent[top[v]];
        }
        return deep[u]>deep[v]?v:u;
    }

    void pathSeg(int u, int v, SegOperator op) {
        while (top[u]!=top[v]) {
            if (deep[top[u]]>deep[top[v]]) {
                int t=u;
                u=v;
                v=t;
            }
            op.seg(dfn[top[v]], dfn[v]);
            v=parent[top[v]];
        }
        op.seg(Math.min(dfn[u], dfn[v]), Math.max(dfn[u], dfn[v]));
    }

    interface SegOperator {
        //[a,b]
        void seg(int a, int b);
    }
}

public class Main implements Runnable {
    void solve() {
        int n=nextInt(),m=nextInt(),r=nextInt(),p=nextInt();
        SumSegTree tree = new SumSegTree(n, p);
        int[] w=new int[n+1];
        List<List<Integer>> g=new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            w[i]=nextInt();
            g.add(new ArrayList<>());
        }
        g.add(new ArrayList<>());
        for (int i = 0; i < n - 1; i++) {
            int a=nextInt() ,b=nextInt();
            g.get(a).add(b);
            g.get(b).add(a);
        }
        TreePathDecomposition de = new TreePathDecomposition(g, r);
        for (int i = 1; i <= n; i++) {
            tree.add(de.dfn[i],de.dfn[i],w[i]);
        }
        for (int i = 0; i < m; i++) {
            int op=nextInt();
            if (op==1) {
                int x=nextInt(),y=nextInt(),z=nextInt();
                de.pathSeg(x, y, new TreePathDecomposition.SegOperator() {
                    @Override
                    public void seg(int a, int b) {
                        tree.add(a,b,z);
                    }
                });
            } else if (op==2) {
                int x=nextInt(),y=nextInt();
                int[] v=new int[1];
                de.pathSeg(x, y, new TreePathDecomposition.SegOperator() {
                    @Override
                    public void seg(int a, int b) {
                        v[0]+=tree.sum(a,b);
                        v[0]%=p;
                    }
                });
                out.println(v[0]);
            } else if (op==3) {
                int x=nextInt(),z=nextInt();
                tree.add(de.dfn[x], de.dfnOut[x], z);
            } else if (op==4) {
                int x=nextInt();
                out.println(tree.sum(de.dfn[x], de.dfnOut[x]));
            }
        }
    }

    public static void main(String[] args) throws Exception {
        new Thread(null, new Main(), "CustomThread", 1024 * 1024 * 100).start();
    }

    @Override
    public void run() {
        if (true) {
            new Main().solve();
        } else {
            int t = nextInt();
            for (int i = 0; i < t; i++) {
                new Main().solve();
            }
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

