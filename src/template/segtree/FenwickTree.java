package template.segtree;

import java.util.Arrays;

/**
 * @Author Create by CROW
 * @Date 2022/10/3
 */
class FenwickTree {
    //f[i]表示 (i-(i&-i),i] 区间的统计
    long[] f;

    //可用下标为 [0,maxN]
    public FenwickTree(int maxN) {
        f=new long[maxN+2];
    }

    //可以加上取余
    long trim(long x) {
        return x;
    }

    //下标i增加v，下标从0开始
    public void add(int i, long v) {
        for(i++;i<f.length;i+=i&-i) {
            f[i]+=v;
        }
    }

    //查询闭区间[0,r]的和
    public long query(int i) {
        long sum=0;
        for (i++; i > 0; i-=i&-i) {
            sum=trim(sum+f[i]);
        }
        return sum;
    }

    //查询闭区间[l,r]的和
    public long query(int l, int r) {
        return query(r)-query(l-1);
    }

}


/**
 * 适用于：
 * 元素只增不减
 * 查询前缀数组的最大值
 * 单点修改
 */
class MaxValFenwickTree {
    long[] f;
    //查询结果相加注意溢出
    private final static long INIT = Long.MIN_VALUE;

    //可用下标为 [0,maxN]
    public MaxValFenwickTree(int maxN) {
        f=new long[maxN+2];
        Arrays.fill(f,INIT);
    }

    //set,下标从0开始
    public void set(int i, long v) {
        for(i++;i<f.length;i+=i&-i) {
            f[i]=Math.max(f[i],v);
        }
    }

    //查询闭区间[0,r]的和
    public long query(int i) {
        long sum=INIT;
        for (i++; i > 0; i-=i&-i) {
            sum=Math.max(f[i],sum);
        }
        return sum;
    }

}