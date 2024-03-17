package main.at2;
//
//import java.io.*;
//import java.util.*;
//import java.util.concurrent.ThreadLocalRandom;
//
//class StringHash {
//    long[] p;
//    long fact;
//    long mod;
//    long[] hash;
//    int len;
//
//    public StringHash(String s, long fact, long mod, long[] p) {
//        int n = s.length();
//        this.len = n;
//        this.hash=new long[n+1];
//        this.fact = fact;
//        this.mod = mod;
//        this.p = p;
//        if (p.length < n + 1) {
//            throw new RuntimeException();
//        }
//        for (int i = 1; i < n + 1; i++) {
//            hash[i]=(hash[i-1]*fact+s.charAt(i-1))%mod;
//        }
//    }
//
//    //[L,R)
//    public long hash(int l, int r) {
//        return (hash[r]-hash[l]*p[r-l]%mod+mod)%mod;
//    }
//
//    public long concatHash(StringHash hashB, char sep) {
//        return concatHash(hashB, sep, 0, len, 0, hashB.len);
//    }
//
//    // [l,r)
//    public long concatHash(StringHash hashB, char sep,
//                           int la, int ra, int lb, int rb) {
//        int lenB = rb - lb;
//        if (sep == 0) {
//            return  (p[lenB] * hash(la, ra) % mod + hashB.hash(lb, rb)) % mod;
//        } else {
//            return (p[lenB+1] * hash(la, ra) % mod + p[lenB] * sep % mod + hashB.hash(lb, rb)) % mod;
//        }
//    }
//}
//
//class MultiStringHash {
//
//    StringHash[] hs;
//
//    static long a = 17;
//    static long[] mods = new long[] {};
//
//    public MultiStringHash(String s) {
//        new StringHash(s, 17, );
//    }
//
//    static void preCompute(int MAX_LEN) {
//        p1=new long[MAX_LEN];
//        p2=new long[MAX_LEN];
//        p1[0]=1;
//        p2[0]=1;
//        for (int i = 1; i < MAX_LEN; i++) {
//            p1[i]=p1[i-1]*fact1%mod;
//            p2[i]=p2[i-1]*fact2%mod;
//        }
//    }
//}
//
//public class Main implements Runnable {
//    void solve() {
//        int n=nextInt();
//        String s=next();
//        StringBuilder s1=new StringBuilder();
//        StringBuilder s2=new StringBuilder();
//        int c=0;
//        int cc=0;
//        for (int i = 0; i < n; i++) {
//            if (i>0) {
//                c += s.charAt(i) == 'A' ? 1 : -1;
//                if (c > 0) s1.append('A');
//                if (c < 0) s1.append('B');
//                if (c == 0) s1.append('C');
//            }
//
//            cc+=s.charAt(i)=='A'?1:-1;
//            if (cc>0) s2.append('A');
//            if (cc<0) s2.append('B');
//            if (cc==0) s2.append('C');
//        }
//        DoubleStringHash hash1 = new DoubleStringHash(s1.toString());
//        DoubleStringHash hash2 = new DoubleStringHash(s2.toString());
//        Set<String> res=new HashSet<>();
//        for (int i = 2; i < n; i++) {
//            String h = hash1.concatHash(hash2, (char) 0,
//                    0, i-1, i-1, n);
//            res.add(h);
//        }
//        res.add(hash2.hash(0,n));
//        res.add(hash1.concatHash(new DoubleStringHash(s2.substring(n-1,n)),(char)0,0,n-1,0,1));
//        System.out.println(res.size());
//    }
//
//    public static void main(String[] args) throws Exception {
//        new Thread(null, new Main(), "CustomThread", 1024 * 1024 * 100).start();
//    }
//
//    @Override
//    public void run() {
//        new Main().solve();
//        out.flush();
//    }
//
//    static PrintWriter out = new PrintWriter(System.out, false);
//    static InputReader in = new InputReader(System.in);
//    static String next() { return in.next(); }
//    static int nextInt() { return Integer.parseInt(in.next()); }
//    static long nextLong() { return Long.parseLong(in.next()); }
//    static class InputReader {
//        public BufferedReader reader;
//        public StringTokenizer tokenizer;
//
//        public InputReader(InputStream stream) {
//            reader = new BufferedReader(new InputStreamReader(stream), 32768);
//            tokenizer = null;
//        }
//
//        public String next() {
//            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
//                try {
//                    tokenizer = new StringTokenizer(reader.readLine());
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//            return tokenizer.nextToken();
//        }
//    }
//}
//
