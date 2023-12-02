package main.nk;

import java.io.*;
import java.util.*;

class ArraySparseIndex {
    Set<Long> set = new HashSet<>();
    long[] ar;
    public void add(long num) {
        set.add(num);
    }
    public void generateIndex() {
        ar=new long[set.size()];
        set.add(Long.MIN_VALUE);
        set.add(Long.MAX_VALUE);
        ar=new long[set.size()];
        int i=0;
        for (Long l : set) {
            ar[i++]=l;
        }
        Arrays.sort(ar);
        set = null;
    }
    public int ge(long num) {
        return bisect(num, true);
    }
    public int le(long num) {
        return bisect(num, true)-1;
    }
    public int ge(double val) {
        return ge((long)Math.ceil(val));
    }
    public int le(double val) {
        return le((long)val);
    }
    public long revertIndex(int index) {
        return ar[index];
    }
    public int bisect(long val, boolean bisectLeft) {
        int low=0,high= ar.length-1;
        while (low<=high){
            int mid=low+high>>1;
            boolean pred = bisectLeft ? ar[mid]>=val : ar[mid] > val;
            if (pred) {
                high=mid-1;
            } else {
                low=mid+1;
            }
        }
        return low;
    }
}
class BinaryLiftingLCA {
    List<List<int[]>> graph;
    int[][] up;//up[b][i] 节点i向上走2^b步
    int[] depth;
    int MB = 30;

    BinaryLiftingLCA(List<List<int[]>> graph, int root) {
        this.graph = graph;
        int n = graph.size();
        up = new int[MB + 1][n];
        depth = new int[n];
        dfs(root, -1);
    }

    void dfs(int u, int p) {
        up[0][u] = p;
        depth[u] = p == -1 ? 0 : depth[p] + 1;
        for (int b = 1; b <= MB; b++) {
            int ancestor = up[b - 1][u];
            if (ancestor == -1) {
                up[b][u] = -1;
            } else {
                up[b][u] = up[b - 1][ancestor];
            }
        }
        for (int[] ch : graph.get(u)) {
            if (ch[0] == p) continue;
            dfs(ch[0], u);
        }
    }

    int levelUp(int u, int lv) {
        for (int i = MB; i >= 0; i--) {
            if ((lv & (1 << i)) > 0) {
                u = up[i][u];
            }
        }
        return u;
    }

    int lca(int u, int v) {
        if (depth[u] < depth[v]) {
            return lca(v, u);
        }
        u = levelUp(u, depth[u] - depth[v]);
        if (u == v) return u;
        for (int i = MB; i >= 0; i--) {
            if (up[i][u] == up[i][v]) continue;
            u = up[i][u];
            v = up[i][v];
        }
        return up[0][u];
    }
}
class RankPrefixSegTree {

    static class Node {
        Node left;
        Node right;
        int sum;

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

    public RankPrefixSegTree(int maxN) {
        this.maxN = maxN;
        this.root = new Node();
    }

    /**
     * 在preNode作为root的基础上更新
     */
    public Node addCount(Node preNode, int i, int val) {
        return add(preNode.copy(), i, val, 0, maxN);
    }

    /**
     * 当前Node的范围: [ls,rs]
     */
    private Node add(Node node, int i, int val, int ls, int rs) {
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

    void pushDown(Node node, long ls, long rs) {
        if (node.left == null) {
            node.left = new Node();
        }
        if (node.right == null) {
            node.right = new Node();
        }
    }

    public long sum(Node oldRoot, Node newRoot, int l, int r) {
        return sum(oldRoot, newRoot, l, r, 0, maxN);
    }

    private long sum(Node oldRoot, Node newRoot, int l, int r, int ls, int rs) {
        if (l < 0 || r > maxN) {
            throw new IllegalArgumentException();
        }
        if (l <= ls && rs <= r) {
            return newRoot.sum - oldRoot.sum;
        }
        pushDown(oldRoot, ls, rs);
        pushDown(newRoot, ls, rs);
        int mid = ls + rs >> 1;
        long res = 0;
        if (l <= mid) {
            res += sum(oldRoot.left, newRoot.left, l, r, ls, mid);
        }
        if (r >= mid + 1) {
            res += sum(oldRoot.right, newRoot.right, l, r, mid + 1, rs);
        }
        return res;
    }

    /**
     * 假设tree[i]表示i在数组中出现的次数，求数组的第k小，如果不存在返回-1
     */
    public long kSmallest(Node oldRoot, Node newRoot, long k) {
        return queryFirstSumGE(oldRoot, newRoot, 0, maxN, k);
    }

    /**
     * 查找使sum([0,r])>=v最小的r，如果不存在返回-1
     */
    private long queryFirstSumGE(Node oldRoot, Node newRoot, int ls, int rs, long v) {
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
            return queryFirstSumGE(oldRoot.left, newRoot.left, ls, mid, v);
        } else {
            return queryFirstSumGE(oldRoot.right, newRoot.right, mid + 1, rs, v - leftSum);
        }
    }

