package main.lc;

import java.util.*;

class ScalarSegTree {

    static class Node {
        Node left;
        Node right;
        long sum = INITIAL;
        long lazy = INITIAL;
        int ls, rs;//debug用
    }

    int maxN;
    Node root;

    public ScalarSegTree(int maxN) {
        this.maxN = maxN;
        this.root = new Node();
        root.ls = 0;
        root.rs = maxN;
    }

    private static final long INITIAL = 0;

    /**
     * 区间统计值合并
     * left,right: 左右子区间统计值
     * lazyVal: 未下传子树的懒更新
     * coverLen: lazyVal影响的区间长度
     */
    public long reduce(long left, long right, long coverLen, long lazyVal) {
        return left + right + coverLen * lazyVal;
    }

    /**
     * O(n)设置初始值，但未必时间更短，因为可能不需要每个节点都创建出来
     */
    void build(long[] vals) {
        build(root, vals, 0, maxN);
    }

    private void build(Node node, long[] vals, int ls, int rs) {
        if (ls==rs) {
            node.sum = vals[ls];
            return;
        }
        pushDown(node, ls, rs);
        int mid = ls + rs >> 1;
        build(node.left,vals, ls, mid);
        build(node.right,vals,mid + 1, rs);
        node.sum = reduce(node.left.sum, node.right.sum, 0, INITIAL);
    }

    public void add(int l, int r, long val) {
        add(root, l, r, val, 0, maxN);
    }

    /**
     * 当前Node的范围: [ls,rs]
     */
    private void add(Node node, int l, int r, long val, int ls, int rs) {
        if (l < 0 || r > maxN || r < 0) throw new IllegalArgumentException("index:" + l + "," + r);
        if (l <= ls && rs <= r) {
            //[l,r]覆盖了当前子树
            node.sum = reduce(node.sum, INITIAL, (rs - ls + 1), val);
            node.lazy = reduce(node.lazy, val, 0, INITIAL);
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
        int cover=Math.min(r,rs)-Math.max(l,ls)+1;
        node.sum = reduce(node.left.sum, node.right.sum, cover, node.lazy);
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

    public long sum(int l, int r) {
        return sum(root, l, r, 0, maxN);
    }

    private long sum(Node node, int l, int r, int ls, int rs) {
        if (l < 0 || r > maxN || r < 0) throw new IllegalArgumentException("index:" + l + "," + r);
        if (l <= ls && rs <= r) {
            return node.sum;
        }
        pushDown(node, ls, rs);
        int mid = ls + rs >> 1;
        int cover=Math.min(r,rs)-Math.max(l,ls)+1;
        long left = INITIAL;
        long right = INITIAL;
        if (l <= mid) {
            left = sum(node.left, l, r, ls, mid);
        }
        if (r >= mid + 1) {
            right = sum(node.right, l, r, mid + 1, rs);
        }
        return reduce(left, right, cover, node.lazy);
    }
}
class Solution {
    public int[] fullBloomFlowers(int[][] flowers, int[] persons) {
        ScalarSegTree tree=new ScalarSegTree((int) 1e9);
        for (int[] a : flowers) {
            tree.add(a[0],a[1],1);
        }
        int[] ans=new int[persons.length];
        for (int i = 0; i < persons.length; i++) {
            ans[i]= (int) tree.sum(persons[i],persons[i]);
        }
        return ans;
    }

    public static void main(String[] args) {
//        int score = new Solution().maximumScore(Arrays.asList(8, 3, 9, 3, 8), 2);
//        System.out.println(score);

//        String input = scanner.nextLine();
//        long r = new A().new Solution().getSchemeCount(3, 3, LCUtils.parseStringAr("[\"..R\",\"..B\",\"?R?\"]"));
//        System.out.println(r);
    }
}
