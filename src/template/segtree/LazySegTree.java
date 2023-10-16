package template.segtree;

/**
 * https://leetcode.com/problems/number-of-flowers-in-full-bloom/submissions/
 *
 * @Author Create by CROW
 * @Date 2023/2/19
 */
class LazySegTree {

    static class Node {
        Node left;
        Node right;
        long lazyAdd;
        long sum;
        int ls, rs;//debug用
    }

    int maxN;
    Node root;

    public LazySegTree(int maxN) {
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

    private static final Node EMPTY = new Node();
    private final Node sumAns=new Node();

    public long sum(int l, int r) {
        sumAns.sum=0;
        sum(root, l, r, 0, maxN);
        return sumAns.sum;
    }

    private void sum(Node node, int l, int r, int ls, int rs) {
        if (l < 0 || r > maxN) {
            throw new IllegalArgumentException();
        }
        if (l <= ls && rs <= r) {
            reduce(sumAns, node, sumAns, ls, rs);
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

    /**
     * sum方法为了减少创建对象，所有[l,r]范围内覆盖节点合计(reduce)到全局对象。
     * 但有些情况下必须每一层左右区间合并，比如求区间内连续1的数量，可以用此方法。
     */
    public Node sumInLevel(int l, int r) {
        return sumInLevel(root, l, r, 0, maxN);
    }

    private Node sumInLevel(Node node, int l, int r, int ls, int rs) {
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
            leftRes = sumInLevel(node.left, l, r, ls, mid);
        }
        if (r >= mid + 1) {
            rightRes = sumInLevel(node.right, l, r, mid + 1, rs);
        }
        reduce(res, leftRes, rightRes, ls, rs);
        return res;
    }


}
