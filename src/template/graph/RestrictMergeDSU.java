package template.graph;

import java.util.*;

/**
 * 存在合并限制的并查集，如果两颗子树包含相同的banKey，则不能合并。
 * 启发式合并的复杂度。
 */
class RestrictMergeDSU {
    int[] fa;
    int[] sz;//包含节点数量
    List<Set<Integer>> banKey = new ArrayList<>();//含有相同banKey的子树不能合并

    public RestrictMergeDSU(int n, List<Set<Integer>> banKeyList) {
        fa = new int[n + 1];
        sz = new int[n + 1];
        for (int i = 0; i <= n; i++) {
            fa[i] = i;
            sz[i] = 1;
        }
        banKey = banKeyList;
    }

    int find(int x) {
        if (fa[x] != x) {
            fa[x] = find(fa[x]);
        }
        return fa[x];
    }

    boolean union(int i, int j) {
        int f1 = find(fa[i]);
        int f2 = find(fa[j]);
        if (f1 > f2) {
            int t = f2;
            f2 = f1;
            f1 = t;
        }
        if (f1 == f2) return true;
        Set<Integer> s1 = banKey.get(f1);
        Set<Integer> s2 = banKey.get(f2);
        if (s1.size() > s2.size()) {
            Set<Integer> t = s1;
            s1 = s2;
            s2 = t;
        }
        for (Integer b : s2) {
            if (s1.contains(b)) return false;
        }
        s1.addAll(s2);
        banKey.set(f1, s1);
        fa[f2] = f1;
        sz[f1] += sz[f2];
        return true;
    }

}
