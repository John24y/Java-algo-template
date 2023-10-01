package template.graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 基环树找环
 * https://leetcode.com/problems/count-visited-nodes-in-a-directed-graph/
 */
class RingTree {
    List<List<Integer>> cycList=new ArrayList<>();//所有环的list
    int[] cycId;//每个节点所在环(cyc_list)的下标，不在环上是-1

    //节点i指向edges[i]
    public RingTree(int[] edges) {
        int n = edges.length;
        cycId=new int[n];
        Arrays.fill(cycId, -1);
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
            for (Integer v : cyc) {
                cycId[v]=cycList.size()-1;
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

}

