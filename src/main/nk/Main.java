package main.nk;

import java.io.*;
import java.util.*;

class SimpleSegTree {

    static class Node {
        Node left;
        Node right;
        long sum;
    }

    int maxN;
    Node root;

    public SimpleSegTree(int maxN) {
        this.maxN = maxN;
        this.root = new Node();
    }

    long trim(long v) {
        //可以加上取余
        return v;
    }

    public void add(int i, long val) {
        add(root, i, val, 0, maxN);
    }

    /**
     * 当前Node的范围: [ls,rs]
     */
    private void add(Node node, int i, long val, int ls, int rs) {
        if (ls==rs) {
            node.sum = trim(node.sum+val);
            return;
        }

        pushDown(node, ls, rs);
        int mid = ls + rs >> 1;
        //左子树[ls,mid]
        //右子树[mid+1,rs]
        if (i <= mid) {
            add(node.left, i, val, ls, mid);
        } else if (i >= mid + 1) {
            add(node.right, i, val, mid + 1, rs);
        }
        node.sum = trim(node.sum + val);
    }

    void pushDown(Node node, int ls, int rs) {
        int mid = ls + rs >> 1;
        if (node.left == null) {
            node.left = new Node();
        }
        if (node.right == null) {
            node.right = new Node();
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
}
class RankFenwickSegTree {
    SimpleSegTree[] trees;
    int maxX, maxY;

    class VirtualNode {
        List<SimpleSegTree.Node> listPos=new ArrayList<>();
        List<SimpleSegTree.Node> listNeg=new ArrayList<>();
        long sum() {
            long res=0;
            for (SimpleSegTree.Node node : listPos) res+=node.sum;
            for (SimpleSegTree.Node node : listNeg) res-=node.sum;
            return res;
        }
        void left() {
            for (int k=0;k<2;k++){
                List<SimpleSegTree.Node> list = k==0?listPos:listNeg;
                int j=0;
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).left!=null) {
                        list.set(j, list.get(i).left);
                        j++;
                    }
                }
                while (list.size()>j) list.remove(list.size()-1);
            }
        }
        void right() {
            for (int k=0;k<2;k++){
                List<SimpleSegTree.Node> list = k==0?listPos:listNeg;
                int j=0;
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).right!=null) {
                        list.set(j, list.get(i).right);
                        j++;
                    }
                }
                while (list.size()>j) list.remove(list.size()-1);
            }
        }
    }

    public RankFenwickSegTree(int maxX, int maxY) {
        trees=new SimpleSegTree[maxX+2];
        this.maxX=maxX;
        this.maxY=maxY;
        for (int i = 1; i < trees.length; i++) {
            trees[i] = new SimpleSegTree(maxY);
        }
    }

    public void add(int x, int y, int val) {
        for(x++;x<trees.length;x+=x&-x) {
            trees[x].add(y, val);
        }
    }

    private VirtualNode virtualNode(int xL, int xR) {
        VirtualNode node = new VirtualNode();
        for (; xL > 0; xL-=xL&-xL) {
            node.listNeg.add(trees[xL].root);
        }
        for (xR++; xR > 0; xR-=xR&-xR) {
            node.listPos.add(trees[xR].root);
        }
        return node;
    }

    //[xL,xR]组成线段树上的第k小, 不存在返回-1
    public int rank(int xL, int xR, int k) {
        VirtualNode node = virtualNode(xL, xR);
        return rank(node, 0, maxY, k);
    }

    private int rank(VirtualNode node, int ls, int rs, int k) {
        if (node.sum() < k) {
            return -1;
        }
        if (ls == rs) {
            return ls;
        }
        int mid = ls + rs >> 1;
        long leftSum=0;
        for (SimpleSegTree.Node n : node.listPos) {
            if (n.left!=null)leftSum+=n.left.sum;
        }
        for (SimpleSegTree.Node n : node.listNeg) {
            if (n.left!=null)leftSum-=n.left.sum;
        }
        if (leftSum >= k) {
            node.left();
            return rank(node, ls, mid, k);
        } else {
            node.right();
            return rank(node, mid + 1, rs, (int) (k - leftSum));
        }
    }
}

public class Main {

    public void solve() throws Exception {
        int n=readInt(),m=readInt();
        int[] a=new int[n+1];
        RankFenwickSegTree tree = new RankFenwickSegTree(n, (int) 1e9);
        for (int i = 1; i <= n; i++) {
            a[i]=readInt();
            tree.add(i,a[i],1);
        }
        for (int i = 0; i < m; i++) {
            String op=next();
            if (op.equals("Q")) {
                int l=readInt(),r=readInt(),k=readInt();
                out.println(tree.rank(l, r, k));
            } else {
                int x=readInt(),y=readInt();
                tree.add(x,a[x],-1);
                a[x]=y;
                tree.add(x,a[x],1);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        if (true) {
            new Main().solve();
        } else {
            int t = readInt();
            for (int i = 0; i < t; i++) {
                new Main().solve();
            }
        }
        out.flush();
    }

    static PrintWriter out = new PrintWriter(System.out, false);
    static InputReader in = new InputReader(System.in);
    static String next() { return in.next(); }
    static int readInt() { return Integer.parseInt(in.next()); }
    static long readLong() { return Long.parseLong(in.next()); }
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

