package template.segtree.finegrind;

/**
 * tree[i]表示i在数组中出现的次数。可单点更新并查询数组的前k大之和。
 *
 * @Author Create by CROW
 * @Date 2023/6/18
 */
class RankTree {
    static final int OP_ADD = 1;
    static final int OP_SET = 2;

    public static class Node {
        Node left;
        Node right;
        int ls, rs;//debug用
        long sum;
        long cnt;
        long lazyVal;
        int lazyType;
    }

    int maxN;
    Node root;

    public RankTree(int maxN) {
        this.maxN = maxN;
        this.root = createNode(0, maxN);
    }

    Node createNode(int ls, int rs) {
        Node node = new Node();
        node.ls = ls;
        node.rs = rs;
        return node;
    }

    /**
     * 更新范围覆盖了整个node，要做两件事：
     * 1 更新懒标记
     * 2 维护统计信息
     */
    void apply(Node node, int ls, int rs, int type, long val) {
        node.lazyType = type;
        long len = (rs - ls + 1);
        if (type == OP_ADD) {
            node.lazyVal += val;
            node.cnt += len * val;
            node.sum += len * (rs + ls) / 2 * val;
        } else if (type==OP_SET) {
            node.lazyVal = val;
            node.cnt = len * val;
            node.sum = len * (rs + ls) / 2 * val;
        }
    }

    /**
     * 两个子区间统计信息进行合并，子区间可以是查询时动态构造的，而不一定是某个node的范围
     */
    void reduce(Node node, Node left, Node right, int ls, int rs) {
        node.sum = left.sum + right.sum;
        node.cnt = left.cnt + right.cnt;
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

    public void add(int i, long val) {
        add(root, i, i, OP_ADD, val, 0, maxN);
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
            node.left = createNode(ls, mid);
        }
        if (node.right == null) {
            node.right = createNode(mid + 1, rs);
        }
        // 1 如果有多种懒操作变量，注意下传顺序，以及下传后的重置
        // 2 lazyVal会累积，即使每次add都是val==1，下传的时候lazyVal也会>1
        if (node.lazyType != 0) {
            apply(node.left, ls, mid, node.lazyType, node.lazyVal);
            apply(node.right, mid + 1, rs, node.lazyType, node.lazyVal);
            node.lazyType = 0;
            node.lazyVal = 0;
        }
    }

    private final Node ansNode = new Node();

    public long sum(int l, int r) {
        ansNode.sum = 0;
        query(root, l, r, 0, maxN);
        return ansNode.sum;
    }

    public long cnt(int l, int r) {
        ansNode.cnt = 0;
        query(root, l, r, 0, maxN);
        return ansNode.cnt;
    }

    private void query(Node node, int l, int r, int ls, int rs) {
        if (l < 0 || r > maxN) {
            throw new IllegalArgumentException();
        }
        if (l <= ls && rs <= r) {
            reduce(ansNode, node, ansNode, ls, rs);
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

    // 最小的cnt个数之和
    public long kMinSum(Node node, long cnt) {
        if (cnt == 0) return 0;
        int ls = node.ls, rs = node.rs;
        if (ls == rs && node.cnt > cnt) {
            return node.sum / node.cnt * cnt;
        }
        if (node.cnt <= cnt) {
            return node.sum;
        }
        pushDown(node, ls, rs);
        long res = 0;
        if (node.left.cnt < cnt) {
            res += node.left.sum + kMinSum(node.right, cnt - node.left.cnt);
        } else {
            res = kMinSum(node.left, cnt);
        }
        return res;
    }

    // 最大的cnt个数之和
    public long kMaxSum(Node node, long cnt) {
        if (cnt == 0) return 0;
        int ls = node.ls, rs = node.rs;
        if (ls == rs && node.cnt > cnt) {
            return node.sum / node.cnt * cnt;
        }
        if (node.cnt <= cnt) {
            return node.sum;
        }
        pushDown(node, ls, rs);
        long res = 0;
        if (node.right.cnt < cnt) {
            res += res + node.right.sum + kMaxSum(node.left, cnt - node.right.cnt);
        } else {
            res = kMaxSum(node.right, cnt);
        }
        return res;
    }
}
