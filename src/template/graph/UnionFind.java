package template.graph;

import java.util.ArrayList;
import java.util.List;

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
        int f1=find(i);
        int f2=find(j);
        edgeCnt[f1]++;
        if (f1==f2)return;
        fa[f2]=f1;
        sz[f1]+=sz[f2];
        edgeCnt[f1]+=edgeCnt[f2];
    }

}


/**
 * 可以对联通子集的根节点做区间修改 O(1)，修改的值在路径压缩时懒传递到子节点。
 * 但修改时对整个联通子集的，对联通集的子树做修改是无法 O(1) 的，因为联通集内部的结构不稳定。
 */
class LazyUnionFind {
    int[] fa;
    int[] sz;//包含节点数量
    long[] offset;// 每个节点相比于当前根节点的增量，offset[i]=val[i]-val[root[i]]

    public LazyUnionFind(int n) {
        fa=new int[n+1];
        sz=new int[n+1];
        offset=new long[n+1];
        for (int i = 0; i <= n; i++) {
            fa[i]=i;
            sz[i]=1;
        }
    }

    int find(int x) {
        if (fa[x]!=x){
            int r = find(fa[x]);
            offset[x]=offset[fa[x]]+offset[x];
            fa[x]=r;
        }
        return fa[x];
    }

    //val[j]-val[i]=d
    boolean union(int i, int j, long d) {
        int f1=find(i);
        int f2=find(j);
        if (f1==f2) {
            return offset[j]-offset[i]==d;
        }
        fa[f2]=f1;
        sz[f1]+=sz[f2];
        offset[f2]=d-offset[j]+offset[i];
        return true;
    }
}

/**
 * 启发式合并的并查集，每次小的联通集合并到大的，合并时可以遍历小联通集的每个节点
 */
class HeuUnionFind {
    int[] fa;
    int[] sz;//包含节点数量
    long[] offset; // 每个节点相比于当前根节点的增量, offset[i]=val[i]-val[root[i]]
    List<List<Integer>> vertex = new ArrayList<>();

    public HeuUnionFind(int n) {
        fa=new int[n+1];
        sz=new int[n+1];
        offset=new long[n+1];
        for (int i = 0; i <= n; i++) {
            fa[i]=i;
            sz[i]=1;
            vertex.add(new ArrayList<>());
            vertex.get(i).add(i);
        }
    }

    int find(int x) {
        if (fa[x]!=x){
            int r = find(fa[x]);
            fa[x]=r;
        }
        return fa[x];
    }

    //val[j]-val[i]=d
    boolean union(int i, int j, long d) {
        int f1=find(i);
        int f2=find(j);
        if (f1==f2) {
            return offset[j]-offset[i]==d;
        }
        if (sz[f1]<sz[f2]) {
            return union(j,i,-d);
        }
        long nd = d-offset[j]+offset[i];
        fa[f2]=f1;
        sz[f1]+=sz[f2];
        for (Integer v : vertex.get(f2)) {
            offset[v]+=nd;
            vertex.get(f1).add(v);
        }
        vertex.get(f2).clear();
        return true;
    }

}


