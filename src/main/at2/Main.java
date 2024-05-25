package main.at2;

import java.io.*;
import java.util.*;
class SimpleSegTree {
    int M = 998244353;

    static class Node {
        Node left;
        Node right;
        long sum;
        long mul;
        long ls, rs;
    }

    int maxN;
    Node root;

    public SimpleSegTree(int maxN) {
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

    public void apply(Node node, int type, long val, int ls, int rs) {
        node.sum += val;
        node.sum %= M;
        node.mul = node.sum;
    }

    public void reduce(Node node, Node left, Node right, int ls, int rs) {
        node.sum = (left.sum + right.sum)%M;
        node.mul = (left.mul * right.sum)%M;
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

    Node query(Node node, int l, int r, int ls, int rs) {
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
            leftRes = query(node.left, l, r, ls, mid);
        }
        if (r >= mid + 1) {
            rightRes = query(node.right, l, r, mid + 1, rs);
        }
        if (leftRes == null) return rightRes;
        if (rightRes == null) return leftRes;
        reduce(res, leftRes, rightRes, ls, rs);
        return res;
    }
}
class FenwickTree {
    //f[i]表示 (i-(i&-i),i] 区间的和
    long[] f;

    //可用下标为 [0,maxN]
    public FenwickTree(int maxN) {
        f=new long[maxN+2];
    }

    //可以加上取余
    long trim(long x) {
        return x;
    }

    //下标i增加v，下标从0开始
    public void add(int i, long v) {
        for(i++;i<f.length;i+=i&-i) {
            f[i]+=v;
        }
    }

    //查询闭区间[0,r]的和
    public long query(int i) {
        long sum=0;
        for (i++; i > 0; i-=i&-i) {
            sum=trim(sum+f[i]);
        }
        return sum;
    }

    //查询闭区间[l,r]的和
    public long query(int l, int r) {
        return query(r)-query(l-1);
    }

}

public class Main implements Runnable{
    public void solve() {
        int n=nextInt(),h=nextInt();
        int[] x=new int[n];
        List<int[]> sl = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            x[i]=nextInt();
            sl.add(new int[]{x[i],i});
        }
        sl.sort(new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
                return Integer.compare(o1[0], o2[0]);
            }
        });
        int[][] segi = new int[n][2];
        List<List<Integer>> segs = new ArrayList<>();
        for (int i = 0; i < n;) {
            int j=i+1;
            List<Integer> seg = new ArrayList<>();
            seg.add(sl.get(i)[1]);
            while (j<n && sl.get(j)[0]-sl.get(i)[0]<=h) {
                seg.add(sl.get(j)[1]);
                j++;
            }
            i=j;
            for (int k = 0; k < seg.size(); k++) {
                segi[seg.get(k)][0]=segs.size();
                segi[seg.get(k)][1]=k;
            }
            segs.add(seg);
        }
        List<int[]> segPre = new ArrayList<>();
        List<int[]> segSuf = new ArrayList<>();
        int m = segs.size();
        for (int i = 0; i < m; i++) {
            List<Integer> list = segs.get(i);
            int[] pre = new int[list.size()];
            int[] suf = new int[list.size()];
            FenwickTree tree = new FenwickTree(n);
            for (int j = 0; j < list.size(); j++) {
                pre[j] = (int) tree.query(0, list.get(j));
                tree.add(list.get(j), 1);
            }
            tree = new FenwickTree(n);
            for (int j = list.size() - 1; j >= 0; j--) {
                suf[j] = (int) tree.query(0, list.get(j));
                tree.add(list.get(j), 1);
            }
            segPre.add(pre);
            segSuf.add(suf);
        }
        SimpleSegTree tree = new SimpleSegTree(m);
        for (int i = 0; i < n; i++) {
            int[] pre = segPre.get(segi[i][0]);
            int[] suf = segSuf.get(segi[i][0]);

        }
    }


    public static void main(String[] args) throws Exception {
        new Thread(null, new Main(), "CustomThread", 1024 * 1024 * 100).start();
    }

    @Override
    public void run() {
        int t = 1;
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