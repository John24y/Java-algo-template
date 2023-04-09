/**
 * @Author Create by jiaxiaozheng
 * @Date 2023/2/19
 */
public class SegTreeTemplate {

    class Node {
        Node left;
        Node right;
        long lazyAdd;
        long sum;
        int ls, rs;//debug用
    }

    int maxN;
    Node root;

    public SegTreeTemplate(int maxN) {
        this.maxN = maxN;
        this.root = new Node();
        root.ls = 0;
        root.rs = maxN;
    }

    /**
     * child修改后，更新统计到当前node，node中可以有多个需要reduce的字段. 此时Node的lazy值已下发。
     */
    void reduce(Node node, int ls, int rs) {
        node.sum = node.left.sum + node.right.sum;
    }

    public void add(int l, int r, long val) {
        add(root, l, r, val, 0, maxN);
    }

    /**
     * 当前Node的范围: [ls,rs]
     */
    private void add(Node node, int l, int r, long val, int ls, int rs) {
        if (l < 0 || r > maxN) {
            throw new IllegalArgumentException();
        }
        if (l <= ls && rs <= r) {
            //[l,r]覆盖了当前子树
            //只要有外部对当前节点的更新操作, 就需要重新计算当前节点的sum
            node.lazyAdd += val;
            node.sum += (rs - ls + 1) * val;
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
        reduce(node, ls, rs);
    }

    /**
     * @param node
     * @param ls
     * @param rs
     */
    //对孩子节点的递归调用时要先下传懒标记
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
        if (node.lazyAdd!=0) {
            add(node.left, ls, mid, node.lazyAdd, ls, mid);
            add(node.right, mid + 1, rs, node.lazyAdd, mid + 1, rs);
            node.lazyAdd = 0;
        }
    }

    public long sum(int l, int r) {
        return sum(root, l, r, 0, maxN);
    }

    private long sum(Node node, int l, int r, int ls, int rs) {
        if (l < 0 || r > maxN) {
            throw new IllegalArgumentException();
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
        return res;
    }

}
