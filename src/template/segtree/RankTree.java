package template.segtree;

/**
 * tree[i]表示i在数组中出现的次数，数组可动态修改的情况下求数组的第k小
 *
 * @Author Create by CROW
 * @Date 2023/2/19
 */
class RankTree extends SegTreeTemplate {

    public RankTree(int maxN) {
        super(maxN);
    }

    /**
     * 假设tree[i]表示i在数组中出现的次数，求数组的第k小，如果不存在返回-1
     */
    public long kSmallest(long k) {
        return queryFirstGE(k);
    }

    /**
     * 查找使sum([0,r])>=v最小的r，如果不存在返回-1
     */
    public long queryFirstGE(long v) {
        return queryFirstGE(root, 0, maxN, v);
    }

    private long queryFirstGE(Node node, int ls, int rs, long v) {
        if (node.sum < v) {
            return -1;
        }
        if (ls == rs) {
            return ls;
        }
        int mid = ls + rs >> 1;
        pushDown(node, ls, rs);
        if (node.left.sum >= v) {
            return queryFirstGE(node.left, ls, mid, v);
        } else {
            return queryFirstGE(node.right, mid + 1, rs, v - node.left.sum);
        }
    }
}
