package template.graph;

import java.util.ArrayDeque;
import java.util.Arrays;

class KMMatching {
    private static final long INF = Long.MAX_VALUE;

    private int n;
    private long[][] graph;

    private long[] leftLabel;
    private long[] rightLabel;
    private long[] slack;
    int[] leftMatch;
    int[] rightMatch;
    private int[] pre;
    private boolean[] leftVis;
    private boolean[] rightVis;
    private ArrayDeque<Integer> q = new ArrayDeque<>();

    //n个点，下标[1..n]，下标0不可用
    public KMMatching(int n) {
        this.n = n;
        n++;
        graph =new long[n][n];
        leftLabel = new long[n];
        rightLabel = new long[n];
        slack = new long[n];
        leftMatch = new int[n];
        rightMatch = new int[n];
        pre = new int[n];
        leftVis = new boolean[n];
        rightVis = new boolean[n];
        for (int i = 0; i < n; i++) {
            Arrays.fill(graph[i], -INF);
        }
    }

    public void add(int left, int right, long w) {
        graph[left][right]=w;
        leftLabel[left] = Math.max(leftLabel[left], w);
    }

    private void aug(int r) {
        int t;
        while(r > 0) {
            t = leftMatch[pre[r]];
            leftMatch[pre[r]] = r;
            rightMatch[r] = pre[r];
            r = t;
        }
    }

    private void bfs(int s) {
        Arrays.fill(leftVis, false);
        Arrays.fill(rightVis, false);
        Arrays.fill(pre, 0);
        Arrays.fill(slack, INF);

        q.clear();
        q.add(s);

        while (true) {
            while (!q.isEmpty()) {
                int l = q.poll();
                leftVis[l] = true;
                for (int r = 1; r <= n; r++) {
                    if (!rightVis[r]) {
                        if (graph[l][r] != -INF && leftLabel[l] + rightLabel[r] - graph[l][r] < slack[r]) {
                            slack[r] = leftLabel[l] + rightLabel[r] - graph[l][r];
                            pre[r] = l;
                            if (slack[r] == 0) {
                                rightVis[r] = true;
                                if (rightMatch[r] == 0) {
                                    aug(r);
                                    return;
                                } else {
                                    q.add(rightMatch[r]);
                                }
                            }
                        }
                    }
                }
            }
            long d = INF;
            for (int i = 1; i <= n; i++) {
                if (!rightVis[i]) {
                    d = Math.min(d, slack[i]);
                }
            }
            for (int i = 1; i <= n; i++) {
                if (leftVis[i]) {
                    leftLabel[i] -= d;
                }
                if (rightVis[i]) {
                    rightLabel[i] += d;
                } else {
                    slack[i] -= d;
                }
            }
            for (int i = 1; i <= n; i++) {
                if (!rightVis[i]) {
                    if (slack[i] == 0) {
                        rightVis[i] = true;
                        if (rightMatch[i] == 0) {
                            aug(i);
                            return;
                        } else {
                            q.add(rightMatch[i]);
                        }
                    }
                }
            }
        }
    }

    public long solve() {
        for (int i = 1; i <= n; i++) {
            bfs(i);
        }
        long ans=0;
        for (int i = 1; i <= n; i++) {
            ans+=graph[i][leftMatch[i]];
        }
        return ans;
    }

}
