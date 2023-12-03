package main.at;

import java.io.*;
import java.util.StringTokenizer;


class SingleApplySegTree {

    static class Node {
        Node left;
        Node right;
        long sum;
        long ls, rs;
    }

    int maxN;
    Node root;

    public SingleApplySegTree(int maxN) {
        this.maxN = maxN;
        this.root = createNode(0, maxN);
    }

    Node createNode(int ls, int rs) {
        Node node = new Node();
        node.ls = ls;
        node.rs = rs;
        return node;
    }

    public void build(int[] vals) {
        build(vals, root, 0, maxN);
    }

    private void build(int[] vals, Node node, int ls, int rs) {
        if (ls == rs) {
            if (ls >= vals.length) return;
            apply(node, 0, vals[ls], ls, rs);
            return;
        }
        pushDown(node, ls, rs);
        int mid = ls + rs >> 1;
        build(vals, node.left, ls, mid);
        build(vals, node.right, mid + 1, rs);
        reduce(node, node.left, node.right, ls, rs);
    }

    /**
     * 修改单点
     */
    public void apply(Node node, int type, long val, int ls, int rs) {
        node.sum += val;
    }

    /**
     * 两个子区间统计信息进行合并，子区间可以是查询时动态构造的，而不一定是某个node的范围
     */
    public void reduce(Node node, Node left, Node right, int ls, int rs) {
        node.sum = left.sum + right.sum;
    }

    public void add(int i, long val) {
        add(root, i, 0, val, 0, maxN);
    }

    /**
     * 当前Node的范围: [ls,rs]
     */
    private void add(Node node, int i, int type, long val, int ls, int rs) {
        if (ls == rs) {
            apply(node, type, val, ls, rs);
            return;
        }

        pushDown(node, ls, rs);
        int mid = ls + rs >> 1;
        //左子树[ls,mid]
        //右子树[mid+1,rs]
        if (i <= mid) {
            add(node.left, i, type, val, ls, mid);
        } else if (i >= mid + 1) {
            add(node.right, i, type, val, mid + 1, rs);
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
    }

    private final Node sumResult = new Node();

    public long sum(int l, int r) {
        sumResult.sum = 0;
        sum(root, l, r, 0, maxN);
        return sumResult.sum;
    }

    private void sum(Node node, int l, int r, int ls, int rs) {
        if (l < 0 || r > maxN || r < 0) throw new IllegalArgumentException("index:" + l + "," + r);
        if (l <= ls && rs <= r) {
            reduce(sumResult, sumResult, node, ls, rs);
            return;
        }
        pushDown(node, ls, rs);
        int mid = ls + rs >> 1;
        if (l <= mid) {
            sum(node.left, l, r, ls, mid);
        }
        if (r >= mid + 1) {
            sum(node.right, l, r, mid + 1, rs);
        }
    }

    Node queryStrictMerge(Node node, int l, int r, int ls, int rs) {
        if (l < 0 || r > maxN) {
            throw new IllegalArgumentException();
        }
        if (l <= ls && rs <= r) {
            return node;
        }
        pushDown(node, ls, rs);
        int mid = ls + rs >> 1;
        Node res = createNode(Math.max(ls, l), Math.min(rs, r)), leftRes = null, rightRes = null;
        if (l <= mid) {
            leftRes = queryStrictMerge(node.left, l, r, ls, mid);
        }
        if (r >= mid + 1) {
            rightRes = queryStrictMerge(node.right, l, r, mid + 1, rs);
        }
        if (leftRes==null) return rightRes;
        if (rightRes==null) return leftRes;
        reduce(res, leftRes, rightRes, ls, rs);
        return res;
    }
}
class StringHashSegTree extends SingleApplySegTree {
    static int mod = (int) (1e9 + 7);
    static int fact1 = 171;
    static int fact2 = 13;
    static int N = (int) 1e6 + 1;
    static long[] p1 = new long[N];
    static long[] p2 = new long[N];

    static {
        p1[0] = 1;
        p2[0] = 1;
        for (int i = 1; i < N; i++) {
            p1[i] = p1[i - 1] * fact1 % mod;
            p2[i] = p2[i - 1] * fact2 % mod;
        }
    }

    static class Node extends SingleApplySegTree.Node {
        long hash1, hash2;
        long reverseHash1, reverseHash2;
        Node getLeft() {
            return (Node) left;
        }
        Node getRight() {
            return (Node) right;
        }
    }

    public StringHashSegTree(int maxN) {
        super(maxN);
    }

    @Override
    Node createNode(int ls, int rs) {
        Node node = new Node();
        node.ls = ls;
        node.rs = rs;
        return node;
    }

    public void build(String s) {
        build(s, (Node) root, 0, maxN);
    }

    private void build(String s, Node node, int ls, int rs) {
        if (ls == rs) {
            if (ls >= s.length()) return;
            apply(node, s.charAt(ls), ls, rs);
            return;
        }
        pushDown(node, ls, rs);
        int mid = ls + rs >> 1;
        build(s, node.getLeft(), ls, mid);
        build(s, node.getRight(), mid + 1, rs);
        reduce(node, node.left, node.right, ls, rs);
    }

    @Override
    public void apply(SingleApplySegTree.Node node, int type, long val, int ls, int rs) {
        apply((Node) node, val, ls, rs);
    }

    @Override
    public void reduce(SingleApplySegTree.Node node, SingleApplySegTree.Node left, SingleApplySegTree.Node right,
                       int ls, int rs) {
        reduce((Node) node, (Node) left,(Node) right, ls, rs);
    }

    public void apply(Node node, long val, int ls, int rs) {
        node.hash1 = node.hash2 = val;
        node.reverseHash1 = node.reverseHash2 = val;
    }

    public void reduce(Node node, Node left, Node right, int ls, int rs) {
        node.hash1 = (left.hash1 * p1[(int) (right.rs - right.ls + 1)] + right.hash1) % mod;
        node.hash2 = (left.hash2 * p2[(int) (right.rs - right.ls + 1)] + right.hash2) % mod;
        node.reverseHash1 = (right.reverseHash1 * p1[(int) (left.rs - left.ls + 1)] + left.reverseHash1) % mod;
        node.reverseHash2 = (right.reverseHash2 * p2[(int) (left.rs - left.ls + 1)] + left.reverseHash2) % mod;
    }

    public Node hash(int l, int r) {
        return (Node) queryStrictMerge(root, l, r, 0, maxN);
    }
}

public class Main {
    public void solve() throws Exception {
        int n=nextInt(),q=nextInt();
        String s = next();
        StringHashSegTree tree = new StringHashSegTree(n);
        tree.build(s);
        for (int i = 0; i < q; i++) {
            int op=nextInt();
            if (op==1){
                int x = nextInt() - 1;
                char c = next().charAt(0);
                tree.add(x,c);
            } else {
                int L=nextInt()-1,R=nextInt()-1;
                StringHashSegTree.Node sum1 = tree.hash(L, (L+R)/2);
                StringHashSegTree.Node sum2 = tree.hash((L+R+1)/2,R);
                out.println(sum1.hash1==sum2.reverseHash1&&sum1.hash2==sum2.reverseHash2?"Yes":"No");
            }
        }
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

