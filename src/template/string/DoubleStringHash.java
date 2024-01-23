package template.string;

class DoubleStringHash {
    static long[] p1;
    static long[] p2;
    static int fact1=171;
    static int fact2=13;
    static int mod = (int) (1e9+7);

    static void preCompute(int MAX_LEN) {
        p1=new long[MAX_LEN];
        p2=new long[MAX_LEN];
        p1[0]=1;
        p2[0]=1;
        for (int i = 1; i < MAX_LEN; i++) {
            p1[i]=p1[i-1]*fact1%mod;
            p2[i]=p2[i-1]*fact2%mod;
        }
    }

    long[] hash1;
    long[] hash2;
    int len;

    public DoubleStringHash(String s) {
        if (p1 == null) throw new RuntimeException("call preCompute first!");
        int n=s.length();
        len = n;
        hash1=new long[n+1];
        hash2=new long[n+1];
        gen(hash1, s, fact1);
        gen(hash2, s, fact2);
    }

    private void gen(long[] hash, String s, int fact) {
        int n=s.length();
        for (int i = 1; i < n + 1; i++) {
            hash[i]=(hash[i-1]*fact+s.charAt(i-1))%mod;
        }
    }

    //[L,R)
    public long hash(int l, int r) {
        long h1 = hash(l, r, hash1, p1);
        long h2 = hash(l, r, hash2, p2);
        return (h1<<31) | h2;
    }

    public long hash() {
        long h1 = hash(0, len, hash1, p1);
        long h2 = hash(0, len, hash2, p2);
        return (h1<<31) | h2;
    }

    public long hash1() {
        return hash(0, len, hash1, p1);
    }

    public long hash2() {
        return hash(0, len, hash2, p2);
    }

    public long hash1(int l, int r) {
        return hash(l, r, hash1, p1);
    }

    public long hash2(int l, int r) {
        return hash(l, r, hash2, p2);
    }

    public long hash(int l, int r, long[] hash, long[] p) {
        return (hash[r]-hash[l]*p[r-l]%mod+mod)%mod;
    }

    public long concatHash(DoubleStringHash hashB, char sep) {
        return concatHash(hashB, sep, 0, len, 0, hashB.len);
    }

    // [l,r)
    public long concatHash(DoubleStringHash hashB, char sep,
                           int la, int ra, int lb, int rb) {
        int lenB = rb - lb;
        if (sep == 0) {
            long ret1 = (p1[lenB] * hash1(la, ra) % mod + hashB.hash1(lb, rb)) % mod;
            long ret2 = (p2[lenB] * hash2(la, ra) % mod + hashB.hash2(lb, rb)) % mod;
            return (ret1<<31)|ret2;
        } else {
            long ret1 = (p1[lenB+1] * hash1(la, ra) % mod + p1[lenB] * sep % mod + hashB.hash1(lb, rb)) % mod;
            long ret2 = (p2[lenB+1] * hash2(la, ra) % mod + p2[lenB] * sep %mod + hashB.hash2(lb, rb)) % mod;
            return (ret1<<31)|ret2;
        }
    }

}