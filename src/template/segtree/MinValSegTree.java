package template.segtree;


public class MinValSegTree {

    static class Node {
        Node left;
        Node right;
        long lazyAdd;
        long sum;
        int ls, rs;//debug用
        int minId;//值最小且index最小的index
        long minVal;
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
        return r;
    }

    /**
     * 添加val到当前node的整个区间，不用下发
     */
    void addLazy(Node node, int ls, int rs, long val) {
        node.lazyAdd += val;
        node.sum += (rs - ls + 1) * val;
        node.minVal += val;
    }

    /**
     * child修改后，更新统计到当前node，node中可以有多个需要reduce的字段. 此时Node的lazy值已下发。
     */
    void reduce(Node node, int ls, int rs) {
        node.sum = node.left.sum + node.right.sum;
        if (node.right.minVal < node.left.minVal) {
            node.minId = node.right.minId;
            node.minVal = node.right.minVal;
        } else {
            node.minId = node.left.minId;
            node.minVal = node.left.minVal;
        }
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
            addLazy(node, ls, rs, val);
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
        if (node.lazyAdd != 0) {
            //lazy是当前节点已经算了，但是还没下发child的
            addLazy(node.left, ls, mid, node.lazyAdd);
            addLazy(node.right, mid + 1, rs, node.lazyAdd);
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

    //查询最小值
    public long queryMinVal(int l, int r) {
        tmpMinId = -1;
        tmpMinVal = Long.MAX_VALUE;
        queryMinId(root, l, r, 0, maxN);
        return tmpMinVal;
    }

    //查询最小值的下标，如果有多个则取下标最小的
    public int queryMinId(int l, int r) {
        tmpMinId = -1;
        tmpMinVal = Long.MAX_VALUE;
        queryMinId(root, l, r, 0, maxN);
        return tmpMinId;
    }

    int tmpMinId;
    long tmpMinVal;

    private void queryMinId(Node node, int l, int r, int ls, int rs) {
        if (l <= ls && rs <= r) {
            if (tmpMinId == -1 || node.minVal < tmpMinVal || node.minVal == tmpMinVal && node.minId < tmpMinId) {
                tmpMinId = node.minId;
                tmpMinVal = node.minVal;
            }
            return;
        }
        pushDown(node, ls, rs);
        int mid = ls + rs >> 1;
        if (l <= mid) {
            queryMinId(node.left, l, r, ls, mid);
        }
        if (r >= mid + 1) {
            queryMinId(node.right, l, r, mid + 1, rs);
        }
    }

}




