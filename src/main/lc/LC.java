package main.lc;

import main.LCUtils;

import java.util.*;

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

class Solution {
    public int minimumDiameterAfterMerge(int[][] edges1, int[][] edges2) {
        int n = edges1.length + 1;
        int m = edges2.length + 1;
        List<int[]> ed1 = new ArrayList<>();
        for (int i = 0; i < n - 1; i++) {
            ed1.add(new int[]{edges1[i][0],edges1[i][1], 1});
        }
        List<int[]> ed2 = new ArrayList<>();
        for (int i = 0; i < m - 1; i++) {
            ed2.add(new int[]{edges2[i][0],edges2[i][1], 1});
        }
        long diameter1 = new DPDiameter().diameter(0, n - 1, ed1);
        long diameter2 = new DPDiameter().diameter(0, m - 1, ed2);
        if (diameter1 > diameter2) {
            long t = diameter2;
            diameter2 = diameter1;
            diameter1 = t;
        }
        return (int) (Math.max((diameter1 + 1) / 2  + 1, (diameter2) / 2)  + (diameter2 + 1) / 2);
    }

    public static void main(String[] args) {
        int i = new Solution().minimumDiameterAfterMerge(LCUtils.parseIntIntAr("[[1,0],[2,3],[1,4],[2,1],[2,5]]"),
                LCUtils.parseIntIntAr("[[4,5],[2,6],[3,2],[4,7],[3,4],[0,3],[1,0],[1,8]]")
        );
        System.out.println(i);
    }
}