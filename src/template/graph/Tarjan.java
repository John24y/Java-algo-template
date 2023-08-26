package template.graph;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Create by CROW
 * @Date 2023/2/11
 */
class Tarjan {

    List<List<Integer>> g = new ArrayList<>();
    int n;
    int[] dfn, low;
    int[] fa;//祖先
    int[] color;//强连通分量id，不在环中的节点是只有1个节点的分量
    int[] colorSize;//每个强连通分量包含的节点数量
    int colorNum;//不同颜色数量
    boolean[] cutPoint;//是否割点

    int curDfn;
    private boolean[] inStack;
    private ArrayList<Integer> stack = new ArrayList<>();

    public Tarjan(int n, List<List<Integer>> g) {
        this.n = n;
        this.g = g;
        assert g.size() == n + 1;
        dfn = new int[n + 1];
        low = new int[n + 1];
        fa = new int[n + 1];
        color = new int[n + 1];
        colorSize = new int[n + 1];
        inStack = new boolean[n + 1];
        cutPoint = new boolean[n + 1];
        stack.ensureCapacity(n + 1);
        for (int i = 1; i <= n; i++) {
            fa[i] = i;
        }
        for (int i = 1; i <= n; i++) {
            if (dfn[i] == 0) {
                tarjan(i);
            }
        }
    }

    void tarjan(int u) {
        dfn[u] = low[u] = ++curDfn;
        stack.add(u);
        inStack[u] = true;
        int branch = 0;
        for (int v : g.get(u)) {
            if (dfn[v] == 0) {
                branch++;
                fa[v] = fa[u];
                tarjan(v);
                low[u] = Math.min(low[u], low[v]);
                if (u != fa[u] && low[v] >= dfn[u]) {
                    //找到非根节点割点
                    cutPoint[u] = true;
                }
            } else if (inStack[v]) {//如果v已访问过，且v不在stack中，则v和u必不在同一环上，可以跳过此边
                low[u] = Math.min(low[u], dfn[v]);//无论是找割点还是强联通分量，都应该取dfn[v]
            }
        }
        if (u == fa[u] && branch >= 2) {
            //找到根节点割点
            cutPoint[u] = true;
        }
        if (low[u] == dfn[u]) {
            //u是强连通分量的入口，u的连通分量的节点都在栈中，为此连通分量染色
            colorNum++;
            int k;
            do {
                //stack!=调用栈，再碰到联通分量入口之前的回溯，不会从stack中移除掉
                k = stack.remove(stack.size() - 1);
                inStack[k] = false;
                color[k] = colorNum;
                colorSize[colorNum]++;
            } while (u != k);
        }
    }

    /**
     * ret[0] 每个连通分量的入度，下标0不可用
     * ret[1] 每个连通分量的出度，下标0不可用
     */
    int[][] calcDig() {
        int[] inDig=new int[colorNum+1];
        int[] outDig=new int[colorNum+1];
        for (int i = 1; i < n; i++) {
            for (Integer ch : g.get(i)) {
                if (color[ch]!=color[i]) {
                    inDig[color[ch]]++;
                    outDig[color[i]]++;
                }
            }
        }
        return new int[][] {inDig,outDig};
    }
}

