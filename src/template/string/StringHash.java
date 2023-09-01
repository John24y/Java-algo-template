package template.string;

//有些题目会卡hash冲突，比如用比较小的字符基数。这种可以用不同的fact创建多个hash，然后 (a<<31)|b
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

    //[L,R)
    public long hash(int l, int r) {
        return (hash[r]-hash[l]*p[r-l]%mod+mod)%mod;
    }
}
