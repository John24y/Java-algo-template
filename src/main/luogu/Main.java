package main.luogu;

import java.io.*;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.StringTokenizer;
class LazySegTree {
    static final int OP_ADD = 1;
    static final int OP_SET = 2;

    public static class Node {
        Node left;
        Node right;
        long sum;
        long lazyVal;
        int lazyType;

        void init(int ls, int rs) {
        }

        void initForQuery(int ls, int rs) {
            sum = 0;
        }
    }

    int maxN;
    Node root;

    public LazySegTree(int maxN) {
        this.maxN = maxN;
        this.root = new Node();
        this.root.init(0, maxN);
    }

    /**
     * 1 *累加*懒标记
     * 2 维护统计信息
     * 3 @param val可能在多次懒修改中累积，并非总是调用add时的传参
     */
    void apply(Node node, int ls, int rs, int type, long val) {
        node.lazyType = type;
        if (type == OP_ADD) {
            node.lazyVal += val;
            node.sum += (rs - ls + 1) * val;
        } else if (type==OP_SET) {
            node.lazyVal = val;
            node.sum = (rs - ls + 1) * val;
        }
    }

    void reduce(Node node, Node left, Node right, int ls, int rs) {
        node.sum = left.sum + right.sum;
    }

    /**
     * O(n)设置初始值
     */
    void build(long[] vals) {
        build(root, vals, 0, maxN);
    }

    private void build(Node node, long[] vals, int ls, int rs) {
        if (ls == rs) {
            if (ls < vals.length) {
                apply(node, ls, rs, OP_ADD, vals[ls]);
            }
            return;
        }
        pushDown(node, ls, rs);
        int mid = ls + rs >> 1;
        build(node.left, vals, ls, mid);
        build(node.right, vals, mid + 1, rs);
        reduce(node, node.left, node.right, ls, rs);
    }

    public void add(int l, int r, long val) {
        add(root, l, r, OP_ADD, val, 0, maxN);
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
            node.left = new Node();
            node.left.init(ls, mid);
        }
        if (node.right == null) {
            node.right = new Node();
            node.right.init(mid+1, rs);
        }
        // 如果有多种懒操作变量，注意下传顺序，以及下传后的重置
        if (node.lazyType != 0) {
            apply(node.left, ls, mid, node.lazyType, node.lazyVal);
            apply(node.right, mid + 1, rs, node.lazyType, node.lazyVal);
            node.lazyType = 0;
            node.lazyVal = 0;
        }
    }

    public long sum(int l, int r) {
        return query(root, l, r, 0, maxN);
    }

    private long query(Node node, int l, int r, int ls, int rs) {
        if (l <= ls && rs <= r) {
            return node.sum;
        }
        pushDown(node, ls, rs);
        int mid = ls + rs >> 1;
        long res=0;
        if (l <= mid) {
            res+= query(node.left, l, r, ls, mid);
        }
        if (r >= mid + 1) {
            res+= query(node.right, l, r, mid + 1, rs);
        }
        return res;
    }

}
public class Main {

    void slv() {
        int n=nextInt(),m=nextInt();
        LazySegTree tree = new LazySegTree(n);
        long[] ar=new long[n+1];
        for (int i = 0; i < n; i++) {
            ar[i+1]=nextLong();
//            tree.add(i+1,i+1,ar[i+1]);
        }
        tree.build(ar);
        for (int i = 0; i < m; i++) {
            int op = nextInt();
            if (op==1) {
                int x=nextInt(),y=nextInt();
                tree.add(x,y,nextLong());
            } else {
                int x=nextInt(),y=nextInt();
                long sum = tree.sum(x, y);
                out.println(sum);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        new Main().slv();
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