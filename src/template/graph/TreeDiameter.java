package template.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class TreeDiameter {
    /**
     * 两次遍历算法，只能求边长没有负数的
     */
    class SimpleDiameter {
        /**
         * 两次遍历算法，只能求边长没有负数的
         */
        public long diameter(int minIndex, int maxIndex, List<int[]> edges) {
            List<List<int[]>> g = new ArrayList<>();
            for (int i = minIndex; i <= maxIndex; i++) {
                g.add(new ArrayList<>());
            }
            for (int[] edge : edges) {
                g.get(edge[0]).add(new int[]{edge[1], edge[2]});
                g.get(edge[1]).add(new int[]{edge[0], edge[2]});
            }
            long[] res = bfsMax(maxIndex + 1, g, minIndex);
            res = bfsMax(maxIndex + 1, g, (int) res[1]);
            return res[0];
        }

        private long[] bfsMax(int n, List<List<int[]>> g, int start) {
            long res = 0;
            long resPoint = start;
            List<long[]> q = new ArrayList<>();
            q.add(new long[]{start, 0});
            boolean[] vis = new boolean[n];
            vis[start] = true;
            while (!q.isEmpty()) {
                long[] remove = q.remove(q.size() - 1);
                int v = (int) remove[0];
                long dis = remove[1];
                if (dis > res) {
                    res = dis;
                    resPoint = v;
                }
                for (int[] edge : g.get(v)) {
                    if (!vis[edge[0]]) {
                        vis[edge[0]] = true;
                        q.add(new long[]{edge[0], edge[1] + dis});
                    }
                }
            }
            return new long[]{res, resPoint};
        }
    }

    /**
     * 换根DP算法，边长可以位负。
     * 枚举树中最长路径的根节点，对当前根节点找最远孩子节点。
     * 另一种方法是对根节点找最远的2个孩子节点，距离相加，但仍然要遍历所有节点为根才能得到答案，所以实现更麻烦。
     */
    class DPDiameter {
        long[] dp;
        long[] deep;
        List<List<int[]>> g;

        public long diameter(int minIndex, int maxIndex, List<int[]> edges) {
            g = new ArrayList<>();
            for (int i = minIndex; i <= maxIndex; i++) {
                g.add(new ArrayList<>());
            }
            for (int[] edge : edges) {
                g.get(edge[0]).add(new int[]{edge[1], edge[2]});
                g.get(edge[1]).add(new int[]{edge[0], edge[2]});
            }
            dp = new long[maxIndex + 1];
            deep = new long[maxIndex + 1];
            down(0, -1);
            dfs(0, -1, 0);
            long ans = dp[0];
            for (long l : dp) {
                ans = Math.max(ans, l);
            }
            return ans;
        }

        private void down(int i, int p) {
            for (int[] edge : g.get(i)) {
                if (edge[0] == p) {
                    continue;
                }
                down(edge[0], i);
                deep[i] = Math.max(deep[i], edge[1] + Math.max(0, deep[edge[0]]));
            }
        }

        private void dfs(int i, int p, long pDeep) {
            List<Long> dis = new ArrayList<>();
            if (p != -1) {
                dis.add(pDeep);
            }
            for (int[] edge : g.get(i)) {
                if (edge[0] == p) continue;
                dis.add(deep[edge[0]] + edge[1]);
            }
            Collections.sort(dis);
            if (dis.isEmpty()) {
                dp[i] = 0;
                return;
            }
            dp[i] = dis.get(dis.size() - 1);
            for (int[] edge : g.get(i)) {
                if (edge[0] == p) continue;
                long v = deep[edge[0]] + edge[1];
                long npDeep = v != dp[i] ? dp[i] : (dis.size() >= 2 ? dis.get(dis.size() - 2) : 0);
                dfs(edge[0], i, npDeep + edge[1]);
            }
        }

    }

}
