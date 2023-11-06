package main.at;

import java.io.*;
import java.util.*;

class MaxValSegTree {
    //对查询结果相加需要注意溢出
    static final long INIT = 0;
    static final int OP_ADD = 1;
    static final int OP_SET = 2;

    static class Node {
        Node left;
        Node right;
        int ls, rs;//debug用

        long maxVal;
        int maxId;//值最大且index最小的index
        int lazyType;
        long lazyVal;
        long sum;
    }

    int maxN;
    Node root;

    public MaxValSegTree(int maxN) {
        this.maxN = maxN;
        this.root = createNode(0, maxN);
        root.ls = 0;
        root.rs = maxN;
    }

    Node createNode(int ls, int rs) {
        Node r = new Node();
        r.maxId = ls;
        r.maxVal = INIT;
        return r;
    }

    void apply(Node node, int ls, int rs, int type, long val) {
        node.lazyType = type;
        if (type == OP_ADD) {
            node.lazyVal += val;
            node.sum += (rs - ls + 1) * val;
            node.maxVal += val;
        } else if (type==OP_SET) {
            node.lazyVal = val;
            node.sum = (rs - ls + 1) * val;
            node.maxVal = val;
        }
    }

    void reduce(Node node, Node left, Node right, int ls, int rs) {
        node.sum = left.sum + right.sum;
        if (right.maxVal > left.maxVal) {
            node.maxId = right.maxId;
            node.maxVal = right.maxVal;
        } else {
            node.maxId = left.maxId;
            node.maxVal = left.maxVal;
        }
    }

    void build(long[] vals) {
        build(root, vals, 0, maxN);
    }

    private void build(Node node, long[] vals, int ls, int rs) {
        if (ls==rs) {
            apply(node, ls, rs, OP_SET, vals[ls]);
            return;
        }
        pushDown(node, ls, rs);
        int mid = ls + rs >> 1;
        build(node.left,vals, ls, mid);
        build(node.right,vals,mid + 1, rs);
        reduce(node, node.left, node.right, ls, rs);
    }

    public void add(int l, int r, long val) {
        update(root, l, r, 0, maxN, OP_ADD, val);
    }

    public void set(int l, int r, long val) {
        update(root, l, r, 0, maxN, OP_SET, val);
    }

    /**
     * 当前Node的范围: [ls,rs]
     */
    private void update(Node node, int l, int r, int ls, int rs, int type, long val) {
        if (l < 0 || r > maxN) {
            throw new IllegalArgumentException();
        }
        if (l <= ls && rs <= r) {
            apply(node, ls, rs, type, val);
            return;
        }

        pushDown(node, ls, rs);
        int mid = ls + rs >> 1;
        //左子树[ls,mid]
        //右子树[mid+1,rs]
        if (l <= mid) {
            update(node.left, l, r, ls, mid, type, val);
        }
        if (r >= mid + 1) {
            update(node.right, l, r, mid + 1, rs, type, val);
        }
        reduce(node, node.left, node.right, ls, rs);
    }

    //下发lazy值
    void pushDown(Node node, int ls, int rs) {
        int mid = ls + rs >> 1;
        if (node.left == null) {
            node.left = createNode(ls, mid);
            node.left.ls = ls;
            node.left.rs = mid;
        }
        if (node.right == null) {
            node.right = createNode(mid + 1, rs);
            node.right.ls = mid + 1;
            node.right.rs = rs;
        }
        if (node.lazyType != 0) {
            apply(node.left, ls, mid, node.lazyType, node.lazyVal);
            apply(node.right, mid + 1, rs, node.lazyType, node.lazyVal);
            node.lazyType = 0;
            node.lazyVal = 0;
        }
    }

    public long sum(int l, int r) {
        queryNode.sum = 0;
        query(root, l, r, 0, maxN);
        return queryNode.sum;
    }

    //查询最大值
    public long queryMaxVal(int l, int r) {
        queryNode.maxVal = INIT;
        queryNode.maxId = -1;
        query(root, l, r, 0, maxN);
        return queryNode.maxVal;
    }

    //查询最大值的下标，如果有多个则取下标最小的
    public int queryMaxId(int l, int r) {
        queryNode.maxVal = INIT;
        queryNode.maxId = -1;
        query(root, l, r, 0, maxN);
        return queryNode.maxId;
    }

    private static final Node EMPTY = new Node();
    private final Node queryNode = new Node();

    private void query(Node node, int l, int r, int ls, int rs) {
        if (l < 0 || r > maxN) {
            throw new IllegalArgumentException();
        }
        if (l <= ls && rs <= r) {
            reduce(queryNode, node, queryNode, ls, rs);
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

    public void solve() throws Exception {
        MaxValSegTree tree = new MaxValSegTree((int) 2e5+10);
        int n=nextInt(),d=nextInt(),w=nextInt();
        List<List<Integer>> xs = new ArrayList<>();
        for (int i = 0; i < 2e5 + 10; i++) {
            xs.add(new ArrayList<>());
        }
        for (int i = 0; i < n; i++) {
            int t=nextInt(),x=nextInt();
            xs.get(x).add(t);
        }
        int r=0;
        long ans=0;
        for (int l = 0; l < xs.size(); l++) {
            while (r<xs.size()&&r<l+w){
                for (Integer t : xs.get(r)) {
                    tree.add(Math.max(0,t-d+1),t,1);
                }
                r++;
            }
            ans=Math.max(ans, tree.root.maxVal);
            for (Integer t : xs.get(l)) {
                tree.add(Math.max(0,t-d+1),t,-1);
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

