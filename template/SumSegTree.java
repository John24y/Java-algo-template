

class SumSegTree {

    static class Node {
        Node left;
        Node right;
        long sum;
        long lazy;
        int ls, rs;//debug用
    }

    int maxN;
    Node root;

    public SumSegTree(int maxN) {
        this.maxN = maxN;
        this.root = new Node();
        root.ls = 0;
        root.rs = maxN;
    }

    long trim(long v) {
        //可以加上取余
        return v;
    }

    public void add(int l, int r, long val) {
        add(root, l, r, val, 0, maxN);
    }

    /**
     * 当前Node的范围: [ls,rs]
     */
    private void add(Node node, int l, int r, long val, int ls, int rs) {
        if (r<l){
            return;
        }
        if (r<0){
            throw new IllegalArgumentException("index:"+r);
        }
        if (l <= ls && rs <= r) {
            //[l,r]覆盖了当前子树
            node.sum = trim(node.sum+(rs - ls + 1) * val);
            node.lazy = trim(node.lazy + val);
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
        int cover=Math.min(r,rs)-Math.max(l,ls)+1;
        node.sum = trim(node.sum + val * cover);
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
        //pushDown的时候并没有去下发lazy值,因为对于求sum来说,可以在query的时候把整条路径上的lazy都算上
    }

    public long query(int l, int r) {
        return query(root, l, r, 0, maxN);
    }

    private long query(Node node, int l, int r, int ls, int rs) {
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
        int cover=Math.min(r,rs)-Math.max(l,ls)+1;
        long res = trim(cover*node.lazy);
        if (l <= mid) {
            res += query(node.left, l, r, ls, mid);
        }
        if (r >= mid + 1) {
            res += query(node.right, l, r, mid + 1, rs);
        }
        return trim(res);
    }
}