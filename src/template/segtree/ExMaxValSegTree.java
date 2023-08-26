package template.segtree;

/**
 * - 支持查询区间上第一个>x的值，可以从左边找，也可以从右边找
 * - 支持作为排序表，对前k大的数-1，并且保持有序
 *
 * @Author Create by CROW
 * @Date 2023/7/28
 */
public class ExMaxValSegTree extends MaxValSegTree {

    public ExMaxValSegTree(int maxN) {
        super(maxN);
    }

    /**
     * 查询[l,r]上第一个>x的下标
     *
     * @param fromLeft true 找左边第一个，false 找右边第一个
     * @return -1 表示找不到
     */
    public int queryFirstGreater(int l, int r, int x, boolean fromLeft) {
        int[] order = fromLeft ? new int[]{0, 1} : new int[]{1, 0};
        return queryFirstGreater(root, l, r, x, order, 0, maxN);
    }

    private int queryFirstGreater(Node node, int l, int r, int x, int[] order, int ls, int rs) {
        int mid = ls + rs >> 1;
        if (node.maxVal <= x) {
            return -1;
        }
        if (ls == rs) {
            return ls;
        }
        pushDown(node, ls, rs);

        for (int ord : order) {
            if (ord == 0 && l <= mid) {
                int ret = queryFirstGreater(node.left, l, r, x, order, ls, mid);
                if (ret != -1) {
                    return ret;
                }
            }
            if (ord == 1 && r >= mid + 1) {
                int ret = queryFirstGreater(node.right, l, r, x, order, mid + 1, rs);
                if (ret != -1) {
                    return ret;
                }
            }
        }
        return -1;
    }

    /**
     * 前k大的数-1. 下标从1开始，下标0不存储元素。
     * 如果第k大的数<=0，则返回false，并且不操作。
     * 前提是整颗树是从大到小排序号的。
     */
    public boolean decTopK(int k) {
        long v = sum(k, k);
        if (v<=0) {
            return false;
        }
        int r2=queryFirstGreater(1,maxN, (int) v-1,false);
        int r1=queryFirstGreater(1,maxN, (int) v,false);
        if (r1==-1) r1=0;
        add(0,r1, -1);
        add(r2-(k-r1)+1,r2, -1);
        return true;
    }
}
