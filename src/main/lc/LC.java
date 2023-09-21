package main.lc;

import java.util.*;

/**
 * @Author Create by CROW
 * @Date 2023/4/22
 */
public class LC {
    static class KMP {
        public static int[] table(String p){
            int[] match = new int[p.length()];
            int maxLen = 0;

            for(int i = 1; i < p.length(); i++){
                while(maxLen > 0 && p.charAt(maxLen) != p.charAt(i)){
                    maxLen = match[maxLen - 1];
                }
                if(p.charAt(maxLen) == p.charAt(i)){
                    maxLen++;
                }
                match[i] = maxLen;
            }
            return match;
        }

        public static List<Integer> findMatch(String t, String p){
            int[] m = table(p);
            List<Integer> res = new ArrayList<>();
            int count = 0;
            for(int i = 0; i < t.length(); i++){
                while(count > 0 && p.charAt(count) != t.charAt(i)){
                    count = m[count - 1];
                }
                if(p.charAt(count) == t.charAt(i)){
                    count++;
                }
                if(count == p.length()){
                    res.add(i - p.length() + 1);
                    count = m[count - 1];
                }
            }
            return res;
        }
    }
    class Solution {
        int n;
        int M= (int) (1e9+7);
        public int numberOfWays(String s, String t, long k) {
            String ss=s+s;
            List<Integer> match = KMP.findMatch(ss, t);
            n=s.length();
            dp=new Map[n];
            for (int i = 0; i < n; i++) {
                dp[i]=new HashMap<>();
            }
            long res=0;
            for (Integer m : match) {
                if (m<n){
                    res+=dfs(k,m);
                    res%=M;
                } else {
                    break;
                }
            }
            return (int) res;
        }

        Map<Long,Integer>[] dp;
        int dfs(long k, int m) {
            if (k==1) {
                return m>0?1:0;
            }
            if (dp[m].containsKey(k)) {
                return dp[m].get(k);
            }
            long hb=Long.highestOneBit(k);
            long a=hb,b=k-hb;
            if (hb==k) {
                a=b=k>>1;
            }
            int res=0;
            for (int i = 0; i < n; i++) {
                int t=(m-i+n)%n;
                res= (int) (((long)dfs(a,i)*(long)dfs(b,t)+res)%M);
            }
            dp[m].put(k, res);
            return res;
        }
    }

    public static void main(String[] args) {
        /**
         * "wkldv"
         * "ldvwk"
         * 854972569843185
         */
        /**
         * "giagiagiagiagiagiagiagiagiagiagiagiagiagiagiagiagiagiagiagiagiagiagiagiagiagiagiagiagia"
         * "giagiagiagiagiagiagiagiagiagiagiagiagiagiagiagiagiagiagiagiagiagiagiagiagiagiagiagiagia"
         * 458907869062402
         */
        long l = new LC().new Solution().numberOfWays("giagiagiagiagiagiagiagiagiagiagiagiagiagiagiagiagiagiagiagiagiagiagiagiagiagiagiagiagia", "giagiagiagiagiagiagiagiagiagiagiagiagiagiagiagiagiagiagiagiagiagiagiagiagiagiagiagiagia", 458907869062402L);
        System.out.println(l);
//        int[] ints = new LC().new Solution().maximumSumQueries(
//                LCUtils.parseIntAr("[4,3,1,2]"),
//                LCUtils.parseIntAr("[2,4,9,5]"),
//                LCUtils.parseIntIntAr("[[4,1],[1,3],[2,5]]")
//        );
//        System.out.println(Arrays.toString(ints));
    }

}


