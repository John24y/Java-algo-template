import java.util.ArrayList;
import java.util.List;

/**
 * @Author Create by jiaxiaozheng
 * @Date 2022/9/5
 */
public class LCP05 {

    static class SegTree {

        static class Node {
            Node left;
            Node right;
            long sum;
            long lazy;
            int ls,rs;//debug用
        }

        int maxN;
        Node root;

        public SegTree(int maxN) {
            this.maxN = maxN;
            this.root = new Node();
            root.ls=0;
            root.rs=maxN;
        }

        public void add(int l, int r, long val) {
            add(root, l, r, val, 0, maxN);
        }

        /**
         * 当前Node的范围: [ls,rs]
         */
        private void add(Node node, int l, int r, long val, int ls, int rs) {
            if (l <= ls && rs <= r) {
                //[l,r]覆盖了当前子树
                node.sum += (rs-ls+1)*val;
                node.lazy += val;
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
            node.sum = node.left.sum + node.right.sum;
        }

        void pushDown(Node node, int ls, int rs) {
            int mid = ls + rs >> 1;
            if (node.left==null) {
                node.left=new Node();
                node.left.ls=ls;
                node.left.rs=mid;
            }
            if (node.right==null) {
                node.right=new Node();
                node.right.ls=mid+1;
                node.right.rs=rs;
            }
            //lazy是当前节点已经算了，但是还没下发child的
            node.left.lazy += node.lazy;
            node.left.sum += (mid - ls + 1) * node.lazy;
            node.right.lazy += node.lazy;
            node.right.sum += (rs - mid) * node.lazy;
            node.lazy = 0;
            //节点的本身没有值，它的sum是child的sum
            node.sum = node.left.sum + node.right.sum;
        }

        public long query(int l, int r) {
            return query(root, l, r, 0, maxN);
        }

        private long query(Node node, int l, int r, int ls, int rs) {
            if (l <= ls && rs <= r) {
                return node.sum;
            }
            pushDown(node, ls, rs);
            int mid = ls + rs >> 1;
            long res = 0;
            if (l <= mid) {
                res += query(node.left, l, r, ls, mid);
            }
            if (r >= mid + 1) {
                res += query(node.right, l, r, mid + 1, rs);
            }
            return res % 1000000007;
        }
    }

    class Solution {
        int c;
        int[] rank;
        int[] end;

        void dfs(int i, List<List<Integer>> list) {
            c++;
            rank[i]=c;
            for (int ne:list.get(i)){
                dfs(ne, list);
            }
            end[i]=c;
        }

        public int[] bonus(int n, int[][] leadership, int[][] operations) {
            List<List<Integer>> list = new ArrayList<>();
            for (int i = 0; i <= n; i++) {
                list.add(new ArrayList<>());
            }
            for (int[] ints : leadership) {
                list.get(ints[0]).add(ints[1]);
            }
            rank = new int[n+1];
            end = new int[n+1];
            dfs(1, list);

            List<Integer> res = new ArrayList<>();
            SegTree segTree = new SegTree(50001);
//            SegTree segTree = new SegTree(10);
            for (int[] op : operations) {
                if (op[0] == 1) {
                    segTree.add(rank[op[1]], rank[op[1]], op[2]);
                } else if (op[0] == 2) {
                    segTree.add(rank[op[1]], end[op[1]], op[2]);
                } else if (op[0] == 3) {
                    res.add((int) segTree.query(rank[op[1]], end[op[1]]));
                }
            }
            return res.stream().mapToInt(x->x).toArray();
        }
    }

    public static void main(String[] args) {
        int[] bonus = new LCP05().new Solution().bonus(6, new int[][]{{1, 2}, {1, 6}, {2, 3}, {2, 5}, {1, 4}},
                new int[][]{{1, 1, 500}, {2, 2, 50}, {3, 1}, {2, 6, 15}, {3, 1}});
        System.out.println(bonus);
    }
}
