package template.graph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 快速求任意两点LCA（批量查询LCA）的几种算法：
 * 1. 启发式合并 O(nlogQ)
 * 2. 树上倍增 O(nlogn)
 * 3. Tarjan+并查集
 * 4. 树链剖分 O(nlogn) {@link TreePathDecomposition}
 */
class TreeLCA {

    /**
     * Tarjan批量求LCA. 以0作为根节点。
     * graph   树结构, graph[i]是节点i的邻居节点
     * queries LCA批量查询, queries[i]=[a,b] 表示求a和b的LCA
     * return ans[i]表示第i个查询的结果
     */
    class TarjanLCA {
        boolean[] vis;
        List<List<Integer>> graph;
        List<int[]> queries;
        List<List<Integer>> q;
        int[] fa;
        int[] ans;

        TarjanLCA(List<List<Integer>> graph, List<int[]> queries) {
            this.graph = graph;
            this.queries = queries;
            ans = new int[queries.size()];
            vis = new boolean[graph.size()];
            fa = new int[graph.size()];
            q = new ArrayList<>();
            for (int i = 0; i < graph.size(); i++) {
                q.add(new ArrayList<>());
            }
            for (int i = 0; i < queries.size(); i++) {
                int[] ints = queries.get(i);
                q.get(ints[0]).add(i);
                q.get(ints[1]).add(i);
            }
            for (int i = 0; i < graph.size(); i++) {
                fa[i] = i;
            }
            dfs(0, -1);
        }

        int find(int i) {
            if (fa[i] != i) {
                fa[i] = find(fa[i]);
            }
            return fa[i];
        }

        void dfs(int i, int p) {
            for (int child : graph.get(i)) {
                if (child == p) continue;
                dfs(child, i);
                fa[child] = i;
            }
            vis[i] = true;
            for (int qi : q.get(i)) {
                int[] ints = queries.get(qi);
                if (ints[0] == ints[1]) {
                    ans[qi] = ints[0];
                    continue;
                }
                if (ints[0] == i && vis[ints[1]]) {
                    ans[qi] = find(ints[1]);
                } else if (ints[1] == i && vis[ints[0]]) {
                    ans[qi] = find(ints[0]);
                }
            }
        }
    }

    /**
     * 启发式合并批量求LCA. 以0作为根节点。
     * graph   树结构, graph[i]是节点i的邻居节点
     * queries LCA批量查询, queries[i]=[a,b] 表示求a和b的LCA
     * return ans[i]表示第i个查询的结果
     */
    class MergingLCA {
        List<List<Integer>> graph;
        List<int[]> queries;
        List<Set<Integer>> q;
        int[] ans;

        MergingLCA(List<List<Integer>> graph, List<int[]> queries) {
            this.graph = graph;
            this.queries = queries;
            ans = new int[queries.size()];
            q = new ArrayList<>();
            for (int i = 0; i < graph.size(); i++) {
                q.add(new HashSet<>());
            }
            for (int i = 0; i < queries.size(); i++) {
                int[] ints = queries.get(i);
                if (ints[0] == ints[1]) {
                    ans[i] = ints[0];
                } else {
                    q.get(ints[0]).add(i);
                    q.get(ints[1]).add(i);
                }
            }
            dfs(0, -1);
        }

        void dfs(int i, int p) {
            for (int child : graph.get(i)) {
                if (child == p) continue;
                dfs(child, i);
                if (q.get(child).size() > q.get(i).size()) {
                    Set<Integer> t = q.get(i);
                    q.set(i, q.get(child));
                    q.set(child, t);
                }
                for (Integer qi : q.get(child)) {
                    if (q.get(i).contains(qi)) {
                        ans[qi] = i;
                        q.get(i).remove(qi);
                    } else {
                        q.get(i).add(qi);
                    }
                }
            }
        }
    }

    /**
     * 倍增式求LCA. 以0作为根节点。
     * graph   树结构, graph[i]是节点i的邻居节点
     */
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

}

