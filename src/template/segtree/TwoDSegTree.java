package template.segtree;

import java.util.ArrayList;
import java.util.List;

/**
 * 树状数组套线段树。可以求动态数组的区间上第k小
 */
class TwoDSegTree {
    SingleApplySegTree[] trees;
    int maxX, maxY;

    class VirtualNode {
        List<SingleApplySegTree.Node> listPos=new ArrayList<>();
        List<SingleApplySegTree.Node> listNeg=new ArrayList<>();
        long sum() {
            long res=0;
            for (SingleApplySegTree.Node node : listPos) res+=node.sum;
            for (SingleApplySegTree.Node node : listNeg) res-=node.sum;
            return res;
        }
        void left() {
            for (int k=0;k<2;k++){
                List<SingleApplySegTree.Node> list = k==0?listPos:listNeg;
                int j=0;
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).left!=null) {
                        list.set(j, list.get(i).left);
                        j++;
                    }
                }
                while (list.size()>j) list.remove(list.size()-1);
            }
        }
        void right() {
            for (int k=0;k<2;k++){
                List<SingleApplySegTree.Node> list = k==0?listPos:listNeg;
                int j=0;
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).right!=null) {
                        list.set(j, list.get(i).right);
                        j++;
                    }
                }
                while (list.size()>j) list.remove(list.size()-1);
            }
        }
    }

    public TwoDSegTree(int maxX, int maxY) {
        trees=new SingleApplySegTree[maxX+2];
        this.maxX=maxX;
        this.maxY=maxY;
        for (int i = 1; i < trees.length; i++) {
            trees[i] = new SingleApplySegTree(maxY);
        }
    }

    public void add(int x, int y, int val) {
        for(x++;x<trees.length;x+=x&-x) {
            trees[x].add(y, val);
        }
    }

    private VirtualNode virtualNode(int xL, int xR) {
        VirtualNode node = new VirtualNode();
        for (; xL > 0; xL-=xL&-xL) {
            node.listNeg.add(trees[xL].root);
        }
        for (xR++; xR > 0; xR-=xR&-xR) {
            node.listPos.add(trees[xR].root);
        }
        return node;
    }

    //[xL,xR]组成线段树上的第k小, 不存在返回-1
    public int rank(int xL, int xR, int k) {
        VirtualNode node = virtualNode(xL, xR);
        return rank(node, 0, maxY, k);
    }

    private int rank(VirtualNode node, int ls, int rs, int k) {
        if (node.sum() < k) {
            return -1;
        }
        if (ls == rs) {
            return ls;
        }
        int mid = ls + rs >> 1;
        long leftSum=0;
        for (SingleApplySegTree.Node n : node.listPos) {
            if (n.left!=null)leftSum+=n.left.sum;
        }
        for (SingleApplySegTree.Node n : node.listNeg) {
            if (n.left!=null)leftSum-=n.left.sum;
        }
        if (leftSum >= k) {
            node.left();
            return rank(node, ls, mid, k);
        } else {
            node.right();
            return rank(node, mid + 1, rs, (int) (k - leftSum));
        }
    }
}
