package lc;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author Create by CROW
 * @Date 2023/2/15
 */
public class A {
    class BinaryLiftingLCA {
        List<List<Integer>> graph;
        int[][] up;//up[b][i] 节点i向上走2^b步
        int[] depth;
        int MB = 30;

        BinaryLiftingLCA(List<List<Integer>> graph) {
            this.graph = graph;
            int n = graph.size();
            up = new int[MB+1][n];
            depth = new int[n];
            dfs(0, -1);
        }

        void dfs(int u, int p) {
            up[0][u] = p;
            depth[u] = p == -1 ? 0 : depth[p] + 1;
            for (int b = 1; b <= MB; b++) {
                int ancestor = up[b - 1][u];
                if (ancestor == -1) {
                    up[b][u] = -1;
                } else {
                    up[b][u] = up[b - 1][ancestor];
                }
            }
            for (Integer ch : graph.get(u)) {
                if (ch == p) continue;
                dfs(ch, u);
            }
        }

        int levelUp(int u, int lv) {
            for (int i = MB; i >= 0; i--) {
                if ((lv & (1 << i)) > 0) {
                    u = up[i][u];
                }
            }
            return u;
        }

        int lca(int u, int v) {
            if (depth[u] < depth[v]) {
                return lca(v, u);
            }
            u = levelUp(u, depth[u] - depth[v]);
            if (u == v) return u;
            for (int i = MB; i >= 0; i--) {
                if (up[i][u] == up[i][v]) continue;
                u = up[i][u];
                v = up[i][v];
            }
            return up[0][u];
        }
    }
    class Solution {
        int[][] wsum;
        public int[] minOperationsQueries(int n, int[][] edges, int[][] queries) {
            wsum=new int[n][27];
            List<List<int[]>> g=new ArrayList<>();
            List<List<Integer>> graph=new ArrayList<>();
            for (int i = 0; i < n; i++) {
                g.add(new ArrayList<>());
                graph.add(new ArrayList<>());
            }
            for (int[] edge : edges) {
                g.get(edge[0]).add(new int[]{edge[1],edge[2]});
                g.get(edge[1]).add(new int[]{edge[0],edge[2]});
                graph.get(edge[0]).add(edge[1]);
                graph.get(edge[1]).add(edge[0]);
            }
            dfs(0,-1,new int[27],g);
            BinaryLiftingLCA lca = new BinaryLiftingLCA(graph);
            int[] ans=new int[queries.length];
            for (int i = 0; i < queries.length; i++) {
                int[] t=new int[27];
                int total = 0, mx = 0;
                for (int j = 0; j < 27; j++) {
                    t[j]+=wsum[queries[i][0]][j];
                    t[j]+=wsum[queries[i][1]][j];
                    t[j]-=wsum[lca.lca(queries[i][0],queries[i][1])][j]*2;
                    total+=t[j];
                    mx = Math.max(mx, t[j]);
                }
                ans[i]=total-mx;
            }
            return ans;
        }

        void dfs(int i, int p, int[] path, List<List<int[]>> g) {
            System.arraycopy(path, 0, wsum[i], 0, 27);
            for (int[] ch : g.get(i)) {
                if (ch[0]==p)continue;
                path[ch[1]]++;
                dfs(ch[0],i,path,g);
                path[ch[1]]--;
            }
        }
    }

    public static void main(String[] args) {
//        int score = new Solution().maximumScore(Arrays.asList(8, 3, 9, 3, 8), 2);
//        System.out.println(score);

//        String input = scanner.nextLine();
//        long r = new A().new Solution().getSchemeCount(3, 3, LCUtils.parseStringAr("[\"..R\",\"..B\",\"?R?\"]"));
//        System.out.println(r);
    }

}

