package template.segtree.finegrind;

/**
 * tree[i]表示i在数组中出现的次数。可单点更新并查询数组的前k大之和。
 *
 * @Author Create by CROW
 * @Date 2023/6/18
 */
class TopKSumTree {

    static class Node {
        Node left;
        Node right;
        long sum;
        long cnt;
        int ls, rs;
    }

    int maxN;
    Node root;

    public TopKSumTree(int maxN) {
        this.maxN = maxN;
        this.root = new Node();
        root.ls = 0;
        root.rs = maxN;
    }

    long trim(long v) {
        //可以加上取余
        return v;
    }

    //增加cnt个元素i
    public void add(int i, long cnt) {
        add(root, i, cnt);
    }

    private void add(Node node, int i, long cnt) {
        int ls = node.ls, rs = node.rs;
        if (i < 0 || i > maxN) {
            throw new IllegalArgumentException("index:" + i);
        }
        if (rs == ls && ls == i) {
            node.sum = trim(node.sum + cnt * (long) i);
            node.cnt = trim(node.cnt + cnt);
            return;
        }

        pushDown(node, ls, rs);
        int mid = ls + rs >> 1;
        //左子树[ls,mid]
        //右子树[mid+1,rs]
        if (i <= mid) {
            add(node.left, i, cnt);
        }
        if (i >= mid + 1) {
            add(node.right, i, cnt);
        }
        node.sum = trim(node.sum + cnt * (long) i);
        node.cnt = trim(node.cnt + cnt);
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
    }

    //求[l,r]上元素之和
    public long sum(Node node, int l, int r) {
        int ls = node.ls, rs = node.rs;
        if (r < l) {
            return 0;
        }
        if (r < 0) {
            throw new IllegalArgumentException("index:" + r);
        }
        if (l <= ls && rs <= r) {
            return node.sum;
        }
        pushDown(node, ls, rs);
        int mid = ls + rs >> 1;
        long res = 0;
        if (l <= mid) {
            res += sum(node.left, l, r);
            res = trim(res);
        }
        if (r >= mid + 1) {
            res += sum(node.right, l, r);
            res = trim(res);
        }
        return trim(res);
    }

    //求[l,r]元素个数
    public long cnt(Node node, int l, int r) {
        int ls = node.ls, rs = node.rs;
        if (r < l) {
            return 0;
        }
        if (r < 0) {
            throw new IllegalArgumentException("index:" + r);
        }
        if (l <= ls && rs <= r) {
            return node.cnt;
        }
        pushDown(node, ls, rs);
        int mid = ls + rs >> 1;
        long res = 0;
        if (l <= mid) {
            res += sum(node.left, l, r);
            res = trim(res);
        }
        if (r >= mid + 1) {
            res += sum(node.right, l, r);
            res = trim(res);
        }
        return trim(res);
    }

    //从最大下标开始向左找到cnt个值并返回sum
    public long sumLeftwardWithCnt(Node node, long cnt) {
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
            res = trim(res + node.right.sum + sumLeftwardWithCnt(node.left, cnt - node.right.cnt));
        } else {
            res = sumLeftwardWithCnt(node.right, cnt);
        }
        return res;
    }
}
