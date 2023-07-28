package main;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.*;

class FastInput {
    private final InputStream is;
    private byte[] buf = new byte[1 << 13];
    private int bufLen;
    private int bufOffset;
    private int next;

    public FastInput(InputStream is) {
        this.is = is;
    }

    private int read() {
        while (bufLen == bufOffset) {
            bufOffset = 0;
            try {
                bufLen = is.read(buf);
            } catch (IOException e) {
                bufLen = -1;
            }
            if (bufLen == -1) {
                return -1;
            }
        }
        return buf[bufOffset++];
    }

    public void skipBlank() {
        while (next >= 0 && next <= 32) {
            next = read();
        }
    }

    public int[] readInts(int n) {
        int[] ans = new int[n];
        for (int i = 0; i < n; i++) ans[i] = readInt();
        return ans;
    }

    public long[] readLongs(int n) {
        long[] ans = new long[n];
        for (int i = 0; i < n; i++) ans[i] = readLong();
        return ans;
    }

    public int readInt() {
        long l = readLong();
        if (l > Integer.MAX_VALUE || l < Integer.MIN_VALUE) throw new RuntimeException("int overflow!");
        return (int) l;
    }

    public long readLong() {
        boolean rev = false;
        skipBlank();
        if (next == '+' || next == '-') {
            rev = next == '-';
            next = read();
        }
        long val = 0;
        while (next >= '0' && next <= '9') {
            val = val * 10 - next + '0';
            next = read();
        }
        return rev ? val : -val;
    }

    public String readString() {
        skipBlank();
        StringBuilder sb = new StringBuilder();
        while (next > 32) {
            sb.append((char) next);
            next = read();
        }
        return sb.toString();
    }
}
public class Main {
    static FastInput in = new FastInput(System.in);
    static PrintWriter out = new PrintWriter(System.out, false);
    static String readString() { return in.readString(); }
    static int readInt() { return in.readInt(); }
    static long readLong() { return in.readLong(); }
    static void solveT() { int t=readInt(); for (int i = 0; i < t; i++) new Main().solve(); }
    static long modinv(long a, long m) {
        long b = m;
        long u = 1;
        long v = 0;
        long tmp = 0;

        while (b > 0) {
            long t = a / b;
            a -= t * b;
            tmp = a;
            a = b;
            b = tmp;

            u -= t * v;
            tmp = u;
            u = v;
            v = tmp;
        }

        u %= m;
        if (u < 0) u += m;
        return u;
    }

    static int mod = (int) (998244353);

    public static void main(String[] args) throws Exception {
        new Main().solve();
        out.flush();
    }

    int trans(int s, int d) {
        int ns=0;
        for (int i = 0; i <= 10; i++) {
            if ((s&(1<<i))>0){
                if (i+d<=10) {
                    ns|=1<<(i+d);
                }
            }
        }
        return (ns|s|(1<<d))&(~1);
    }

    void solve() {
        int n=readInt();
        int[] a=in.readInts(n);
        long[] dp=new long[1<<11];
        dp[1]=1;//初始状态能组合出0有1种，一轮转移后就没了
        long total=1;
        for (int i = 0; i < n; i++) {
            total*=a[i];
            total%=mod;
            long[] ndp=new long[1<<11];
            for (int s = 0; s < (1<<11); s++) {
                if (dp[s]==0) continue;
                for (int j = 1; j <= 10; j++) {
                    if (j>a[i]) break;
                    ndp[trans(s,j)]+=dp[s];
                    ndp[trans(s,j)]%=mod;
                }
                if (a[i]>10) {
                    ndp[s] += dp[s]*(a[i]-10);
                    ndp[s] %= mod;
                }
            }
            dp=ndp;
        }
        long res=0;
        for (int s = 0; s < (1<<11); s++) {
            if ((s&(1<<10))>0){
                res+=dp[s];
                res%=mod;
            }
        }
        out.println(modinv(total,mod)*res%mod);
    }
}

