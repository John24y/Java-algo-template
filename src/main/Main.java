package main;
import java.io.*;
import java.util.*;

class TopKSumTree {

    static class Node {
        Node left;
        Node right;
        long sum;
        long cnt;
        int ls, rs;//debug用
    }

    int maxN;
    Node root;

    public TopKSumTree(int maxN) {
        this.maxN = maxN;
        this.root = new Node();
        root.ls = 0;
        root.rs = maxN;
    }

    long trim(long v) {
        //可以加上取余
        return v;
    }

    public void add(int i, long val, long cnt) {
        add(root, i, val, cnt, 0, maxN);
    }

    /**
     * 当前Node的范围: [ls,rs]
     */
    private void add(Node node, int i, long val, long cnt, int ls, int rs) {
        if (i<0 || i > maxN){
            throw new IllegalArgumentException("index:" + i);
        }
        if (rs == ls && ls == i) {
            node.sum = trim(node.sum + val);
            node.cnt = trim(node.cnt + cnt);
            return;
        }

        pushDown(node, ls, rs);
        int mid = ls + rs >> 1;
        //左子树[ls,mid]
        //右子树[mid+1,rs]
        if (i <= mid) {
            add(node.left, i, val, cnt, ls, mid);
        }
        if (i >= mid + 1) {
            add(node.right, i, val, cnt,mid + 1, rs);
        }
        node.sum = trim(node.sum + val);
        node.cnt = trim(node.cnt + cnt);
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
        long res = 0;
        if (l <= mid) {
            res += sum(node.left, l, r, ls, mid);
        }
        if (r >= mid + 1) {
            res += sum(node.right, l, r, mid + 1, rs);
        }
        return trim(res);
    }

    //从最大下标开始向左找到cnt个值并返回sum
    public long sumLeftwardWithCnt(Node node, long cnt) {
        if (cnt==0) return 0;
        int ls=node.ls, rs=node.rs;
        if (ls==rs && node.cnt>cnt) {
            return node.sum / node.cnt * cnt;
        }
        if (node.cnt<=cnt) {
            return node.sum;
        }
        pushDown(node, ls, rs);
        long res=0;
        if (node.right.cnt < cnt) {
            res = trim(res + node.right.sum + sumLeftwardWithCnt(node.left, cnt - node.right.cnt));
        } else {
            res = sumLeftwardWithCnt(node.right, cnt);
        }
        return res;
    }
}


public class Main {
    public void solve() throws Exception {
        int n = nextInt(),k=nextInt(),q=nextInt();
        int MX = (int) 1e9;
        TopKSumTree tree = new TopKSumTree((int) 1e9);
        int[] ar=new int[n];
        Arrays.fill(ar,-1);
        for (int i = 0; i < q; i++) {
            int j=nextInt()-1,y=nextInt();
            int old = ar[j];
            ar[j] = y;
            if (old>-1) {
                tree.add(old,-old,-1);
            }
            tree.add(y,y,1);
            out.println(tree.sumLeftwardWithCnt(tree.root, k));
        }
    }

    public static void main(String[] args) throws Exception {
        new Main().solve();
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

