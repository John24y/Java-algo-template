package template.segtree.specialized;


/**
 * https://leetcode.com/problems/largest-rectangle-in-histogram
 */
class MinValSegTree {
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

        long minVal;
        int minId;
        int lazyType;
        long lazyVal;

        void init(int ls, int rs) {
            this.ls = ls;
            this.rs = rs;
            minId = ls;
            minVal = 0;
        }

        void initForQuery(int ls, int rs) {
            this.ls = ls;
            this.rs = rs;
            minId = ls;
            minVal = Long.MAX_VALUE;
        }
    }

    int maxN;
    Node root;

    public MinValSegTree(int maxN) {
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
        if (type == OP_ADD) {
            node.lazyVal += val;
            node.minVal += val;
            node.lazyType = node.lazyType == OP_SET ? OP_SET : OP_ADD;
        } else if (type == OP_SET) {
            node.lazyVal = val;
            node.minVal = val;
            node.lazyType = type;
        }
    }

    void reduce(Node node, Node left, Node right, int ls, int rs) {
        if (left.minVal <= right.minVal) {
            node.minVal = left.minVal;
            node.minId = left.minId;
        } else {
            node.minVal = right.minVal;
            node.minId = right.minId;
        }
    }

    public void add(int l, int r, long val) {
        update(root, l, r, 0, maxN, OP_ADD, val);
    }

    public void set(int l, int r, long val) {
        update(root, l, r, 0, maxN, OP_SET, val);
    }

    void build(int[] vals) {
        build(root, vals, 0, maxN);
    }

    private void build(Node node, int[] vals, int ls, int rs) {
        if (ls == rs) {
            if (ls>=vals.length) return;
            apply(node, ls, rs, OP_SET, vals[ls]);
            return;
        }
        pushDown(node, ls, rs);
        int mid = ls + rs >> 1;
        build(node.left, vals, ls, mid);
        build(node.right, vals, mid + 1, rs);
        reduce(node, node.left, node.right, ls, rs);
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

    /**
     * 查询[l,r]上第一个 < x的下标 (注意不含等于)
     *
     * @param fromLeft true 找左边第一个，false 找右边第一个
     * @return -1 表示找不到
     */
    public int queryFirstLess(int l, int r, int x, boolean fromLeft) {
        int[] order = fromLeft ? new int[]{0, 1} : new int[]{1, 0};
        return queryFirstLess(root, l, r, x, order, 0, maxN);
    }

    private int queryFirstLess(Node node, int l, int r, int x, int[] order, int ls, int rs) {
        int mid = ls + rs >> 1;
        if (node.minVal >= x) {
            return -1;
        }
        if (ls == rs) {
            return ls;
        }
        pushDown(node, ls, rs);

        for (int ord : order) {
            if (ord == 0 && l <= mid) {
                int ret = queryFirstLess(node.left, l, r, x, order, ls, mid);
                if (ret != -1) {
                    return ret;
                }
            }
            if (ord == 1 && r >= mid + 1) {
                int ret = queryFirstLess(node.right, l, r, x, order, mid + 1, rs);
                if (ret != -1) {
                    return ret;
                }
            }
        }
        return -1;
    }
}


class ExMinValSegTree extends MinValSegTree {

    public ExMinValSegTree(int maxN) {
        super(maxN);
    }

    /**
     * 查询[l,r]上第一个 < x的下标 (注意不含等于)
     *
     * @param fromLeft true 找左边第一个，false 找右边第一个
     * @return -1 表示找不到
     */
    public int queryFirstLess(int l, int r, int x, boolean fromLeft) {
        int[] order = fromLeft ? new int[]{0, 1} : new int[]{1, 0};
        return queryFirstLess(root, l, r, x, order, 0, maxN);
    }

    private int queryFirstLess(Node node, int l, int r, int x, int[] order, int ls, int rs) {
        int mid = ls + rs >> 1;
        if (node.minVal >= x) {
            return -1;
        }
        if (ls == rs) {
            return ls;
        }
        pushDown(node, ls, rs);

        for (int ord : order) {
            if (ord == 0 && l <= mid) {
                int ret = queryFirstLess(node.left, l, r, x, order, ls, mid);
                if (ret != -1) {
                    return ret;
                }
            }
            if (ord == 1 && r >= mid + 1) {
                int ret = queryFirstLess(node.right, l, r, x, order, mid + 1, rs);
                if (ret != -1) {
                    return ret;
                }
            }
        }
        return -1;
    }
}