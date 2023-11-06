package main.lc;

import java.util.*;
class FenwickTree {
    long[] f;

    //可用下标为 [0,maxN]
    public FenwickTree(int maxN) {
        f=new long[maxN+2];
        Arrays.fill(f,Long.MIN_VALUE);
    }

    //下标i增加v，下标从0开始
    public void add(int i, long v) {
        for(i++;i<f.length;i+=i&-i) {
            f[i]=Math.max(f[i],v);
        }
    }

    //查询闭区间[0,r]的和
    public long query(int i) {
        long sum=Long.MIN_VALUE;
        for (i++; i > 0; i-=i&-i) {
            sum=Math.max(f[i],sum);
        }
        return sum;
    }

}
class SparseIndex {
    Map<Long,Integer> map = new HashMap<>();
    public void add(long num) {
        map.put(num,-1);
    }
    public void generateIndex() {
        List<Long> list = new ArrayList<>(map.keySet());
        Collections.sort(list);
        for (int i = 0; i < list.size(); i++) {
            map.put(list.get(i),i);
        }
    }
    public int getIndex(long num) {
        return map.get(num);
    }
}
class Solution {
    public long maxBalancedSubsequenceSum(int[] nums) {
        int n=nums.length;
        SparseIndex index = new SparseIndex();
        for (int i = 0; i < n; i++) {
            index.add((long)nums[i] - i);
        }
        index.generateIndex();
        FenwickTree tree = new FenwickTree(index.map.size());
        for (int i = 0; i < n; i++) {
            int idx = index.getIndex((long) nums[i] - i);
            long q=tree.query(idx);
            long s=Math.max(nums[i],q);
            if (q!=Long.MIN_VALUE) {
                s=Math.max(s, nums[i]+q);
            }
            tree.add(idx,s);
        }
        return tree.query(index.map.size());
    }

    public static void main(String[] args) {
        long score = new Solution().maxBalancedSubsequenceSum(new int[]{-1,-5});
        System.out.println(score);

//        String input = scanner.nextLine();
//        long r = new A().new Solution().getSchemeCount(3, 3, LCUtils.parseStringAr("[\"..R\",\"..B\",\"?R?\"]"));
//        System.out.println(r);
    }
}
