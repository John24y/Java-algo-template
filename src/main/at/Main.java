package main.at;

import java.io.*;
import java.util.*;

class StringHash {
    long[] hash;
    long[] p;
    int fact;
    long mod;

    public StringHash(String s) {
        this(s,17,123458488852451L);
    }

    public StringHash(String s,int fact,long mod) {
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

    public long hash(int l, int r) {
        return (hash[r]-hash[l]*p[r-l]%mod+mod)%mod;
    }
}
public class Main {
    void solve() {
        int n=readInt(),m=readInt(),k=readInt();
        String s=read();
        StringHash hash1 = new StringHash(s);
        StringHash hash2 = new StringHash(s, 17, 1000000009);
        Map<Long,List<Integer>> cnt=new HashMap<>();
        for (int i = 0; i + m <= n; i++) {
            long h = hash1.hash(i, i + m);
            cnt.putIfAbsent(h,new ArrayList<>());
            cnt.get(h).add(i);
        }
        int res=0;
        for (List<Integer> li : cnt.values()) {
            int last=li.get(0);
            int cc=1;
            for (int i = 1; i < li.size(); i++) {
                int v=li.get(i);
                if (v>=last+m) {
                    cc++;
                    last=v;
                }
            }
            if (cc==k){
                res++;
            }
        }
        out.println(res);
    }

    public static void main(String[] args) throws Exception {
        new Main().solve();
        out.flush();
    }

    static PrintWriter out = new PrintWriter(System.out, false);
    static InputReader in = new InputReader(System.in);
    static String read() { return in.next(); }
    static int readInt() { return Integer.parseInt(in.next()); }
    static long readLong() { return Long.parseLong(in.next()); }
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

