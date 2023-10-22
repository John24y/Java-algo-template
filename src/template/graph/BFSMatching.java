package template.graph;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 最大二分图匹配
 * 左侧点数为n，右侧点数为m，为左侧每个点找到一个匹配，左右侧任一点只能匹配一个。
 */
class BFSMatching {
    List<List<Integer>> g;
    int[] pl, pr, want;
    int n, m;

    public BFSMatching(int _n, int _m) {
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

    private boolean bfs(int l) {
        //want[i]是尝试匹配右侧点i的左侧点
        want=new int[m];
        Arrays.fill(want, -1);
        //寻找匹配的子问题（左侧点），只要任意一个找到空位匹配，整个问题就有解
        ArrayDeque<Integer> q=new ArrayDeque<>(n);
        q.add(l);
        while (!q.isEmpty()) {
            l = q.pollFirst();
            for (Integer r : g.get(l)) {
                if (want[r]==-1) {
                    want[r] = l;
                    if (pr[r]==-1) {
                        //有解，回溯答案，更新匹配
                        int nextr = r;
                        while (nextr != -1) {
                            //l,r同时迭代，不要漏掉
                            r = nextr;
                            l = want[nextr];
                            nextr = pl[l];
                            pl[l] = r;
                            pr[r] = l;
                        }
                        return true;
                    } else {
                        //每个左侧点至多加入一次队列
                        q.add(pr[r]);
                    }
                }
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
            if (bfs(i)) {
                ans++;
            }
        }
        return ans;
    }
}
