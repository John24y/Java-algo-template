package lc;

/**
 * @Author Create by jiaxiaozheng
 * @Date 2022/10/3
 */
public class LCP24 {
    public static void main(String[] args) {
        int[] game = new Solution().numsGame(LCUtils.readInts());
        System.out.println(game);
    }

    static class Solution {
        public int[] numsGame(int[] nums) {
            int n= nums.length;
            int MX = 1002+n;
            FenwickTree tree1 = new FenwickTree(MX);
            FenwickTree tree2 = new FenwickTree(MX);
            int[] ans = new int[n];
            for (int i = 0; i < nums.length; i++) {
                int num = nums[i]-i+n+2;
                tree1.add(num,1);
                tree2.add(num,num);
                int low=1,high=MX;
                while (low<=high){
                    int mid=low+high>>1;
                    if (tree1.query(1,mid) >= i/2+1) {
                        high=mid-1;
                    }else {
                        low=mid+1;
                    }
                }
                long s1=tree1.query(1,low-1)*low-tree2.query(1,low-1);
                long s2=tree2.query(low+1,MX)-tree1.query(low+1,MX)*low;
                ans[i]= (int) ((s1+s2+1000000007)%1000000007);
            }
            return ans;
        }

        class FenwickTree {
            long[] f;

            //创建下标[1,maxN]的数组
            public FenwickTree(int maxN) {
                f=new long[maxN+1];
            }

            //可以加上取余
            long trim(long x) {
                return x;
            }

            //下标i增加v，下标0不可用
            public void add(int i, long v) {
                for(;i<f.length;i+=i&-i) {
                    f[i]+=v;
                }
            }

            //查询闭区间[1,r]的和，下标0不可用
            public long query(int i) {
                if (i<0){
                    throw new IllegalArgumentException("index:"+i);
                }
                long sum=0;
                for (; i > 0; i-=i&-i) {
                    sum=trim(sum+f[i]);
                }
                return sum;
            }

            //查询闭区间[l,r]的和
            public long query(int l, int r) {
                return query(r)-query(l-1);
            }

        }

    }

}



