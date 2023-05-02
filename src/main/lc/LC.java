package main.lc;

import lc.LCUtils;

import java.util.*;

/**
 * @Author Create by jiaxiaozheng
 * @Date 2023/4/22
 */
public class LC {
class SumSegTree {

    class Node {
        Node left;
        Node right;
        long sum;
        long lazy;
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

    long trim(long v) {
        //可以加上取余
        return v;
    }

    public void add(int l, int r, long val) {
        add(root, l, r, val, 0, maxN);
    }

    /**
     * 当前Node的范围: [ls,rs]
     */
    private void add(Node node, int l, int r, long val, int ls, int rs) {
        if (r<l){
            return;
        }
        if (r<0){
            throw new IllegalArgumentException("index:"+r);
        }
        if (l <= ls && rs <= r) {
            //[l,r]覆盖了当前子树
            node.sum = trim(node.sum+(rs - ls + 1) * val);
            node.lazy = trim(node.lazy + val);
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
        node.sum = trim(node.sum + val * cover);
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
        //pushDown的时候并没有去下发lazy值,因为对于求sum来说,可以在query的时候把整条路径上的lazy都算上
    }

    public long sum(int l, int r) {
        return sum(root, l, r, 0, maxN);
    }

    private long sum(Node node, int l, int r, int ls, int rs) {
        if (r<l) {
            return 0;
        }
        if (r<0){
            throw new IllegalArgumentException("index:"+r);
        }
        if (l <= ls && rs <= r) {
            return node.sum;
        }
        pushDown(node, ls, rs);
        int mid = ls + rs >> 1;
        int cover=Math.min(r,rs)-Math.max(l,ls)+1;
        long res = trim(cover*node.lazy);
        if (l <= mid) {
            res += sum(node.left, l, r, ls, mid);
        }
        if (r >= mid + 1) {
            res += sum(node.right, l, r, mid + 1, rs);
        }
        return trim(res);
    }
}

class Solution {
    public long countOperationsToEmptyArray(int[] nums) {
        int n = nums.length;
        SumSegTree tree=new SumSegTree(n-1);
        List<int[]> list=new ArrayList<>();
        for (int i = 0; i < n; i++) {
            tree.add(i,i,1);
            list.add(new int[]{nums[i],i});
        }
        Collections.sort(list, Comparator.comparingInt(x->x[0]));
        int last=0;
        int res=0;
        for (int[] ints : list) {
            int e=ints[0],i=ints[1];
            tree.add(i,i,-1);
            if (i>=last) {
                res+=tree.sum(last,i);
            } else {
                res+=tree.sum(0,n-1)-tree.sum(i,last);
            }
            last=i;
        }
        return res+n;
    }
}


    public static void main(String[] args) {
//        long l = new LC().new Solution().countOperationsToEmptyArray(LCUtils.parseIntAr("[4,3,2,1]"));
        long l = new LC().new Solution().countOperationsToEmptyArray(LCUtils.parseIntAr("[1,4,3,2]"));
        System.out.println(l);
    }

}


