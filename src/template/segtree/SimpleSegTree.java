package template.segtree;

class SimpleSegTree {

    static class Node {
        Node left;
        Node right;
        long sum;
    }

    int maxN;
    Node root;

    public SimpleSegTree(int maxN) {
        this.maxN = maxN;
        this.root = new Node();
    }

    long trim(long v) {
        //可以加上取余
        return v;
    }

    public void add(int i, long val) {
        add(root, i, val, 0, maxN);
    }

    /**
     * 当前Node的范围: [ls,rs]
     */
    private void add(Node node, int i, long val, int ls, int rs) {
        if (ls==rs) {
            node.sum = trim(node.sum+val);
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
        node.sum = trim(node.sum + val);
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

    public long sum(int l, int r) {
        return sum(root, l, r, 0, maxN);
    }

    private long sum(Node node, int l, int r, int ls, int rs) {
        if (r<l) {
            return 0;
        }
        if (r<0){
            throw new IllegalArgumentException("index:"+r);
        }
        if (l <= ls && rs <= r) {
            return node.sum;
        }
        pushDown(node, ls, rs);
        int mid = ls + rs >> 1;
        long res = 0;
        if (l <= mid) {
            res += sum(node.left, l, r, ls, mid);
        }
        if (r >= mid + 1) {
            res += sum(node.right, l, r, mid + 1, rs);
        }
        return trim(res);
    }
}
