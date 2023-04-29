package main.lc;

import lc.LCUtils;

/**
 * @Author Create by jiaxiaozheng
 * @Date 2023/4/22
 */
public class LC {
class SegTreeTemplate {

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
        int mid=ls+rs>>1;
        pushDown(node, ls, rs);
        if (node.left.sum >= v) {
            return queryFirstGE(node.left, ls, mid, v);
        } else {
            return queryFirstGE(node.right, mid+1, rs, v-node.left.sum);
        }
    }
}

    class Solution {
        public int[] getSubarrayBeauty(int[] nums, int k, int x) {
            RankTree tree = new RankTree(100);
            for (int i = 0; i < k; i++) {
                tree.add(nums[i]+50,nums[i]+50, 1);
            }
            int n= nums.length;
            int[] res=new int[n-k+1];
            res[0]= Math.min(0,(int) tree.kSmallest(x)-50);
            for (int i = k; i < n; i++) {
                tree.add(nums[i]+50,nums[i]+50, 1);
                tree.add(nums[i-k]+50,nums[i-k]+50, -1);
                res[i-k+1]= Math.min(0,(int) tree.kSmallest(x)-50);;
            }
            return res;
        }
    }

    public static void main(String[] args) {
        new LC().new Solution().getSubarrayBeauty(LCUtils.parseIntAr("[1,-1,-3,-2,3]"), 3, 2);
    }

}


