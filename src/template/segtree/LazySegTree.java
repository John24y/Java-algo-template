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
 * <p>
 * https://www.luogu.com.cn/problem/P3372
 * https://leetcode.com/problems/number-of-flowers-in-full-bloom/submissions/
 *
 * @Author Create by CROW
 * @Date 2023/2/19
 */
class LazySegTree {
    static final int OP_ADD = 1;
    static final int OP_SET = 2;
    static Node EMPTY = new Node();
    static {
        EMPTY.init(-1,0);
    }

    public static class Node {
        Node left;
        Node right;
        int ls, rs;
        long sum;
        long lazyVal;
        int lazyType;

        void init(int ls, int rs) {
            this.ls = ls;
            this.rs = rs;
        }

        void initForQuery(int ls, int rs) {
            this.ls = ls;
            this.rs = rs;
            sum = 0;
        }
    }

    int maxN;
    Node root;

    public LazySegTree(int maxN) {
        this.maxN = maxN;
        this.root = new Node();
        this.root.init(0, maxN);
    }

    /**
     * 1 *累加*懒标记
     * 2 维护统计信息
     * 3 @param val可能在多次懒修改中累积，并非总是调用add时的传参
     */
    void apply(Node node, int ls, int rs, int type, long val) {
        node.lazyType = type;
        if (type == OP_ADD) {
            node.lazyVal += val;
            node.sum += (rs - ls + 1) * val;
        } else if (type==OP_SET) {
            node.lazyVal = val;
            node.sum = (rs - ls + 1) * val;
        }
    }

    void reduce(Node node, Node left, Node right, int ls, int rs) {
        node.sum = left.sum + right.sum;
    }

    /**
     * O(n)设置初始值
     */
    void build(long[] vals) {
        build(root, vals, 0, maxN);
    }

    private void build(Node node, long[] vals, int ls, int rs) {
        if (ls == rs) {
            if (ls < vals.length) {
                apply(node, ls, rs, OP_ADD, vals[ls]);
            }
            return;
        }
        pushDown(node, ls, rs);
        int mid = ls + rs >> 1;
        build(node.left, vals, ls, mid);
        build(node.right, vals, mid + 1, rs);
        reduce(node, node.left, node.right, ls, rs);
    }

    public void add(int l, int r, long val) {
        add(root, l, r, OP_ADD, val, 0, maxN);
    }

    /**
     * 当前Node的范围: [ls,rs]
     */
    private void add(Node node, int l, int r, int type, long val, int ls, int rs) {
        if (l < 0 || r > maxN) {
            throw new IllegalArgumentException();
        }
        if (l <= ls && rs <= r) {
            apply(node, ls, rs, type, val);
            return;
        }
        pushDown(node, ls, rs);
        int mid = ls + rs >> 1;
        if (l <= mid) {
            add(node.left, l, r, type, val, ls, mid);
        }
        if (r >= mid + 1) {
            add(node.right, l, r, type, val, mid + 1, rs);
        }
        reduce(node, node.left, node.right, ls, rs);
    }

    void pushDown(Node node, int ls, int rs) {
        int mid = ls + rs >> 1;
        if (node.left == null) {
            node.left = new Node();
            node.left.init(ls, mid);
        }
        if (node.right == null) {
            node.right = new Node();
            node.right.init(mid+1, rs);
        }
        // 如果有多种懒操作变量，注意下传顺序，以及下传后的重置
        if (node.lazyType != 0) {
            apply(node.left, ls, mid, node.lazyType, node.lazyVal);
            apply(node.right, mid + 1, rs, node.lazyType, node.lazyVal);
            node.lazyType = 0;
            node.lazyVal = 0;
        }
    }

    public long sum(int l, int r) {
        return query(root, l, r, 0, maxN).sum;
    }

    private Node query(Node node, int l, int r, int ls, int rs) {
        if (l < 0 || r > maxN) {
            throw new IllegalArgumentException();
        }
        if (l <= ls && rs <= r) {
            return node;
        }
        pushDown(node, ls, rs);
        int mid = ls + rs >> 1;
        Node left = EMPTY, right = EMPTY;
        if (l <= mid) {
            left = query(node.left, l, r, ls, mid);
        }
        if (r >= mid + 1) {
            right = query(node.right, l, r, mid + 1, rs);
        }
        Node ret = new Node();
        ret.initForQuery(Math.max(ls, l), Math.min(rs, r));
        reduce(ret, left, right, ret.ls, ret.rs);
        return ret;
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

