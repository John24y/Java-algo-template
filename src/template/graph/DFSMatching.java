package template.graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 最大二分图匹配
 * 左侧点数为n，右侧点数为m，为左侧每个点找到一个匹配，左右侧任一点只能匹配一个。
 */
class DFSMatching {
    List<List<Integer>> g;
    int[] pl, pr;
    int n, m;
    boolean[] vis;

    //左侧点数为n，右侧点数为m，点的下标从0开始
    public DFSMatching(int _n, int _m) {
        n = _n;
        m = _m;
        g = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            g.add(new ArrayList<>());
        }
    }

    public void add(int from, int to) {
        g.get(from).add(to);
    }

    private boolean dfs(int l) {
        vis[l]=true;
        for (int r : g.get(l)) {
            if (pr[r] == -1) {
                pl[l] = r;
                pr[r] = l;
                return true;
            }
        }
        for (int r : g.get(l)) {
            if (!vis[pr[r]] && dfs(pr[r])) {
                pl[l] = r;
                pr[r] = l;
                return true;
            }
        }
        return false;
    }

    public int solve() {
        pl = new int[n];
        pr = new int[m];
        Arrays.fill(pl, -1);
        Arrays.fill(pr, -1);
        int ans=0;
        for (int i = 0; i < n; i++) {
            vis=new boolean[n];
            if (pl[i] == -1 && dfs(i)) {
                ans++;
            }
        }
        return ans;
    }
}
