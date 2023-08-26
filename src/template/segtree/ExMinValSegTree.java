package template.segtree;

/**
 * - 支持查询区间上第一个<x的值，可以从左边找，也可以从右边找
 *
 * @Author Create by CROW
 * @Date 2023/7/28
 */
public class ExMinValSegTree extends MinValSegTree {

    public ExMinValSegTree(int maxN) {
        super(maxN);
    }

    /**
     * 查询[l,r]上第一个 < x的下标 (注意不含等于)
     *
     * @param fromLeft true 找左边第一个，false 找右边第一个
     * @return -1 表示找不到
     */
    public int queryFirstLess(int l, int r, int x, boolean fromLeft) {
        int[] order = fromLeft ? new int[]{0, 1} : new int[]{1, 0};
        return queryFirstLess(root, l, r, x, order, 0, maxN);
    }

    private int queryFirstLess(Node node, int l, int r, int x, int[] order, int ls, int rs) {
        int mid = ls + rs >> 1;
        if (node.minVal >= x) {
            return -1;
        }
        if (ls == rs) {
            return ls;
        }
        pushDown(node, ls, rs);

        for (int ord : order) {
            if (ord == 0 && l <= mid) {
                int ret = queryFirstLess(node.left, l, r, x, order, ls, mid);
                if (ret != -1) {
                    return ret;
                }
            }
            if (ord == 1 && r >= mid + 1) {
                int ret = queryFirstLess(node.right, l, r, x, order, mid + 1, rs);
                if (ret != -1) {
                    return ret;
                }
            }
        }
        return -1;
    }
}
