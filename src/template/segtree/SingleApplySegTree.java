package template.segtree;


/**
 * 简化版线段树，只做单点更新操作。
 * <p>
 * 对比FenwickTree：FenwickTree在查询时只支持前缀查询，实现最大值最小值查询很麻烦。而线段树可以任意区间查询。
 */
class SingleApplySegTree {

    static class Node {
        Node left;
        Node right;
        long sum;
    }

    int maxN;
    Node root;

    public SingleApplySegTree(int maxN) {
        this.maxN = maxN;
        this.root = new Node();
    }

    /**
     * 修改单点
     */
    public void apply(Node node, long val, int ls, int rs) {
        node.sum += val;
    }

    /**
     * 两个子区间统计信息进行合并，子区间可以是查询时动态构造的，而不一定是某个node的范围
     */
    public void reduce(Node node, Node left, Node right, int ls, int rs) {
        node.sum = left.sum + right.sum;
    }

    public void add(int i, long val) {
        add(root, i, val, 0, maxN);
    }

    /**
     * 当前Node的范围: [ls,rs]
     */
    private void add(Node node, int i, long val, int ls, int rs) {
        if (ls==rs) {
            apply(node, val, ls, rs);
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
        reduce(node, node.left, node.right, ls, rs);
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
}
