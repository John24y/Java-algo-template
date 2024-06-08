package template.graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 基环树找环
 * https://leetcode.com/problems/count-visited-nodes-in-a-directed-graph/
 * <p>
 * 所有点的出度为1，则必存在环，至少存在自环
 */
class RingTree {
    List<List<Integer>> cycList=new ArrayList<>();//所有环的list
    int[] cycId;//每个节点所在环(cyc_list)的下标，不在环上是-1
    int[] idxInCyc;
    int[] edges;

    //节点i指向edges[i]
    public RingTree(int[] edges) {
        int n = edges.length;
        cycId=new int[n];
        idxInCyc=new int[n];
        this.edges=edges;
        Arrays.fill(cycId, -1);
        Arrays.fill(idxInCyc, -1);
        boolean[] vis=new boolean[n];
        boolean[] inStack=new boolean[n];
        List<Integer> stack=new ArrayList<>();
        for (int i = 0; i < n; i++) {
            dfs(i, vis, inStack, stack, edges);
        }
    }

    void dfs(int i, boolean[] vis, boolean[] inStack, List<Integer> stack, int[] edges) {
        if (inStack[i]) {
            int j=edges[i];
            List<Integer> cyc=new ArrayList<>();
            cycList.add(cyc);
            while (j!=i){
                cyc.add(j);
                j=edges[j];
            }
            cyc.add(i);
            for (int k = 0; k < cyc.size(); k++) {
                int v=cyc.get(k);
                cycId[v]=cycList.size()-1;
                idxInCyc[v]=k;
            }
            return;
        }
        if (vis[i])return;
        vis[i]=true;
        inStack[i]=true;
        stack.add(i);
        dfs(edges[i], vis, inStack, stack, edges);
        stack.remove(stack.size()-1);
        inStack[i]=false;
    }

    /**
     * 到圆环的距离，每条边的长度算1
     */
    int[] distanceToCycle() {
        int n = edges.length;
        boolean[] vis = new boolean[n];
        int[] dis = new int[n];
        for (int i = 0; i < n; i++) {
            distanceToCycle(i, edges, vis, dis);
        }
        return dis;
    }

    private void distanceToCycle(int i, int[] edges, boolean[] vis, int[] dis) {
        if (vis[i]) return;
        vis[i] = true;
        distanceToCycle(edges[i], edges, vis, dis);
        if (cycId[i] == -1) {
            dis[i] = dis[edges[i]] + 1;
        } else {
            dis[i]=cycList.get(cycId[i]).size();
        }
    }

}

