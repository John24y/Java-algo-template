package template.segtree;

/**
 * 主席树，可实现静态数组查询区间排名。
 *
 * @Author Create by CROW
 * @Date 2023/4/29
 */
class RankPrefixSegTree {

    class Node {
        Node left;
        Node right;
        long sum;

        public Node copy() {
            Node node = new Node();
            node.left = left;
            node.right = right;
            node.sum = sum;
            return node;
        }
    }

    int maxN;
    Node root;

    public RankPrefixSegTree(int maxN) {
        this.maxN = maxN;
        this.root = new Node();
    }

    /**
     * 在preNode作为root的基础上更新
     */
    public Node addCount(Node preNode, int i, long val) {
        return add(preNode.copy(), i, val, 0, maxN);
    }

    /**
     * 当前Node的范围: [ls,rs]
     */
    private Node add(Node node, int i, long val, int ls, int rs) {
        if (i < 0 || i > maxN) {
            throw new IllegalArgumentException();
        }
        if (ls == rs) {
            assert ls==i;
            node.sum += val;
            return node;
        }

        pushDown(node, ls, rs);
        int mid = ls + rs >> 1;
        //左子树[ls,mid]
        //右子树[mid+1,rs]
        if (i <= mid) {
            node.left = node.left.copy();
            add(node.left, i, val, ls, mid);
        } else {
            node.right = node.right.copy();
            add(node.right, i, val, mid + 1, rs);
        }
        node.sum += val;
        return node;
    }

    void pushDown(Node node, int ls, int rs) {
        if (node.left == null) {
            node.left = new Node();
        }
        if (node.right == null) {
            node.right = new Node();
        }
    }

    public long sum(Node oldRoot, Node newRoot, int l, int r) {
        return sum(oldRoot, newRoot, l, r, 0, maxN);
    }

    private long sum(Node oldRoot, Node newRoot, int l, int r, int ls, int rs) {
        if (l < 0 || r > maxN) {
            throw new IllegalArgumentException();
        }
        if (l <= ls && rs <= r) {
            return newRoot.sum - oldRoot.sum;
        }
        pushDown(oldRoot, ls, rs);
        pushDown(newRoot, ls, rs);
        int mid = ls + rs >> 1;
        long res = 0;
        if (l <= mid) {
            res += sum(oldRoot.left, newRoot.left, l, r, ls, mid);
        }
        if (r >= mid + 1) {
            res += sum(oldRoot.right, newRoot.right, l, r, mid + 1, rs);
        }
        return res;
    }

    /**
     * 假设tree[i]表示i在数组中出现的次数，求数组的第k小，如果不存在返回-1
     */
    public long kSmallest(Node oldRoot, Node newRoot, long k) {
        return queryFirstGE(oldRoot, newRoot, 0, maxN, k);
    }

    /**
     * 查找使sum([0,r])>=v最小的r，如果不存在返回-1
     */
    private long queryFirstGE(Node oldRoot, Node newRoot, int ls, int rs, long v) {
        long sum = newRoot.sum - oldRoot.sum;
        if (sum < v) {
            return -1;
        }
        if (ls == rs) {
            return ls;
        }
        int mid = ls + rs >> 1;
        pushDown(oldRoot, ls, rs);
        pushDown(newRoot, ls, rs);
        long leftSum = newRoot.left.sum - oldRoot.left.sum;
        if (leftSum >= v) {
            return queryFirstGE(oldRoot.left, newRoot.left, ls, mid, v);
        } else {
            return queryFirstGE(oldRoot.right, newRoot.right, mid + 1, rs, v - leftSum);
        }
    }
}
