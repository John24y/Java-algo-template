package template.segtree.specialized;

/**
 * 可以求第k小
 *
 * @Author Create by crow
 * @Date 2024/4/26
 */
class SimpleRankTree {

    public static class Node {
        Node left;
        Node right;
        int ls, rs;
        int cnt;
    }

    int maxN;
    Node root;

    public SimpleRankTree(int maxN) {
        this.maxN = maxN;
        this.root = createNode(0, maxN);
    }

    Node createNode(int ls, int rs) {
        Node node = new Node();
        node.ls = ls;
        node.rs = rs;
        return node;
    }

    void apply(Node node, int ls, int rs, int val) {
        node.cnt += val;
    }

    void reduce(Node node, Node left, Node right, int ls, int rs) {
        node.cnt = left.cnt + right.cnt;
    }

    public void add(int l, int r, int val) {
        add(root, l, r, val, 0, maxN);
    }

    private void add(Node node, int l, int r, int val, int ls, int rs) {
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
            add(node.left, l, r,  val, ls, mid);
        }
        if (r >= mid + 1) {
            add(node.right, l, r,  val, mid + 1, rs);
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
    }

    public int cnt(int l, int r) {
        return cnt(root, l, r, 0, maxN);
    }

    private int cnt(Node node, int l, int r, int ls, int rs) {
        if (l < 0 || r > maxN) {
            throw new IllegalArgumentException();
        }
        if (l <= ls && rs <= r) {
            return node.cnt;
        }
        pushDown(node, ls, rs);
        int mid = ls + rs >> 1;
        int res=0;
        if (l <= mid) {
            res+=cnt(node.left, l, r, ls, mid);
        }
        if (r >= mid + 1) {
            res+=cnt(node.right, l, r, mid + 1, rs);
        }
        return res;
    }

    public int kthMin(Node node, long cnt) {
        if (cnt == 0) throw new IllegalArgumentException();
        int ls = node.ls, rs = node.rs;
        if (ls == rs) {
            if (node.cnt<cnt) throw new IllegalArgumentException("not enough");
            return ls;
        }
        pushDown(node, ls, rs);
        if (node.left.cnt < cnt) {
            return kthMin(node.right, cnt - node.left.cnt);
        } else {
            return kthMin(node.left, cnt);
        }
    }

}
