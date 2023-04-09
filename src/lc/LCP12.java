package lc;

import java.util.*;

class LCP12 {
    class Solution {
        public int maxWeight(int[][] edges, int[] value) {
            int n= value.length;
            int[] dig=new int[n];
            List<List<Integer>> adj = new ArrayList<>();//相邻点
            List<Set<Integer>> adjSet = new ArrayList<>();//相邻点
            List<List<int[]>> con = new ArrayList<>();//对角边
            for (int i = 0; i < n; i++) {
                adj.add(new ArrayList<>());
                adjSet.add(new HashSet<>());
                con.add(new ArrayList<>());
            }
            Arrays.sort(edges, Comparator.<int[]>comparingInt(o -> value[o[0]]+value[o[1]]).reversed());
            for (int[] edge : edges) {
                dig[edge[0]]++;
                dig[edge[1]]++;
                adj.get(edge[0]).add(edge[1]);
                adj.get(edge[1]).add(edge[0]);
                adjSet.get(edge[0]).add(edge[1]);
                adjSet.get(edge[1]).add(edge[0]);
            }
            int sqrtE=(int) Math.sqrt(edges.length);
            for (int i = 0; i < n; i++) {
                if (dig[i]>sqrtE){
                    for (int[] edge : edges) {
                        if (edge[0]!=i && edge[1]!=i && adjSet.get(edge[0]).contains(i) && adjSet.get(edge[1]).contains(i)){
                            con.get(i).add(edge);
                        }
                    }
                } else {
                    List<Integer> list = adj.get(i);
                    for (int j = 0; j < list.size(); j++) {
                        for (int k = j+1; k < list.size(); k++) {
                            if (adjSet.get(list.get(j)).contains(list.get(k))){
                                con.get(i).add(new int[]{list.get(j),list.get(k)});
                            }
                        }
                    }
                }
            }
            int res=0;
            //在对角边中找到去重权值top2之和
            for (int i = 0; i < n; i++) {
                List<int[]> list = con.get(i);
                if (list.isEmpty()){
                    continue;
                }
                int v1=list.get(0)[0],v2=list.get(0)[1];
                int a=Math.min(v1,v2),b=Math.max(v1,v2);
                int baseValue=value[i]+value[a]+value[b];
                res=Math.max(res,baseValue);
                List<Integer> aMatch=new ArrayList<>(),bMatch=new ArrayList<>();
                for (int[] edge : list) {
                    //找一个不含a且不含b的
                    if (edge[0]!=a&&edge[0]!=b&&edge[1]!=a&&edge[1]!=b){
                        res=Math.max(res,baseValue+value[edge[0]]+value[edge[1]]);
                    }
                    v1=Math.min(edge[0],edge[1]);
                    v2=Math.max(edge[0],edge[1]);
                    if (v1==a&&v2==b){
                        continue;
                    }
                    for (int j = 0; j < 2; j++) {
                        List<Integer> match=j==0?aMatch:bMatch;
                        int v=j==0?a:b;
                        //分别维护a和b可以匹配的最大的两个(a,b本身除外)
                        if (v1==v||v2==v){
                            int c=v1==v?v2:v1;
                            if (match.isEmpty()||value[c]>value[match.get(0)]){
                                match.add(c);
                            } else if (match.size()==2 && value[c]>value[match.get(1)]) {
                                match.add(1,c);
                            }
                            if (match.size()>2){
                                match.remove(2);
                            }
                        }
                    }
                }
                if (aMatch.isEmpty()&&bMatch.isEmpty()){
                    continue;
                }else if (aMatch.isEmpty()||bMatch.isEmpty()){
                    res=Math.max(res,baseValue+(aMatch.isEmpty()?0:value[aMatch.get(0)])+(bMatch.isEmpty()?0:value[bMatch.get(0)]));
                }else if (aMatch.get(0).equals(bMatch.get(0))){
                    res=Math.max(res,baseValue+(value[aMatch.get(0)]));
                    if (bMatch.size()==2){
                        res=Math.max(res,baseValue+value[aMatch.get(0)]+value[bMatch.get(1)]);
                    }
                    if (aMatch.size()==2){
                        res=Math.max(res,baseValue+value[bMatch.get(0)]+value[aMatch.get(1)]);
                    }
                } else {
                    res=Math.max(res,baseValue+value[bMatch.get(0)]+value[aMatch.get(0)]);
                }
            }
            return res;
        }
    }

    public static void main(String[] args) {
//        int i = new lc.LCP12().new lc.Solution().maxWeight(lc.LCUtils.parseIntIntAr("[[0,1],[0,2],[0,3],[0,4],[0,5],[0,6],[0,7],[0,8],[0,9],[1,2],[1,3],[1,4],[1,5],[1,6],[1,7],[1,8],[1,9],[2,3],[2,4],[2,5],[2,6],[2,7],[2,8],[2,9],[3,4],[3,5],[3,6],[3,7],[3,8],[3,9],[4,5],[4,6],[4,7],[4,8],[4,9],[5,6],[5,7],[5,8],[5,9],[6,7],[6,8],[6,9],[7,8],[7,9],[8,9]]"),
//                lc.LCUtils.parseIntAr("[6808,5250,74,3659,8931,1273,7545,879,7924,7710]"));
//        System.out.println(i==38918);
        int i = new LCP12().new Solution().maxWeight(LCUtils.parseIntIntAr("[[0,3],[5,8],[5,9],[1,5],[3,9],[1,7],[4,7],[6,8],[1,8],[1,3],[2,4],[4,6],[0,9],[3,4],[3,6],[4,8],[3,5],[1,4],[0,6],[8,9],[2,3],[2,6],[6,9],[4,5],[0,7],[2,8],[7,9],[0,2],[0,8],[5,7]]"),
                LCUtils.parseIntAr("[7204,2529,7132,5563,4355,877,1706,6302,7382,821]"));
        System.out.println(i==31636);
    }
}

