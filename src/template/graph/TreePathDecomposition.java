package template.graph;

import java.util.List;

class TreePathDecomposition {
    List<List<Integer>> graph;
    int[] deep,dfn,dfnOut,parent,top,size,hson;
    int n,root,seq;
    TreePathDecomposition(List<List<Integer>> graph, int root) {
        this.graph=graph;
        this.n=graph.size();
        this.root=root;
        deep=new int[n];
        dfn=new int[n];
        dfnOut=new int[n];
        parent=new int[n];
        top=new int[n];
        size=new int[n];
        hson=new int[n];
        dfs1(root, -1);
        dfs2(root, -1, root);
    }

    void dfs1(int i, int p) {
        size[i]+=1;
        deep[i]=p==-1?1:deep[p]+1;
        hson[i]=-1;
        int mx=0;
        for (Integer ch : graph.get(i)) {
            if (ch==p) continue;
            dfs1(ch, i);
            size[i]+=size[ch];
            if (size[ch]>mx) {
                hson[i]=ch;
                mx=size[ch];
            }
        }
    }

    void dfs2(int i, int p, int ptop) {
        dfn[i]=seq++;
        parent[i]=p;
        top[i]=ptop;
        if (hson[i]!=-1) {
            dfs2(hson[i], i, ptop);
            for (Integer ch : graph.get(i)) {
                if (ch == p || ch == hson[i]) continue;
                dfs2(ch, i, ch);
            }
        }
        dfnOut[i]=seq-1;
    }

    int lca(int u, int v) {
        while (top[u]!=top[v]) {
            if (deep[top[u]]>deep[top[v]]) {
                int t=u;
                u=v;
                v=t;
            }
            v=parent[top[v]];
        }
        return deep[u]>deep[v]?v:u;
    }

    void pathSeg(int u, int v, SegOperator op) {
        while (top[u]!=top[v]) {
            if (deep[top[u]]>deep[top[v]]) {
                int t=u;
                u=v;
                v=t;
            }
            op.seg(dfn[top[v]], dfn[v]);
            v=parent[top[v]];
        }
        op.seg(Math.min(dfn[u], dfn[v]), Math.max(dfn[u], dfn[v]));
    }

    interface SegOperator {
        //[a,b]
        void seg(int a, int b);
    }
}
