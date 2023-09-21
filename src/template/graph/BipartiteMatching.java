package template.graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 最大二分图匹配
 * 左侧点数为n，右侧点数为m，为左侧每个点找到一个匹配，左右侧任一点只能匹配一个。
 */
class BipartiteMatching {
    List<List<Integer>> g;
    int[] pa, pb;
    int n, m;
    boolean[] vis;

    public BipartiteMatching(int _n, int _m) {
        n = _n;
        m = _m;
        pa = new int[n];
        pb = new int[m];
        Arrays.fill(pa, -1);
        Arrays.fill(pb, -1);
        g = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            g.add(new ArrayList<>());
        }
    }

    public void add(int from, int to) {
        g.get(from).add(to);
    }

    public boolean dfs(int v) {
        vis[v]=true;
        for (int u : g.get(v)) {
            if (pb[u] == -1) {
                pa[v] = u;
                pb[u] = v;
                return true;
            }
        }
        for (int u : g.get(v)) {
            if (!vis[pb[u]] && dfs(pb[u])) {
                pa[v] = u;
                pb[u] = v;
                return true;
            }
        }
        return false;
    }

    public int solve() {
        int ans=0;
        for (int i = 0; i < n; i++) {
            vis=new boolean[n];
            if (pa[i] == -1 && dfs(i)) {
                ans++;
            }
        }
        return ans;
    }
}
