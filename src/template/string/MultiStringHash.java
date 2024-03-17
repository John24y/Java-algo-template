package template.string;

/**
 * @Author Create by crow
 * @Date 2024/2/21
 */
class MultiStringHash {
    static class StringHash {
        long[] p;
        long fact;
        long mod;
        long[] hash;
        int len;

        public StringHash(String s, long fact, long mod, long[] p) {
            int n = s.length();
            this.len = n;
            this.hash=new long[n+1];
            this.fact = fact;
            this.mod = mod;
            this.p = p;
            if (p.length < n + 1) {
                throw new RuntimeException();
            }
            for (int i = 1; i < n + 1; i++) {
                hash[i]=(hash[i-1]*fact+s.charAt(i-1))%mod;
            }
        }

        //[L,R)
        public long hash(int l, int r) {
            return (hash[r]-hash[l]*p[r-l]%mod+mod)%mod;
        }

        public long concatHash(StringHash hashB, char sep) {
            return concatHash(hashB, sep, 0, len, 0, hashB.len);
        }

        // [l,r)
        public long concatHash(StringHash hashB, char sep,
                               int la, int ra, int lb, int rb) {
            int lenB = rb - lb;
            if (sep == 0) {
                return  (p[lenB] * hash(la, ra) % mod + hashB.hash(lb, rb)) % mod;
            } else {
                return (p[lenB+1] * hash(la, ra) % mod + p[lenB] * sep % mod + hashB.hash(lb, rb)) % mod;
            }
        }
    }

    static long a = 191;
    static long[] mods = new long[] {1_000_000_007, 989_947_373, 100_000_837, 785_987_567};
    static int nMod = mods.length;
    static long[][]ps;

    static void prepare(int MAX_LEN) {
        MAX_LEN++;
        ps = new long[nMod][];
        for (int i = 0; i < nMod; i++) {
            long m = mods[i];
            ps[i] = new long[MAX_LEN];
            ps[i][0]=1;
            for (int j = 1; j < MAX_LEN; j++) {
                ps[i][j]=ps[i][j-1]*a%m;
            }
        }
    }

    static {
        //prepare((int) 1e6);
    }

    StringHash[] hs;
    int len;

    public MultiStringHash(String s) {
        if (ps == null) throw new RuntimeException("Not prepared!");
        len = s.length();
        hs = new StringHash[nMod];
        for (int i = 0; i < nMod; i++) {
            hs[i] = new StringHash(s, a, mods[i], ps[i]);
        }
    }

    //[L,R)
    public String hash(int l, int r) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < nMod; i++) {
            sb.append(hs[i].hash(l,r));
            sb.append('-');
        }
        return sb.toString();
    }

    public String concatHash(MultiStringHash hashB, char sep) {
        return concatHash(hashB, sep, 0, len, 0, hashB.len);
    }

    // [l,r)
    public String concatHash(MultiStringHash hashB, char sep,
                           int la, int ra, int lb, int rb) {
        int lenB = rb - lb;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < nMod; i++) {
            if (sep == 0) {
                sb.append((ps[i][lenB] * hs[i].hash(la, ra) % mods[i] + hashB.hs[i].hash(lb, rb)) % mods[i]);
            } else {
                sb.append((ps[i][lenB+1] * hs[i].hash(la, ra) % mods[i] + ps[i][lenB] * sep % mods[i] + hashB.hs[i].hash(lb, rb)) % mods[i]);
            }
            sb.append('-');
        }
        return sb.toString();
    }
}
