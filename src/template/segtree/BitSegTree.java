package template.segtree;

/**
 * 每个下标只能储存0或1。支持区间赋值为0或1、区间翻转01、区间求和
 *
 * @Author Create by jiaxiaozheng
 * @Date 2023/3/12
 */
public class BitSegTree {

    class Node {
        Node left;
        Node right;
        OP lazy;
        long sum;
        int ls, rs;//debug用
    }

    enum OP {
        SET,
        UNSET,
        INV
    }

    int maxN;
    Node root;

    public BitSegTree(int maxN) {
        this.maxN = maxN;
        this.root = new Node();
        root.ls = 0;
        root.rs = maxN;
    }

    public void set(int l, int r) {
        add(root, l, r, 0, maxN, OP.SET);
    }

    public void unset(int l, int r) {
        add(root, l, r, 0, maxN, OP.UNSET);
    }

    public void invert(int l, int r) {
        add(root, l, r, 0, maxN, OP.INV);
    }

    /**
     * 当前Node的范围: [ls,rs]
     */
    private void add(Node node, int l, int r, int ls, int rs, OP op) {
        if (l < 0 || r > maxN) {
            throw new IllegalArgumentException();
        }
        if (l <= ls && rs <= r) {
            //每个节点的sum都是准确的，所以不能只更新lazy，sum也要算
            if (op == OP.INV) {
                if (node.lazy == OP.SET) {
                    op = OP.UNSET;
                } else if (node.lazy == OP.UNSET) {
                    op = OP.SET;
                }
            }
            node.lazy = (node.lazy == OP.INV && op == OP.INV) ? null : op;
            if (op == OP.SET) {
                node.sum = (rs - ls + 1);
            } else if (op == OP.UNSET) {
                node.sum = 0;
            } else if (op == OP.INV) {
                node.sum = (rs - ls + 1) - node.sum;
            }
            return;
        }

        pushDown(node, ls, rs);
        int mid = ls + rs >> 1;
        //左子树[ls,mid]
        //右子树[mid+1,rs]
        if (l <= mid) {
            add(node.left, l, r, ls, mid, op);
        }
        if (r >= mid + 1) {
            add(node.right, l, r, mid + 1, rs, op);
        }
        node.sum = node.left.sum + node.right.sum;
    }

    //对孩子节点的递归调用要先下传懒标记
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
        if (node.lazy != null) {
            add(node.left, ls, mid, ls, mid, node.lazy);
            add(node.right, mid + 1, rs, mid + 1, rs, node.lazy);
            node.lazy = null;
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

    /**
     * 返回使 [left,r] 包含count个1的最小的r, 不存在则返回-1
     */
    public int rightBound1(long count) {
        return rightBound(count, 1, root, 0, maxN);
    }

    /**
     * 返回使 [0,r] 包含count个0的最小的r, 不存在则返回-1
     */
    public int rightBound0(long count) {
        return rightBound(count, 0, root, 0, maxN);
    }

    private int rightBound(long count, int target, Node node, int ls, int rs) {
        if (count == 0) {
            return ls - 1;
        }
        if (ls == rs) {
            long c = target == 1 ? (node.sum) : (rs - ls + 1 - node.sum);
            return c == count ? ls : -1;
        }
        pushDown(node, ls, rs);
        int mid = ls + rs >> 1;
        long leftCount = target == 1 ? (node.left.sum) : (mid - ls + 1 - node.left.sum);
        if (leftCount >= count) {
            return rightBound(count, target, node.left, ls, mid);
        } else {
            return rightBound(count - leftCount, target, node.right, mid + 1, rs);
        }
    }

}
