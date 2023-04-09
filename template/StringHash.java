

//如果出现hash冲突，可用不同的fact创建多个此类对象。或者用随机哈希fact。
class StringHash {
    long[] hash;
    long[] p;
    int fact;
    int mod;

    public StringHash(String s) {
        this(s,171,1000000007);
    }

    public StringHash(String s,int fact,int mod) {
        int n=s.length();
        this.fact=fact;
        this.mod=mod;
        hash=new long[n+1];
        p=new long[n+1];
        p[0]=1;
        for (int i = 1; i < n + 1; i++) {
            hash[i]=(hash[i-1]*fact+s.charAt(i-1))%mod;
            p[i]=p[i-1]*fact%mod;
        }
    }

    public long hash(int leftInclusive, int rightExclusive) {
        int li=leftInclusive+1,ri=rightExclusive,len=rightExclusive-leftInclusive;
        //li,ri是闭区间在hash数组上的下标
        //只要减去hash[li-1]在区间[li,ri]上的积累即可, +mod是为了防负数
        return (hash[ri]-hash[li-1]*p[len]%mod+mod)%mod;
    }
}
