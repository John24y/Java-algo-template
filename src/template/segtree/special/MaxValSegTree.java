package template.segtree.special;

class MaxValSegTree {
    static final int OP_ADD = 1;
    static final int OP_SET = 2;
    static NodePool TMP_POOL = new NodePool();

    static class NodePool {
        int i = 1;
        Node[] pool = new Node[129];

        public NodePool() {
            for (int j = 0; j < pool.length; j++) {
                pool[j] = new Node();
            }
            pool[0].init(0, -1);
        }

        Node immutableEmpty() {
            return pool[0];
        }

        Node next(int ls, int rs) {
            Node ret = pool[i++];
            ret.initForQuery(ls, rs);
            if (i == pool.length) i = 1;
            return ret;
        }
    }

    static class Node {
        Node left;
        Node right;
        int ls, rs;//debug用

        long maxVal;
        int lazyType;
        long lazyVal;
        long sum;

        void init(int ls, int rs) {
            this.ls = ls;
            this.rs = rs;
        }

        void initForQuery(int ls, int rs) {
            this.ls = ls;
            this.rs = rs;
            sum = 0;
            maxVal = Long.MIN_VALUE;
        }
    }

    int maxN;
    Node root;

    public MaxValSegTree(int maxN) {
        this.maxN = maxN;
        this.root = new Node();
        this.root.init(0, maxN);
    }

    /**
     * 1 *累加*懒标记
     * 2 维护统计信息
     * 3 val可能在多次懒修改中累积
     */
    void apply(Node node, int ls, int rs, int type, long val) {
        node.lazyType = type;
        if (type == OP_ADD) {
            node.lazyVal += val;
            node.sum += (rs - ls + 1) * val;
            node.maxVal += val;
        } else if (type == OP_SET) {
            node.lazyVal = val;
            node.sum = (rs - ls + 1) * val;
            node.maxVal = val;
        }
    }

    void reduce(Node node, Node left, Node right, int ls, int rs) {
        node.sum = left.sum + right.sum;
        node.maxVal = Math.max(left.maxVal, right.maxVal);
    }

    void build(long[] vals) {
        build(root, vals, 0, maxN);
    }

    private void build(Node node, long[] vals, int ls, int rs) {
        if (ls == rs) {
            if (ls >= vals.length) return;
            apply(node, ls, rs, OP_SET, vals[ls]);
            return;
        }
        pushDown(node, ls, rs);
        int mid = ls + rs >> 1;
        build(node.left, vals, ls, mid);
        build(node.right, vals, mid + 1, rs);
        reduce(node, node.left, node.right, ls, rs);
    }

    public void add(int l, int r, long val) {
        update(root, l, r, 0, maxN, OP_ADD, val);
    }

    public void set(int l, int r, long val) {
        update(root, l, r, 0, maxN, OP_SET, val);
    }

    /**
     * 当前Node的范围: [ls,rs]
     */
    private void update(Node node, int l, int r, int ls, int rs, int type, long val) {
        if (l < 0 || r > maxN) {
            throw new IllegalArgumentException();
        }
        if (l <= ls && rs <= r) {
            apply(node, ls, rs, type, val);
            return;
        }

        pushDown(node, ls, rs);
        int mid = ls + rs >> 1;
        //左子树[ls,mid]
        //右子树[mid+1,rs]
        if (l <= mid) {
            update(node.left, l, r, ls, mid, type, val);
        }
        if (r >= mid + 1) {
            update(node.right, l, r, mid + 1, rs, type, val);
        }
        reduce(node, node.left, node.right, ls, rs);
    }

    //下发lazy值
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
        if (node.lazyType != 0) {
            // 1 如果有多种懒操作变量，注意下传顺序，以及下传后的重置
            // 2 lazyVal会累积，即使每次add都是val==1，下传的时候lazyVal也会>1
            apply(node.left, ls, mid, node.lazyType, node.lazyVal);
            apply(node.right, mid + 1, rs, node.lazyType, node.lazyVal);
            node.lazyType = 0;
            node.lazyVal = 0;
        }
    }

    public Node query(int l, int r) {
        return query(root, l, r, 0, maxN);
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
        Node left, right;
        left = right = TMP_POOL.immutableEmpty();
        if (l <= mid) {
            left = query(node.left, l, r, ls, mid);
        }
        if (r >= mid + 1) {
            right = query(node.right, l, r, mid + 1, rs);
        }
        Node ret = TMP_POOL.next(Math.max(ls, l), Math.min(rs, r));
        reduce(ret, left, right, ret.ls, ret.rs);
        return ret;
    }

}

class ExMaxValSegTree extends MaxValSegTree {

    public ExMaxValSegTree(int maxN) {
        super(maxN);
    }

    /**
     * 查询[l,r]上第一个>x的下标
     *
     * @param fromLeft true 找左边第一个，false 找右边第一个
     * @return -1 表示找不到
     */
    public int queryFirstGreater(int l, int r, int x, boolean fromLeft) {
        int[] order = fromLeft ? new int[]{0, 1} : new int[]{1, 0};
        return queryFirstGreater(root, l, r, x, order, 0, maxN);
    }

    private int queryFirstGreater(Node node, int l, int r, int x, int[] order, int ls, int rs) {
        int mid = ls + rs >> 1;
        if (node.maxVal <= x) {
            return -1;
        }
        if (ls == rs) {
            return ls;
        }
        pushDown(node, ls, rs);

        for (int ord : order) {
            if (ord == 0 && l <= mid) {
                int ret = queryFirstGreater(node.left, l, r, x, order, ls, mid);
                if (ret != -1) {
                    return ret;
                }
            }
            if (ord == 1 && r >= mid + 1) {
                int ret = queryFirstGreater(node.right, l, r, x, order, mid + 1, rs);
                if (ret != -1) {
                    return ret;
                }
            }
        }
        return -1;
    }

    /**
     * 前k大的数-1. 下标从1开始，下标0不存储元素。
     * 如果第k大的数<=0，则返回false，并且不操作。
     * 前提是整颗树是从大到小排序号的。
     */
    public boolean decTopK(int k) {
        long v = query(k, k).sum;
        if (v <= 0) {
            return false;
        }
        int r2 = queryFirstGreater(1, maxN, (int) v - 1, false);
        int r1 = queryFirstGreater(1, maxN, (int) v, false);
        if (r1 == -1) r1 = 0;
        add(0, r1, -1);
        add(r2 - (k - r1) + 1, r2, -1);
        return true;
    }
}

