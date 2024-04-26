package main.lc;
class LazySegTree {
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

    void reduce(Node node, Node left, Node right) {
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
        reduce(node, node.left, node.right);
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
        reduce(node, node.left, node.right);
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
        Node left, right;
        left = right = TMP_POOL.immutableEmpty();
        if (l <= mid) {
            left = query(node.left, l, r, ls, mid);
        }
        if (r >= mid + 1) {
            right = query(node.right, l, r, mid + 1, rs);
        }
        Node ret = new Node();
        reduce(ret, left, right);
        return ret;
    }

}
    class Solution {
        public int[] fullBloomFlowers(int[][] flowers, int[] persons) {
            LazySegTree tree=new LazySegTree((int) 1e9);
            for (int[] a : flowers) {
                tree.add(a[0],a[1],1);
            }
            int[] ans=new int[persons.length];
            for (int i = 0; i < persons.length; i++) {
                ans[i]= (int) tree.sum(persons[i],persons[i]);
            }
            return ans;
        }
    }
