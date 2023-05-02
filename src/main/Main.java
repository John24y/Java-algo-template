package main;
import java.io.*;
import java.util.*;


public class Main {
    static int mod = 998244353;
    static long modinv(long a, long m) {
        return ksm(a, m - 2, m);
    }
    static long ksm(long a, long pow, long mod) {
        long skt = 1;
        while (pow > 0) {
            if (pow % 2 != 0) {
                skt = skt * a % mod;
            }
            a = a * a % mod;
            pow = pow >> 1;
        }
        return skt % mod;
    }

    Map<Long,Long> map=new HashMap<>();
    long m1_5=modinv(5,mod)%mod;

    long dfs(long v, long n) {
        if (v==1) {
            return 1;
        }
        if (v<=0) {
            return 0;
        }
        if (map.containsKey(v)){
            return map.get(v);
        }
        long res=0;
        for (int i = 2; i <= 6; i++) {
            if (v%i==0) {
                res+=dfs(v/i, n)*m1_5%mod;
                res%=mod;
            }
        }
        map.put(v,res);
        return res;
    }

    public void solve() throws Exception {
        long n=nextLong();
        out.println(dfs(n,n));
    }

    public static void main(String[] args) throws Exception {
        new Main().solve();
    }

    static PrintWriter out = new PrintWriter(System.out, true);
    static InputReader in = new InputReader(System.in);
    static String next() { return in.next(); }
    static int nextInt() { return Integer.parseInt(in.next()); }
    static long nextLong() { return Long.parseLong(in.next()); }
    static class InputReader {
        public BufferedReader reader;
        public StringTokenizer tokenizer;

        public InputReader(InputStream stream) {
            reader = new BufferedReader(new InputStreamReader(stream), 32768);
            tokenizer = null;
        }

        public String next() {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {
                    tokenizer = new StringTokenizer(reader.readLine());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return tokenizer.nextToken();
        }
    }
}

