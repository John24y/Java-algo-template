package main.at;

import java.io.*;
import java.util.StringTokenizer;

class LazySegTree {
    long M = 998244353;

    public static class Node {
        Node left;
        Node right;
        long lazyAdd;
        long lazyMul=1;
        long sum;
        int ls, rs;//debug用
    }

    int maxN;
    Node root;

    public LazySegTree(int maxN) {
        this.maxN = maxN;
        this.root = createNode(0, maxN);
    }

    Node createNode(int ls, int rs) {
        Node node = new Node();
        node.ls = ls;
        node.rs = rs;
        return node;
    }

    void apply(Node node, int ls, int rs, int type, long val) {
        if (type==1) {
            node.lazyMul=(val*node.lazyMul)%M;
            node.lazyAdd=(val*node.lazyAdd)%M;
            node.sum=(val*node.sum)%M;
        } else {
            node.lazyAdd=(val+node.lazyAdd)%M;
            node.sum=((rs - ls + 1) * val + node.sum)%M;
        }
    }

    /**
     * 两个子区间统计信息进行合并，子区间可以是查询时动态构造的，而不一定是某个node的范围
     */
    void reduce(Node node, Node left, Node right, int ls, int rs) {
        node.sum = left.sum + right.sum;
    }

    /**
     * O(n)设置初始值，但未必时间更短，因为可能不需要每个节点都创建出来
     */
    void build(int[] vals) {
        build(root, vals, 0, maxN);
    }

    private void build(Node node, int[] vals, int ls, int rs) {
        if (ls == rs) {
            if (ls < vals.length) {
                node.sum = vals[ls]%M;
            }
            return;
        }
        pushDown(node, ls, rs);
        int mid = ls + rs >> 1;
        build(node.left, vals, ls, mid);
        build(node.right, vals, mid + 1, rs);
        reduce(node, node.left, node.right, ls, rs);
    }

    public void add(int l, int r, int type, long val) {
        add(root, l, r, type, val, 0, maxN);
    }

    /**
     * 当前Node的范围: [ls,rs]
     */
    private void add(Node node, int l, int r, int type, long val, int ls, int rs) {
        if (l < 0 || r > maxN) {
            throw new IllegalArgumentException();
        }
        if (l <= ls && rs <= r) {
            apply(node, ls, rs, type, val);
            return;
        }
        pushDown(node, ls, rs);
        int mid = ls + rs >> 1;
        if (l <= mid) {
            add(node.left, l, r, type, val, ls, mid);
        }
        if (r >= mid + 1) {
            add(node.right, l, r, type, val, mid + 1, rs);
        }
        reduce(node, node.left, node.right, ls, rs);
    }

    void pushDown(Node node, int ls, int rs) {
        int mid = ls + rs >> 1;
        if (node.left == null) {
            node.left = createNode(ls, mid);
        }
        if (node.right == null) {
            node.right = createNode(mid + 1, rs);
        }
        if (node.lazyMul != 1) {
            apply(node.left, ls, mid, 1, node.lazyMul);
            apply(node.right, mid + 1, rs, 1, node.lazyMul);
            node.lazyMul = 1;
        }
        if (node.lazyAdd != 0) {
            apply(node.left, ls, mid, 2, node.lazyAdd);
            apply(node.right, mid + 1, rs, 2, node.lazyAdd);
            node.lazyAdd = 0;
        }
    }

    private final Node ansNode = new Node();

    public long sum(int l, int r) {
        ansNode.sum = 0;
        query(root, l, r, 0, maxN);
        return ansNode.sum;
    }

    private void query(Node node, int l, int r, int ls, int rs) {
        if (l < 0 || r > maxN) {
            throw new IllegalArgumentException();
        }
        if (l <= ls && rs <= r) {
            reduce(ansNode, node, ansNode, ls, rs);
            return;
        }
        pushDown(node, ls, rs);
        int mid = ls + rs >> 1;
        if (l <= mid) {
            query(node.left, l, r, ls, mid);
        }
        if (r >= mid + 1) {
            query(node.right, l, r, mid + 1, rs);
        }
    }
}

public class Main {

    /**
     * 求逆元，m必须是素数
     */
    static long modinv(long a, long m) {
        return ksm(a, m - 2, m);
    }

    /**
     * 快速幂，求(a^pow)%mod
     */
    static long ksm(long a, long pow, long mod) {
        long skt = 1;
        while (pow > 0) {
            if (pow % 2 != 0) {
                skt = skt * a % mod;
            }
            a = a * a % mod;
            pow = pow >> 1;
        }
        return skt % mod;
    }

    public void solve() throws Exception {
        long M=998244353;
        int n=nextInt(),m=nextInt();
        int[] a=new int[n+1];
        for (int i = 1; i <= n; i++) {
            a[i]=nextInt();
        }
        LazySegTree tree = new LazySegTree(n);
        tree.build(a);
        for (int i = 0; i < m; i++) {
            int l=nextInt(),r=nextInt();
            long x=nextInt();
            x%=M;
            long mi=modinv(r-l+1, M);
            tree.add(l,r,1, (r-l)*mi%M);
            tree.add(l,r,2, mi*x%M);
        }
        for (int i = 1; i <= n; i++) {
            long v=tree.sum(i,i);
            out.print(v+(i==n?"":" "));
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

