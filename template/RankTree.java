//
//
//import java.util.Map;
//
///**
// * @Author Create by jiaxiaozheng
// * @Date 2022/10/3
// */
//class RankTree {
//    int cur;
//    Map<Long,Integer> idxMap;
//
//    long[] f;
//
//    public RankTree(int size) {
//
//    }
//
//    //创建下标[1,maxN]的数组
//    public RankTree(int maxN) {
//        this(maxN,)
//        f=new long[maxN+1];
//    }
//
//    public RankTree(int maxN, boolean usingHash) {
//        f=new long[maxN+1];
//    }
//
//    //可以加上取余
//    long trim(long x) {
//        return x;
//    }
//
//    //下标i增加v，下标0不可用
//    public void add(int i, long v) {
//        for(;i<f.length;i+=i&-i) {
//            f[i]+=v;
//        }
//    }
//
//    //查询闭区间[1,r]的和，下标0不可用
//    public long query(int i) {
//        if (i<0){
//            throw new IllegalArgumentException("index:"+i);
//        }
//        long sum=0;
//        for (; i > 0; i-=i&-i) {
//            sum=trim(sum+f[i]);
//        }
//        return sum;
//    }
//
//    //查询闭区间[l,r]的和
//    public long query(int l, int r) {
//        return query(r)-query(l-1);
//    }
//
//    //找使区间[l,r]的和>=target的最小的r
//    public int firstGreaterEquals(int l, int target) {
//
//    }
//
//    //找使区间[l,r]的和>=target的最小的r
//    public int firstGreater(int l, int target) {
//
//    }
//}
