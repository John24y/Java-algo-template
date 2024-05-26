package main.lc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

class SimpleSegTree {

    static class Node {
        Node left;
        Node right;
        long ls, rs;
        long[][] dp=new long[2][2];
    }

    int maxN;
    Node root;

    public SimpleSegTree(int maxN) {
        this.maxN = maxN;
        this.root = createNode(0, maxN);
    }

    Node createNode(int ls, int rs) {
        Node node = new Node();
        node.ls = ls;
        node.rs = rs;
        return node;
    }

    public void build(int[] vals) {
        build(vals, root, 0, maxN);
    }

    private void build(int[] vals, Node node, int ls, int rs) {
        if (ls == rs) {
            if (ls >= vals.length) return;
            apply(node, 0, vals[ls], ls, rs);
            return;
        }
        pushDown(node, ls, rs);
        int mid = ls + rs >> 1;
        build(vals, node.left, ls, mid);
        build(vals, node.right, mid + 1, rs);
        reduce(node, node.left, node.right, ls, rs);
    }

    /**
     * 修改单点
     */
    public void apply(Node node, int type, int val, int ls, int rs) {
        node.dp[1][1] = val;
        node.dp[0][0] = 0;
        node.dp[0][1] = Long.MIN_VALUE;
        node.dp[1][0] = Long.MIN_VALUE;
    }

    public void reduce(Node node, Node left, Node right, int ls, int rs) {
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                node.dp[i][j] = Long.MIN_VALUE;
            }
        }
        for (int ll = 0; ll < 2; ll++) {
            for (int lr = 0; lr < 2; lr++) {
                for (int rl = 0; rl < 2; rl++) {
                    for (int rr = 0; rr < 2; rr++) {
                        long v=Long.MIN_VALUE;
                        if (lr==0||rl==0) {
                            long v1=left.dp[ll][lr];
                            long v2=right.dp[rl][rr];
                            if (v1!=Long.MIN_VALUE&&v2!=Long.MIN_VALUE){
                                v=v1+v2;
                            }
                        }
                        node.dp[ll][rr]=Math.max(v,node.dp[ll][rr]);
                    }
                }
            }
        }
    }

    public void add(int i, int val) {
        add(root, i, 0, val, 0, maxN);
    }

    /**
     * 当前Node的范围: [ls,rs]
     */
    private void add(Node node, int i, int type, int val, int ls, int rs) {
        if (ls == rs) {
            apply(node, type, val, ls, rs);
            return;
        }

        pushDown(node, ls, rs);
        int mid = ls + rs >> 1;
        //左子树[ls,mid]
        //右子树[mid+1,rs]
        if (i <= mid) {
            add(node.left, i, type, val, ls, mid);
        } else if (i >= mid + 1) {
            add(node.right, i, type, val, mid + 1, rs);
        }
        reduce(node, node.left, node.right, ls, rs);
    }

    void pushDown(Node node, int ls, int rs) {
        int mid = ls + rs >> 1;
        if (node.left == null) {
            node.left = createNode(ls, mid);
        }
        if (node.right == null) {
            node.right = createNode(mid + 1, rs);
        }
    }

    Node query(Node node, int l, int r, int ls, int rs) {
        if (l < 0 || r > maxN) {
            throw new IllegalArgumentException();
        }
        if (l <= ls && rs <= r) {
            return node;
        }
        pushDown(node, ls, rs);
        int mid = ls + rs >> 1;
        Node res = createNode(Math.max(ls, l), Math.min(rs, r)), leftRes = null, rightRes = null;
        if (l <= mid) {
            leftRes = query(node.left, l, r, ls, mid);
        }
        if (r >= mid + 1) {
            rightRes = query(node.right, l, r, mid + 1, rs);
        }
        if (leftRes == null) return rightRes;
        if (rightRes == null) return leftRes;
        reduce(res, leftRes, rightRes, ls, rs);
        return res;
    }
}

class Solution {
    public int maximumSumSubsequence(int[] nums, int[][] qs) {
        SimpleSegTree tree = new SimpleSegTree(nums.length-1);
        tree.build(nums);
        long res=0;
        int M = (int) (1e9+7);
        for (int[] q : qs) {
            tree.add(q[0],q[1]);
            SimpleSegTree.Node node = tree.query(tree.root, 0, nums.length - 1, 0, nums.length - 1);
            long mx = Long.MIN_VALUE;
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 2; j++) {
                    mx = Math.max(mx, node.dp[i][j]);
                }
            }
            res+=mx;
            res%=M;
        }
        return (int) res;
    }
}