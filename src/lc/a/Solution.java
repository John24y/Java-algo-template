package lc.a;

class SegTreeTemplate {

    static class Node {
        Node left;
        Node right;
        long lazyAdd;
        long sum;
        int ls, rs;//debug用
    }

    int maxN;
    Node root;

    public SegTreeTemplate(int maxN) {
        this.maxN = maxN;
        this.root = create(0, maxN);
    }

    Node create(int ls, int rs) {
        Node node = new Node();
        node.ls = ls;
        node.rs = rs;
        return node;
    }

    /**
     * 更新范围覆盖了整个node，要做两件事：
     * 1 更新懒标记
     * 2 维护统计信息（覆盖node时必须在更新时维护，而不是等到查询时用reduce维护，否则复杂度是O(n)了）
     */
    void apply(Node node, int ls, int rs, long val) {
        node.lazyAdd += val;
        node.sum += (rs-ls+1)*val;
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
    void build(long[] vals) {
        build(root, vals, 0, maxN);
    }

    private void build(Node node, long[] vals, int ls, int rs) {
        if (ls==rs) {
            apply(node, ls, rs, vals[ls]);
            return;
        }
        pushDown(node, ls, rs);
        int mid = ls + rs >> 1;
        build(node.left,vals, ls, mid);
        build(node.right,vals,mid + 1, rs);
        reduce(node, node.left, node.right, ls, rs);
    }

    public void add(int l, int r, long val) {
        add(root, l, r, val, 0, maxN);
    }

    /**
     * 当前Node的范围: [ls,rs]
     */
    private void add(Node node, int l, int r, long val, int ls, int rs) {
        if (l < 0 || r > maxN) {
            throw new IllegalArgumentException();
        }
        if (l <= ls && rs <= r) {
            apply(node, ls, rs, val);
            return;
        }
        pushDown(node, ls, rs);
        int mid = ls + rs >> 1;
        if (l <= mid) {
            add(node.left, l, r, val, ls, mid);
        }
        if (r >= mid + 1) {
            add(node.right, l, r, val, mid + 1, rs);
        }
        reduce(node, node.left, node.right, ls, rs);
    }

    void pushDown(Node node, int ls, int rs) {
        int mid = ls + rs >> 1;
        if (node.left == null) {
            node.left = create(ls, mid);
        }
        if (node.right == null) {
            node.right = create(mid+1, rs);
        }
        if (node.lazyAdd!=0) {
            apply(node.left, ls, mid, node.lazyAdd);
            apply(node.right, mid + 1, rs, node.lazyAdd);
            node.lazyAdd = 0;
        }
    }

    Node sumRes;
    public long sum(int l, int r) {
        sumRes=new Node();
        sum(root, l, r, 0, maxN);
        return sumRes.sum;
    }

    private void sum(Node node, int l, int r, int ls, int rs) {
        if (l < 0 || r > maxN) {
            throw new IllegalArgumentException();
        }
        if (l <= ls && rs <= r) {
            reduce(sumRes,node,EMPTY,ls,rs);
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

    public Node multiSum(int l, int r) {
        return multiSum(root, l, r, 0, maxN);
    }

    private static final Node EMPTY = new Node();
    private Node multiSum(Node node, int l, int r, int ls, int rs) {
        if (l < 0 || r > maxN) {
            throw new IllegalArgumentException();
        }
        if (l <= ls && rs <= r) {
            return node;
        }
        pushDown(node, ls, rs);
        int mid = ls + rs >> 1;
        Node res = new Node(), leftRes = EMPTY, rightRes = EMPTY;
        if (l <= mid) {
            leftRes = multiSum(node.left, l, r, ls, mid);
        }
        if (r >= mid + 1) {
            rightRes = multiSum(node.right, l, r, mid + 1, rs);
        }
        reduce(res, leftRes, rightRes, ls, rs);
        return res;
    }

}
class Solution {
    public int[] fullBloomFlowers(int[][] flowers, int[] persons) {
        SegTreeTemplate tree=new SegTreeTemplate((int) 1e9);
        for (int[] a : flowers) {
            tree.add(a[0],a[1],1);
        }
        int[] ans=new int[persons.length];
        for (int i = 0; i < persons.length; i++) {
            ans[i]= (int) tree.sum(persons[i],persons[i]);
        }
        return ans;
    }

    public static void main(String[] args) {
//        int score = new Solution().maximumScore(Arrays.asList(8, 3, 9, 3, 8), 2);
//        System.out.println(score);

//        String input = scanner.nextLine();
//        long r = new A().new Solution().getSchemeCount(3, 3, LCUtils.parseStringAr("[\"..R\",\"..B\",\"?R?\"]"));
//        System.out.println(r);
    }
}

