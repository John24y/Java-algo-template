package template.segtree;

/**
 * @Author Create by CROW
 * @Date 2022/10/3
 */
class FenwickTree {
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
