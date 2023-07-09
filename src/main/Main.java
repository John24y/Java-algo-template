//package main;
//
//import java.io.*;
//
//import java.io.IOException;
//
//class FastInput {
//    private final InputStream is;
//    private byte[] buf = new byte[1 << 13];
//    private int bufLen;
//    private int bufOffset;
//    private int next;
//
//    public FastInput(InputStream is) {
//        this.is = is;
//    }
//
//    public void readInt(int[] data) {
//        for (int i = 0; i < data.length; i++) {
//            data[i] = readInt();
//        }
//    }
//
//    private int read() {
//        while (bufLen == bufOffset) {
//            bufOffset = 0;
//            try {
//                bufLen = is.read(buf);
//            } catch (IOException e) {
//                bufLen = -1;
//            }
//            if (bufLen == -1) {
//                return -1;
//            }
//        }
//        return buf[bufOffset++];
//    }
//
//    public void skipBlank() {
//        while (next >= 0 && next <= 32) {
//            next = read();
//        }
//    }
//
//    public int[] readInts(int n) {
//        int[] ans = new int[n];
//        readInt(ans);
//        return ans;
//    }
//
//    public int readInt() {
//        long l = readLong();
//        if (l > Integer.MAX_VALUE || l < Integer.MIN_VALUE) throw new RuntimeException("int overflow!");
//        return (int) l;
//    }
//
//    public long readLong() {
//        boolean rev = false;
//        skipBlank();
//        if (next == '+' || next == '-') {
//            rev = next == '-';
//            next = read();
//        }
//        long val = 0;
//        while (next >= '0' && next <= '9') {
//            val = val * 10 - next + '0';
//            next = read();
//        }
//        return rev ? val : -val;
//    }
//
//    public String readString() {
//        skipBlank();
//        StringBuilder sb = new StringBuilder();
//        while (next > 32) {
//            sb.append((char) next);
//            next = read();
//        }
//        return sb.toString();
//    }
//}
//
//class FastOutput {
//    private static final int THRESHOLD = 32 << 10;
//    private final Writer os;
//    private StringBuilder cache = new StringBuilder(THRESHOLD * 2);
//
//    public FastOutput(OutputStream os) {
//        this.os = new OutputStreamWriter(os);
//    }
//
//    public FastOutput print(CharSequence csq) {
//        cache.append(csq);
//        return this;
//    }
//
//    public FastOutput print(char c) {
//        cache.append(c);
//        afterWrite();
//        return this;
//    }
//
//    public FastOutput print(int c) {
//        cache.append(c);
//        afterWrite();
//        return this;
//    }
//
//    public FastOutput print(long c) {
//        cache.append(c);
//        afterWrite();
//        return this;
//    }
//
//    public FastOutput println() {
//        print('\n');
//        flush();
//        return this;
//    }
//
//    private void afterWrite() {
//        if (cache.length() >= THRESHOLD) {
//            flush();
//        }
//    }
//
//    public FastOutput flush() {
//        try {
//            os.append(cache);
//            os.flush();
//            cache.setLength(0);
//        } catch (IOException e) {
//            throw new UncheckedIOException(e);
//        }
//        return this;
//    }
//}
//
//class FWT {
//    static final int MOD = 998244353;
//    static final int MOD_I2 = 499122177; // 2的逆元
//    static final int CAL_OR = 1;
//    static final int CAL_AND = 2;
//    static final int CAL_XOR = 3;
//
//    static int[] fwtOr(int[] a) { return fwt(a, null, CAL_OR); }
//    static int[] fwtAnd(int[] a) { return fwt(a, null, CAL_AND); }
//    static int[] fwtXor(int[] a) { return fwt(a, null, CAL_XOR); }
//    static int[] fwtOr(int[] a, int[] b) { return fwt(a, b, CAL_OR); }
//    static int[] fwtAnd(int[] a, int[] b) { return fwt(a, b, CAL_AND); }
//    static int[] fwtXor(int[] a, int[] b) { return fwt(a, b, CAL_XOR); }
//
//    static int[] fwt(int[] a, int[] b, int calType) {
//        int n = Math.max(a.length, b == null ? 0 : b.length);
//        int limit = 1;
//        // a和b的长度向上补齐到2^x，位运算变换结果可以是小于2^x的任何数
//        while (limit < n) {
//            limit <<= 1;
//        }
//        a = ensureLen(a, limit);
//        if (b!=null) b = ensureLen(b, limit);
//        if (calType == CAL_OR) {
//            or(a, true);
//            if (b!=null) or(b, true);
//            dotMul(a, b == null ? a : b, a);
//            or(a, false);
//        } else if (calType == CAL_AND) {
//            and(a, true);
//            if (b!=null) and(b, true);
//            dotMul(a, b == null ? a : b, a);
//            and(a, false);
//        } else if (calType == CAL_XOR) {
//            xor(a, true);
//            if (b!=null) xor(b, true);
//            dotMul(a, b == null ? a : b, a);
//            xor(a, false);
//        } else {
//            assert false;
//        }
//        return a;
//    }
//
//    private static void dotMul(int[] a, int[] b, int[] res) {
//        for (int i = 0; i < a.length; i++) res[i] = (int) (((long) a[i] * (long) b[i]) % MOD);
//    }
//
//    static int[] ensureLen(int[] a, int len) {
//        //if (a.length == len) return a;
//        int[] tmp = new int[len];
//        System.arraycopy(a, 0, tmp, 0, a.length);
//        return tmp;
//    }
//
//    static void or(int[] a, boolean forward) {
//        int x = forward ? 1 : MOD - 1;
//        // l-当前处理子数组长度, k=l//2
//        for (int l = 2, k = 1; l <= a.length; l <<= 1, k <<= 1)
//            for (int i = 0; i < a.length; i += l)
//                for (int j = 0; j < k; j++)
//                    a[i + j + k] = (int) ((a[i + j + k] + (long) a[i + j] * x) % MOD);
//    }
//
//    static void and(int[] a, boolean forward) {
//        int x = forward ? 1 : MOD - 1;
//        for (int l = 2, k = 1; l <= a.length; l <<= 1, k <<= 1)
//            for (int i = 0; i < a.length; i += l)
//                for (int j = 0; j < k; j++)
//                    a[i + j] = (int) ((a[i + j] + (long) a[i + j + k] * x) % MOD);
//    }
//
//    static void xor(int[] a, boolean forward) {
//        int x = forward ? 1 : MOD_I2;
//        for (int l = 2, k = 1; l <= a.length; l <<= 1, k <<= 1)
//            for (int i = 0; i < a.length; i += l)
//                for (int j = 0; j < k; j++) {
//                    long tmp1 = (long) (a[i + j] + a[i + j + k]) * x % MOD;
//                    long tmp2 = (long) (a[i + j] - a[i + j + k] + MOD) * x % MOD;
//                    a[i + j] = (int) tmp1;
//                    a[i + j + k] = (int) tmp2;
//                }
//    }
//
//}
//
//public class Main {
//    static FastInput in = new FastInput(System.in);
//    static FastOutput out = new FastOutput(System.out);
//    static String readString() { return in.readString(); }
//    static int readInt() { return in.readInt(); }
//    static long readLong() { return in.readLong(); }
//
//    public static void main(String[] args) throws Exception {
//        int m = readInt();
//        int n = 1 << m;
//        int[] a = new int[n], b = new int[n];
//        for (int i = 0; i < n; i++) a[i] = readInt();
//        for (int i = 0; i < n; i++) b[i] = readInt();
//        int[][] res = new int[][]{
//                FWT.fwtOr(a,b),
//                FWT.fwtAnd(a,b),
//                FWT.fwtXor(a,b)
//        };
//        for (int[] r : res) {
//            for (int i : r) {
//                out.print(i).print(' ');
//            }
//            out.println();
//        }
//        out.flush();
//    }
//
//}
//
