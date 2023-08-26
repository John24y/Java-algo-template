package main.nk;

import java.io.*;
import java.util.*;
class XorLinearBase {
    long[] p=new long[64], d=new long[64];
    int cnt=0;
    boolean zero;

    void insert(long x) {
        if (x==0) {
            zero=true;
            return;
        }
        for(int i=62;i>=0;i--){
            if((x>>i)>0) {
                if(p[i]==0) {
                    p[i]=x;
                    cnt++;
                    break;
                } else {
                    x^=p[i];
                }
            }
        }
    }

    boolean gen(long x) {
        for(int i=62;i>=0;i--) {
            if((x>>i)>0) x^=p[i];
        }
        return x==0;
    }

    long max() {
        long ans=0;
        for(int i=62;i>=0;i--){
            if((ans^p[i])>ans) ans^=p[i];
        }
        return ans;
    }

    long min() {
        if(zero) return 0;
        for(int i=0;i<=62;i++) {
            if(p[i]>0) return p[i];
        }
        return -1;
    }

    void rebuild() {
        cnt=0;
        for(int i=62;i>=0;i--) {
            for(int j=i-1;j>=0;j--) {
                if((p[i]&(1L<<j))>0) p[i]^=p[j];
            }
        }
        for(int i=0;i<=62;i++) {
            if(p[i]>0) d[cnt++]=p[i];
        }
    }

    long kth(long k) {
        if (k==1 && zero) return 0;
        if(k>=(1L<<cnt)) return -1;
        long ans=0;
        for(int i=62;i>=0;i--) {
            if((k&(1L<<i))>0) ans^=d[i];
        }
        return ans - (zero ? 1 : 0);
    }

    int rank(long x) {
        int ans = 0;
        for(int i = cnt - 1; i >= 0; i --) {
            if(x >= d[i]) {
                ans += (1 << i);
                x ^= d[i];
            }
        }
        return ans + (zero ? 1 : 0);
    }

}
public class Main {

    public void solve() throws Exception {
        int n=readInt(),m=readInt();
        long[] w=new long[n+1];
        for (int i = 0; i < m; i++) {
            int u=readInt(),v=readInt(),t=readInt();
            w[u]^=t;
            w[v]^=t;
        }
        XorLinearBase base = new XorLinearBase();
        for (int i = 1; i <= n; i++) {
            base.insert(w[i]);
        }
        out.println(base.max());
    }

    public static void main(String[] args) throws Exception {
        int t=readInt();
        for (int i = 0; i < t; i++) {
            new Main().solve();
        }
        out.flush();
    }

    static PrintWriter out = new PrintWriter(System.out, false);
    static InputReader in = new InputReader(System.in);
    static String next() { return in.next(); }
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

