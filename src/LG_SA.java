//import java.util.*;
//import java.util.stream.Collectors;
//
///**
// * @Author Create by jiaxiaozheng
// * @Date 2022/10/11
// */
//public class LG_SA {
//    public static void main(String[] args) {
//        List<Map<Integer, Integer>> list = new ArrayList<>();
//        for (int i = 0; i < 100; i++) {
//            list.add(new HashMap<>());
//        }
//    }
//}
//
//
//class Solution {
//    public int candy(int[] ratings) {
//        int n= ratings.length;
//        PriorityQueue<Integer> q=new PriorityQueue<>(Comparator.comparingInt(x->ratings[x]));
//        for (int i = 0; i < n; i++) {
//            q.add(i);
//        }
//        int[] res=new int[n];
//        int s=0;
//        while (!q.isEmpty()){
//            int i = q.poll();
//            int v=1;
//            if (i>0&&ratings[i]>ratings[i-1]){
//                v=Math.max(v,res[i-1]);
//            }
//            if (i<n-1&&ratings[i]>ratings[i+1]){
//                v=Math.max(v,res[i+1]);
//            }
//            s+=v;
//            res[i]=v;
//        }
//        return s;
//    }
//}