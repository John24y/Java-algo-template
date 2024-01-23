package main.lc;

import java.util.Arrays;

class SumSegTree {
    private static final long INITIAL = 0;

    static class Node {
        Node left;
        Node right;
        long sum = INITIAL;
        long lazy = INITIAL;
        int ls, rs;//debug用
    }

    int maxN;
    Node root;

    public SumSegTree(int maxN) {
        this.maxN = maxN;
        this.root = new Node();
        root.ls = 0;
        root.rs = maxN;
    }

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
        if (ls == rs) {
            node.sum = vals[ls];
            return;
        }
        pushDown(node, ls, rs);
        int mid = ls + rs >> 1;
        build(node.left, vals, ls, mid);
        build(node.right, vals, mid + 1, rs);
        node.sum = reduce(node.left.sum, node.right.sum, 0, INITIAL);
    }

    public void add(int l, int r, long val) {
        if (r<l) return;
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
        //node.sum = reduce(node.left.sum, node.right.sum, rs-ls+1, node.lazy);// 也ok
        node.sum = reduce(node.sum, INITIAL, Math.min(r, rs) - Math.max(l, ls) + 1, val);
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
        int cover = Math.min(r, rs) - Math.max(l, ls) + 1;
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

    public long[] countOfPairs(int n, int x, int y) {
        SumSegTree tree = new SumSegTree(n);
        if (x > y) {
            int t = x;
            x = y;
            y = t;
        }
        for (int i = 1; i < n; i++) {
            if (x == y) {
                tree.add(1, n - i, 1);
                continue;
            }
            if (i <= x) {
                tree.add(1, n - i - (y-x-1), 1); //去掉 [(x,y]
                tree.add(x-i+1,x-i+1,-1);
                int j = x-i;// (y,n]
                tree.add(j+1, j+ ((y - x) / 2), 2);
                if ((y - x) % 2 == 1) {
                    tree.add(j + (y - x + 1) / 2, j + (y - x + 1) / 2, 1);
                }
            } else if (i < y) {
                int jump = i-x+1;
                if (jump>=y-i) {
                    tree.add(1, n - i, 1);
                } else {
                    int js = Math.min(y-i-1,((y-i) - jump) / 2);
                    js = Math.max(0,js);
                    tree.add(1, y-i-1-js, 1);
                    tree.add(1+jump,js+jump,1);
                    tree.add(jump,n-y+jump,1);//[y,n]
                }
            } else {
                tree.add(1, n - i, 1);
            }
        }
        long[] res=new long[n];
        for (int i = 1; i < n; i++) {
            res[i-1]=tree.sum(i,i)*2;
        }
        return res;
    }

    public static void main(String[] args) {
//        long[] longs = new Solution().countOfPairs(3, 1, 3);
//        long[] longs = new Solution().countOfPairs(5, 2, 4);
//        long[] longs = new Solution().countOfPairs(6, 1, 5);
        long[] longs = new Solution().countOfPairs(7, 1, 7);
        System.out.println(Arrays.toString(longs));
//        long l = new Solution().minimumCost(new int[]{1, 3, 2, 6, 4, 2}, 3, 3);
//        long l = new Solution().minimumCost("abcdefgh", "acdeeghh",
//                new String[]{"bcd","fgh","thh"}, new String[]{"cde","thh","ghh"},
//                new int[]{1,3,5});
//        long l = new Solution().minimumCost("abcd", "acbe",
//                new String[]{"a", "b", "c", "c", "e", "d"}, new String[]{"b", "c", "b", "e", "b", "e"},
//                new int[]{2, 5, 5, 1, 2, 20});
//        long l = new Solution().minimumCost("abcdefgh", "acdeeghh",
//                new String[]{"bcd","fgh","thh"}, new String[]{"cde","thh","ghh"},
//                new int[]{1,3,5});
//        long l = new Solution().minimumCost("abcdefgh", "addddddd",
//                new String[]{"bcd","defgh"}, new String[]{"ddd","ddddd"},
//                new int[]{100,1578});
    }
}