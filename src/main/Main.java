package main;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

class RankDiffSegTree {

    class Node {
        Node left;
        Node right;
        long sum;

        public Node copy() {
            Node node = new Node();
            node.left = left;
            node.right = right;
            node.sum = sum;
            return node;
        }
    }

    int maxN;
    Node root;

    public RankDiffSegTree(int maxN) {
        this.maxN = maxN;
        this.root = new Node();
    }

    /**
     * 在preNode作为root的基础上更新
     */
    public Node addCount(Node preNode, int i, long val) {
        return add(preNode.copy(), i, val, 0, maxN);
    }

    /**
     * 当前Node的范围: [ls,rs]
     */
    private Node add(Node node, int i, long val, int ls, int rs) {
        if (i < 0 || i > maxN) {
            throw new IllegalArgumentException();
        }
        if (ls == rs) {
            assert ls==i;
            node.sum += val;
            return node;
        }

        pushDown(node, ls, rs);
        int mid = ls + rs >> 1;
        //左子树[ls,mid]
        //右子树[mid+1,rs]
        if (i <= mid) {
            node.left = node.left.copy();
            add(node.left, i, val, ls, mid);
        } else {
            node.right = node.right.copy();
            add(node.right, i, val, mid + 1, rs);
        }
        node.sum += val;
        return node;
    }

    void pushDown(Node node, int ls, int rs) {
        if (node.left == null) {
            node.left = new Node();
        }
        if (node.right == null) {
            node.right = new Node();
        }
    }

    /**
     * 假设tree[i]表示i在数组中出现的次数，求数组的第k小，如果不存在返回-1
     */
    public long kSmallest(Node oldRoot, Node newRoot, long k) {
        return queryFirstGE(oldRoot, newRoot, 0, maxN, k);
    }

    /**
     * 查找使sum([0,r])>=v最小的r，如果不存在返回-1
     */
    private long queryFirstGE(Node oldRoot, Node newRoot, int ls, int rs, long v) {
        long sum = newRoot.sum - oldRoot.sum;
        if (sum < v) {
            return -1;
        }
        if (ls == rs) {
            return ls;
        }
        int mid = ls + rs >> 1;
        pushDown(oldRoot, ls, rs);
        pushDown(newRoot, ls, rs);
        long leftSum = newRoot.left.sum - oldRoot.left.sum;
        if (leftSum >= v) {
            return queryFirstGE(oldRoot.left, newRoot.left, ls, mid, v);
        } else {
            return queryFirstGE(oldRoot.right, newRoot.right, mid + 1, rs, v - leftSum);
        }
    }
}

public class Main {

    public void solve() throws Exception {
        int n=nextInt(),m=nextInt();
        int[] ar=new int[n+1];
        Set<Integer> set=new HashSet<>();
        for (int i = 1; i <= n; i++) {
            ar[i]=nextInt();
            set.add(ar[i]);
        }
        List<Integer> list = new ArrayList<>(set);
        Collections.sort(list);
        Map<Integer,Integer> map=new HashMap<>(list.size());
        for (int i = 0; i < list.size(); i++) {
            map.put(list.get(i),i+1);
        }
        RankDiffSegTree tree=new RankDiffSegTree(list.size()+1);
        RankDiffSegTree.Node[] roots=new RankDiffSegTree.Node[n+1];
        roots[0]=tree.root;
        for (int i = 1; i <= n; i++) {
            roots[i]=tree.addCount(roots[i-1], map.get(ar[i]),1);
        }
        for (int i = 0; i < m; i++) {
            int l=nextInt(),r=nextInt(),k=nextInt();
            out.println(list.get((int) tree.kSmallest(roots[l-1],roots[r],k)-1));
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

