package template.segtree;

/**
 * 区间修改区间查询线段树。改模板主要是两个方法 apply 和 reduce.
 * <p>
 * apply(Node node, int ls, int rs, long val)
 * 更新范围覆盖了整个node，要做两件事：
 * 1 更新懒标记 (不要依赖val是常量：即使add时val传参是常量比如1，pushDown时调用apply，val也会大于1)
 * 2 维护统计信息（覆盖node时必须O(1)时间更新单个node，才能实现整体O(logn)的更新）.
 * <p>
 * reduce(Node node, Node left, Node right, int ls, int rs)
 * 两个子区间统计信息进行合并，子区间可以是查询时动态构造的，而不一定是某个node的范围
 *
 * https://www.luogu.com.cn/problem/P3372
 * https://leetcode.com/problems/number-of-flowers-in-full-bloom/submissions/
 *
 * @Author Create by CROW
 * @Date 2023/2/19
 */
class LazySegTree {

    public static class Node {
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
     * 1 更新懒标记 (不要依赖val是常量：即使add时val传参是常量比如1，pushDown时调用apply，val也会大于1)
     * 2 维护统计信息（覆盖node时必须O(1)时间更新单个node，才能实现整体O(logn)的更新）.
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
            if (ls<vals.length) {
                apply(node, ls, rs, vals[ls]);
            }
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
    private final Node queryNode = new Node();

    public long sum(int l, int r) {
        queryNode.sum=0;
        query(root, l, r, 0, maxN);
        return queryNode.sum;
    }

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

    /**
     * 查询时总是左右区间合并，而不是合并到全局的queryNode
     * 查询过程需要创建logn个对象，但有时候必须这样合并，比如统计区间内连续1的数量
     */
    private Node queryStrictMerge(Node node, int l, int r, int ls, int rs) {
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
            leftRes = queryStrictMerge(node.left, l, r, ls, mid);
        }
        if (r >= mid + 1) {
            rightRes = queryStrictMerge(node.right, l, r, mid + 1, rs);
        }
        reduce(res, leftRes, rightRes, ls, rs);
        return res;
    }
}

/**
 * tree[i]表示i在数组中出现的次数，数组可动态修改的情况下求数组的第k小
 *
 * @Author Create by CROW
 * @Date 2023/2/19
 */
class RankTree extends LazySegTree {

    public RankTree(int maxN) {
        super(maxN);
    }

    /**
     * 假设tree[i]表示i在数组中出现的次数，求数组的第k小，如果不存在返回-1
     */
    public long kSmallest(long k) {
        return queryFirstGE(k);
    }

    /**
     * 查找使sum([0,r])>=v最小的r，如果不存在返回-1
     */
    public long queryFirstGE(long v) {
        return queryFirstGE(root, 0, maxN, v);
    }

    private long queryFirstGE(Node node, int ls, int rs, long v) {
        if (node.sum < v) {
            return -1;
        }
        if (ls == rs) {
            return ls;
        }
        int mid = ls + rs >> 1;
        pushDown(node, ls, rs);
        if (node.left.sum >= v) {
            return queryFirstGE(node.left, ls, mid, v);
        } else {
            return queryFirstGE(node.right, mid + 1, rs, v - node.left.sum);
        }
    }
}

