/**
 * 区间赋值线段树，但只能覆盖赋值，不能累加。且赋值是相对下标的偏移。
 */
public class OffsetSegTree {

    class Node {
        OffsetSegTree.Node left;
        OffsetSegTree.Node right;
        boolean hasVal;
        long offset;
        int ls, rs;//debug用
    }

    int maxN;
    OffsetSegTree.Node root;

    public OffsetSegTree(int maxN) {
        this.maxN = maxN;
        this.root = new OffsetSegTree.Node();
        root.ls = 0;
        root.rs = maxN;
    }

    public void add(int l, int r, long val) {
        add(root, l, r, val, 0, maxN);
    }

    /**
     * 当前Node的范围: [ls,rs]
     */
    private void add(OffsetSegTree.Node node, int l, int r, long offset, int ls, int rs) {
        if (l < 0 || r > maxN) {
            throw new IllegalArgumentException();
        }
        if (l <= ls && rs <= r) {
            //[l,r]覆盖了当前子树
            node.hasVal=true;
            node.offset = offset;
            return;
        }

        pushDown(node, ls, rs);
        int mid = ls + rs >> 1;
        //左子树[ls,mid]
        //右子树[mid+1,rs]
        if (l <= mid) {
            add(node.left, l, r, offset, ls, mid);
        }
        if (r >= mid + 1) {
            add(node.right, l, r, offset, mid + 1, rs);
        }
    }

    //对孩子节点的递归调用时要先下传懒标记
    void pushDown(OffsetSegTree.Node node, int ls, int rs) {
        int mid = ls + rs >> 1;
        if (node.left == null) {
            node.left = new OffsetSegTree.Node();
            node.left.ls = ls;
            node.left.rs = mid;
        }
        if (node.right == null) {
            node.right = new OffsetSegTree.Node();
            node.right.ls = mid + 1;
            node.right.rs = rs;
        }
        if (node.hasVal) {
            add(node.left, ls, mid, node.offset, ls, mid);
            add(node.right, mid + 1, rs, node.offset, mid + 1, rs);
            node.hasVal = false;
        }
    }

    public long offset(int i) {
        return offset(root, i,0, maxN);
    }

    private long offset(OffsetSegTree.Node node, int i, int ls, int rs) {
        if (i < 0 || i > maxN) {
            throw new IllegalArgumentException();
        }
        if (node.hasVal) {
            return node.offset;
        }
        if (ls == rs) {
            return 0;
        }
        pushDown(node, ls, rs);
        int mid = ls + rs >> 1;
        if (i <= mid) {
            return offset(node.left, i, ls, mid);
        } else {
            return offset(node.right, i, mid+1, rs);
        }
    }

    //找第一个>=val的下标
    public long bisectLeft(long val, long maxR) {
        long lo=1,hi=maxR;
        while (lo<=hi) {
            long mid=lo+hi>>1;
            if (offset((int) mid)+mid>=val) {
                hi=mid-1;
            } else {
                lo=mid+1;
            }
        }
        return lo;
    }
}
