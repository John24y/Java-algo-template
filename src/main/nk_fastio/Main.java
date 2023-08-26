package main.nk_fastio;

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
    TreeSet<Integer> set=new TreeSet<>();
    void fac(int x) {
        for (int i = 1; i*i <= x; i++) {
            if (x%i==0) {
                set.add(x%i);
                set.add(i);
            }
        }
    }

    void solve() {
        int a=readInt(),b=readInt();
        fac(a);
        fac(b);
        out.println(set.size());
        for (Integer e : set) {
            out.print(e+" ");
        }
    }

    public static void main(String[] args) throws Exception {
        new Main().solve();
        out.flush();
    }
    static FastInput in = new FastInput(System.in);
    static PrintWriter out = new PrintWriter(System.out, false);
    static String readString() { return in.readString(); }
    static int readInt() { return in.readInt(); }
    static long readLong() { return in.readLong(); }
    static void solveT() { int t=readInt(); for (int i = 0; i < t; i++) new Main().solve(); }
}

