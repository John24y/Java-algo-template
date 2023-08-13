package lc;

import java.math.BigInteger;
import java.util.*;

/**
 * @Author Create by jiaxiaozheng
 * @Date 2023/2/15
 */
public class A {

    static class Solution {
        public static int[][] leftGeRightGt(long[] nums) {
            int n = nums.length;
            int[] left = new int[n];
            int[] right = new int[n];
            LinkedList<Integer> stack = new LinkedList<>();
            Arrays.fill(left, -1);
            Arrays.fill(right, n);
            for (int i = 0; i < n; i++) {
                while (!stack.isEmpty() && nums[stack.peek()] < nums[i]) {
                    right[stack.pop()] = i;
                }
                if (!stack.isEmpty()) {
                    left[i] = stack.peek();
                }
                stack.push(i);
            }
            return new int[][] { left, right };
        }
        static long ksm(long a, long pow, long mod) {
            long skt = 1;
            while (pow > 0) {
                if (pow % 2 != 0) {
                    skt = skt * a % mod;
                }
                a = a * a % mod;
                pow = pow >> 1;
            }
            return skt % mod;
        }

        static int[] ds=new int[100001];
        static {
            int i=2;
            while (i<=100000){
                if (ds[i]==0){
                    for (int j = i+i; j <= 100000; j+=i) {
                        ds[j]++;
                    }
                    ds[i]=1;
                }
                i++;
            }
        }
        public int maximumScore(List<Integer> nums, int k) {
            int n=nums.size();
            long[] a=new long[n];
            for (int i = 0; i < n; i++) {
                a[i]=ds[nums.get(i)];
            }
            int[][] mo = leftGeRightGt(a);
            int[] left=mo[0];
            int[] right=mo[1];
            long[][] sz=new long[n][3];
            long mod = (long) (1e9+7);
            for (int i = 0; i < n; i++) {
                int l=i-left[i];
                int r=right[i]-i;
                sz[i] = new long[] {a[i], (long) l * r % mod, nums.get(i)};
            }
            Arrays.sort(sz, Comparator.comparingLong(x->-x[2]));
            long res=1;
            for (int i = 0; i < n; i++) {
                long d = Math.min(k,sz[i][1]);
                res=ksm(sz[i][2],d,mod)*res%mod;
                k-=d;
                if (k==0)break;
            }
            return (int) res;
        }
    }

    public static void main(String[] args) {
        int score = new Solution().maximumScore(Arrays.asList(8, 3, 9, 3, 8), 2);
        System.out.println(score);

//        String input = scanner.nextLine();
//        long r = new A().new Solution().getSchemeCount(3, 3, LCUtils.parseStringAr("[\"..R\",\"..B\",\"?R?\"]"));
//        System.out.println(r);
    }

}

