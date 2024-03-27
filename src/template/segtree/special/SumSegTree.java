package template.segtree.special;

/**
 * 简单版求和线段树，可区间查询区间求和，不支持功能扩展，需要复杂功能用 {@link LazySegTree}
 *
 * https://www.luogu.com.cn/problem/P3372
 */
class SumSegTree {

    static class Node {
        Node left;
        Node right;
        long sum = 0;
        long lazy = 0;
        int ls, rs;
    }

    int maxN;
    Node root;

    public SumSegTree(int maxN) {
        this.maxN = maxN;
        this.root = new Node();
        root.ls = 0;
        root.rs = maxN;
    }

    /**
     * 覆盖区间更新，可做取余等操作
     */
    public void apply(Node node, long lazyVal, int ls, int rs) {
        node.sum += lazyVal * (rs - ls + 1);
        node.lazy += lazyVal;
    }

    /**
     * 区间统计值合并，可做取余等操作
     */
    public long reduce(long left, long right) {
        return left + right;
    }

    /**
     * O(n)设置初始值
     */
    void build(long[] vals) {
        build(root, vals, 0, maxN);
    }

    private void build(Node node, long[] vals, int ls, int rs) {
        if (ls==rs) {
            node.sum = vals[ls];
            return;
        }
        pushDown(node, ls, rs);
        int mid = ls + rs >> 1;
        build(node.left,vals, ls, mid);
        build(node.right,vals,mid + 1, rs);
        node.sum = reduce(node.left.sum, node.right.sum);
    }

    public void add(int l, int r, long val) {
        add(root, l, r, val, 0, maxN);
    }

    /**
     * 当前Node的范围: [ls,rs]
     */
    private void add(Node node, int l, int r, long val, int ls, int rs) {
        if (l < 0 || r > maxN || r < 0) throw new IllegalArgumentException("index:" + l + "," + r);
        if (l <= ls && rs <= r) {
            //[l,r]覆盖了当前子树
            apply(node, val, ls, rs);
            return;
        }

        pushDown(node, ls, rs);
        int mid = ls + rs >> 1;
        //左子树[ls,mid]
        //右子树[mid+1,rs]
        if (l <= mid) {
            add(node.left, l, r, val, ls, mid);
        }
        if (r >= mid + 1) {
            add(node.right, l, r, val, mid + 1, rs);
        }
        node.sum = reduce(node.left.sum, node.right.sum);
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
        if (node.lazy != 0) {
            apply(node.left, node.lazy, ls, mid);
            apply(node.right, node.lazy, mid + 1, rs);
            node.lazy = 0;
        }
    }

    public long sum(int l, int r) {
        return sum(root, l, r, 0, maxN);
    }

    private long sum(Node node, int l, int r, int ls, int rs) {
        if (l < 0 || r > maxN || r < 0) throw new IllegalArgumentException("index:" + l + "," + r);
        if (l <= ls && rs <= r) {
            return node.sum;
        }
        pushDown(node, ls, rs);
        int mid = ls + rs >> 1;
        long left = 0;
        long right = 0;
        if (l <= mid) {
            left = sum(node.left, l, r, ls, mid);
        }
        if (r >= mid + 1) {
            right = sum(node.right, l, r, mid + 1, rs);
        }
        return reduce(left, right);
    }
}