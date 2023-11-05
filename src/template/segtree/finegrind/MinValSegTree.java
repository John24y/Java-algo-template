package template.segtree.finegrind;


/**
 * https://leetcode.com/problems/largest-rectangle-in-histogram
 */
class MinValSegTree {
    //对查询结果相加需要注意溢出
    static final long INIT = Long.MAX_VALUE;
    static final int OP_ADD = 1;
    static final int OP_SET = 2;

    static class Node {
        Node left;
        Node right;
        int ls, rs;//debug用

        long minVal;
        int minId;//index最小的
        int lazyType;
        long lazyVal;
        long sum;
    }

    int maxN;
    Node root;

    public MinValSegTree(int maxN) {
        this.maxN = maxN;
        this.root = createNode(0, maxN);
        root.ls = 0;
        root.rs = maxN;
    }

    Node createNode(int ls, int rs) {
        Node r = new Node();
        r.minId = ls;
        r.minVal = INIT;
        return r;
    }

    void apply(Node node, int ls, int rs, int type, long val) {
        node.lazyType = type;
        node.lazyVal = val;
        if (type == OP_ADD) {
            node.sum += (rs - ls + 1) * val;
            node.minVal -= val;
        } else if (type==OP_SET) {
            node.sum = (rs - ls + 1) * val;
            node.minVal = val;
        }
    }

    void reduce(Node node, Node left, Node right, int ls, int rs) {
        node.sum = left.sum + right.sum;
        if (right.minVal < left.minVal) {
            node.minId = right.minId;
            node.minVal = right.minVal;
        } else {
            node.minId = left.minId;
            node.minVal = left.minVal;
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
        if (ls==rs) {
            apply(node, ls, rs, OP_SET, vals[ls]);
            return;
        }
        pushDown(node, ls, rs);
        int mid = ls + rs >> 1;
        build(node.left,vals, ls, mid);
        build(node.right,vals,mid + 1, rs);
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
            node.left = createNode(ls, mid);
            node.left.ls = ls;
            node.left.rs = mid;
        }
        if (node.right == null) {
            node.right = createNode(mid + 1, rs);
            node.right.ls = mid + 1;
            node.right.rs = rs;
        }
        if (node.lazyType != 0) {
            apply(node.left, ls, mid, node.lazyType, node.lazyVal);
            apply(node.right, mid + 1, rs, node.lazyType, node.lazyVal);
            node.lazyType = 0;
        }
    }

    public long sum(int l, int r) {
        queryNode.sum = 0;
        query(root, l, r, 0, maxN);
        return queryNode.sum;
    }

    public long queryMinVal(int l, int r) {
        queryNode.minVal = INIT;
        queryNode.minId = -1;
        query(root, l, r, 0, maxN);
        return queryNode.minVal;
    }

    //查询的下标，如果有多个则取下标最小的
    public int queryMinId(int l, int r) {
        queryNode.minVal = INIT;
        queryNode.minId = -1;
        query(root, l, r, 0, maxN);
        return queryNode.minId;
    }

    private static final Node EMPTY = new Node();
    private final Node queryNode = new Node();

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