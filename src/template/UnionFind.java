package template;

class UnionFind {
    int[] fa;
    int[] sz;//包含节点数量
    int[] edgeCnt;//包含边数

    public UnionFind(int n) {
        fa=new int[n+1];
        sz=new int[n+1];
        edgeCnt=new int[n+1];
        for (int i = 0; i <= n; i++) {
            fa[i]=i;
            sz[i]=1;
        }
    }

    int find(int x) {
        if (fa[x]!=x){
            fa[x]=find(fa[x]);
        }
        return fa[x];
    }

    void union(int i, int j) {
        int f1=find(fa[i]);
        int f2=find(fa[j]);
        if (f1>f2){
            int t=f2;
            f2=f1;
            f1=t;
        }
        edgeCnt[f1]++;
        if (f1==f2)return;
        fa[f2]=f1;
        sz[f1]+=sz[f2];
        edgeCnt[f1]+=edgeCnt[f2];
    }

}