    public long leftwardFirst(Node oldRoot, Node newRoot, int start) {
        return queryFirst(oldRoot, newRoot, 0, maxN, start, true);
    }

    public long rightwardFirst(Node oldRoot, Node newRoot, int start) {
        return queryFirst(oldRoot, newRoot, 0, maxN, start, false);
    }

    private long queryFirst(Node oldRoot, Node newRoot, int ls, int rs, int start, boolean leftward) {
        long sum = newRoot.sum - oldRoot.sum;
        if (sum<=0)  return -1;
        if (ls==rs)  return ls;
        int mid = ls + rs >> 1;
        pushDown(oldRoot, ls, rs);
        pushDown(newRoot, ls, rs);
        long leftSum = newRoot.left.sum - oldRoot.left.sum;
        long rightSum = newRoot.right.sum - oldRoot.right.sum;
        if (leftward) {
            if (rightSum>0&&start>=mid+1) {
                long r = queryFirst(oldRoot.right,newRoot.right,mid+1,rs,start,leftward);
                if (r!=-1) return r;
            }
            return queryFirst(oldRoot.left,newRoot.left,ls,mid,start,leftward);
        } else {
            if (leftSum>0&&start<=mid) {
                long r = queryFirst(oldRoot.left,newRoot.left,ls,mid,start,leftward);
                if (r!=-1) return r;
            }
            return queryFirst(oldRoot.right,newRoot.right,mid+1,rs,start,leftward);
        }
    }

}
public class Main {
    List<List<int[]>> g=new ArrayList<>();
    int n,m;
    RankPrefixSegTree tree;
    long[] presum;
    RankPrefixSegTree.Node[] nodes;
    int[] pa;
    ArraySparseIndex index = new ArraySparseIndex();
    public void solve() throws Exception {
        n=nextInt();
        m=nextInt();
        for (int i = 0; i <= n; i++) {
            g.add(new ArrayList<>());
        }
        presum = new long[n+1];
        nodes = new RankPrefixSegTree.Node[n+1];
        pa=new int[n+1];
        for (int i = 0; i < n-1; i++) {
            int u=nextInt(),v=nextInt(),w=nextInt();
            g.get(u).add(new int[]{v,w});
            g.get(v).add(new int[]{u,w});
        }
        build(1,0);
        index.generateIndex();
        tree=new RankPrefixSegTree(index.ar.length);
        nodes[0]=tree.root;
        build2(1,0);

        BinaryLiftingLCA blca = new BinaryLiftingLCA(g,1);
        for (int i = 0; i < m; i++) {
            int x=nextInt(),y=nextInt();
            int lca = blca.lca(x, y);
            //求从x到y的路径和，因为权值是在边上而不是点上，所以减去 lca.presum
            long tot = presum[x]+presum[y]-presum[lca]-presum[lca];
            long res=Long.MAX_VALUE;
            for (int j = 0; j < 2; j++) {
                int end = j==0?x:y;
                //这里求前缀求差做为线段树，为了使线段树包含lca节点，需要减去lca.parent
                long r = tree.leftwardFirst(nodes[pa[lca]], nodes[end], index.le(presum[end] - (tot / 2)));
                if (r>-1) {
                    long rr=(presum[end]-index.revertIndex((int) r));
                    res=Math.min(res,Math.abs(tot-rr-rr));
                }
                r = tree.rightwardFirst(nodes[pa[lca]], nodes[end], index.ge(presum[end] - ((tot+1) / 2)));
                if (r>-1) {
                    long rr=(presum[end]-index.revertIndex((int) r));
                    res=Math.min(res,Math.abs(tot-rr-rr));
                }
            }
            out.println(res);
        }
    }

    private void build(int i, int p) {
        index.add(presum[i]);
        pa[i]=p;
        for (int[] ints : g.get(i)) {
            int ch=ints[0];
            int w=ints[1];
            if (ch==p)continue;
            presum[ch]=presum[i]+w;
            build(ch,i);
        }
   }

   private void build2(int i, int p) {
       nodes[i]=tree.addCount(nodes[p],index.ge(presum[i]),1);
       for (int[] ints : g.get(i)) {
           int ch=ints[0];
           if (ch==p)continue;
           build2(ch,i);
       }
   }

    public static void main(String[] args) throws Exception {
        int t=1;
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

