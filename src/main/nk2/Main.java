package main.nk2;

import java.io.*;
import java.util.*;

public class Main {
    int M = 998244353;
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

    List<Integer> fac(int n) {
        List<Integer> res=new ArrayList<>();
        int p=2,x=n;
        while (p*p<=n){
            if (x%p==0){
                res.add(p);
            }
            while (x%p==0){
                x/=p;
            }
            p++;
        }
        if (x!=1&&x!=n){
            res.add(x);
        }
        return res;
    }

    public void solve() throws Exception {
        int n=nextInt();
        String s=next();
        int cdot=0;
        for (int i = 0; i < n; i++) {
            if (s.charAt(i)=='.') cdot++;
        }
        List<Integer> fac=fac(n);
        long res=1;
        if (cdot==0) {
            res++;
        }
        for (Integer f : fac) {
            boolean[] cover=new boolean[f];
            for (int i = 0; i < f; i++) {
                for (int j = i; j < n; j+=f) {
                    cover[i]|=s.charAt(j)=='.';
                }
            }
            int uc=0;
            for (int i = 0; i < f; i++) {
                if (!cover[i])uc++;
            }
            res = (res+ksm(2,uc,M)+M-1-(uc==f?1:0))%M;
        }
        System.out.println(res);
    }

    public static void main(String[] args) throws Exception {
//        int t = nextInt();
//        for (int i = 0; i < t; i++) {
//        }
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

