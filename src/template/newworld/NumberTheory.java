package template.newworld;

import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Deque;
import java.util.function.Supplier;
import java.io.OutputStreamWriter;
import java.io.UncheckedIOException;
import java.util.function.Consumer;
import java.io.Closeable;
import java.io.Writer;
import java.util.ArrayDeque;

/**
 * 洛谷：dalt
 * https://www.luogu.com.cn/record/46032014
 * https://www.luogu.com.cn/user/107185
 * Built using CHelper plug-in
 * Actual solution is at the top
 */
class NumberTheory {

    static interface InverseNumber {
    }

    static class Buffer<T> {
        private Deque<T> deque;
        private Supplier<T> supplier;
        private Consumer<T> cleaner;
        private int allocTime;
        private int releaseTime;

        public Buffer(Supplier<T> supplier) {
            this(supplier, (x) -> {
            });
        }

        public Buffer(Supplier<T> supplier, Consumer<T> cleaner) {
            this(supplier, cleaner, 0);
        }

        public Buffer(Supplier<T> supplier, Consumer<T> cleaner, int exp) {
            this.supplier = supplier;
            this.cleaner = cleaner;
            deque = new ArrayDeque<>(exp);
        }

        public T alloc() {
            allocTime++;
            return deque.isEmpty() ? supplier.get() : deque.removeFirst();
        }

        public void release(T e) {
            releaseTime++;
            cleaner.accept(e);
            deque.addLast(e);
        }

        public void check() {
            if (allocTime != releaseTime) {
                throw new IllegalStateException("Buffer alloc " + allocTime + " but release " + releaseTime);
            }
        }

    }

    static class IntExtGCDObject {
        private int[] xy = new int[2];

        public int extgcd(int a, int b) {
            return ExtGCD.extGCD(a, b, xy);
        }

        public int getX() {
            return xy[0];
        }

    }

    static class FastFourierTransform {
        private static double[][] realLevels = new double[30][];
        private static double[][] imgLevels = new double[30][];

        private static void prepareLevel(int i) {
            if (realLevels[i] == null) {
                realLevels[i] = new double[1 << i];
                imgLevels[i] = new double[1 << i];
                for (int j = 0, s = 1 << i; j < s; j++) {
                    realLevels[i][j] = Math.cos(Math.PI / s * j);
                    imgLevels[i][j] = Math.sin(Math.PI / s * j);
                }
            }
        }

        public static void fft(double[][] p, boolean inv) {
            int m = Log2.ceilLog(p[0].length);
            int n = 1 << m;
            int shift = 32 - Integer.numberOfTrailingZeros(n);
            for (int i = 1; i < n; i++) {
                int j = Integer.reverse(i << shift);
                if (i < j) {
                    SequenceUtils.swap(p[0], i, j);
                    SequenceUtils.swap(p[1], i, j);
                }
            }

            double[][] t = new double[2][1];
            for (int d = 0; d < m; d++) {
                int s = 1 << d;
                int s2 = s << 1;
                prepareLevel(d);
                for (int i = 0; i < n; i += s2) {
                    for (int j = 0; j < s; j++) {
                        int a = i + j;
                        int b = a + s;
                        mul(realLevels[d][j], imgLevels[d][j], p[0][b], p[1][b], t, 0);
                        sub(p[0][a], p[1][a], t[0][0], t[1][0], p, b);
                        add(p[0][a], p[1][a], t[0][0], t[1][0], p, a);
                    }
                }
            }

            if (inv) {
                for (int i = 0, j = 0; i <= j; i++, j = n - i) {
                    double a = p[0][j];
                    double b = p[1][j];
                    div(p[0][i], p[1][i], n, p, j);
                    if (i != j) {
                        div(a, b, n, p, i);
                    }
                }
            }
        }

        public static void add(double r1, double i1, double r2, double i2, double[][] r, int i) {
            r[0][i] = r1 + r2;
            r[1][i] = i1 + i2;
        }

        public static void sub(double r1, double i1, double r2, double i2, double[][] r, int i) {
            r[0][i] = r1 - r2;
            r[1][i] = i1 - i2;
        }

        public static void mul(double r1, double i1, double r2, double i2, double[][] r, int i) {
            r[0][i] = r1 * r2 - i1 * i2;
            r[1][i] = r1 * i2 + i1 * r2;
        }

        public static void div(double r1, double i1, double r2, double[][] r, int i) {
            r[0][i] = r1 / r2;
            r[1][i] = i1 / r2;
        }

    }

    static class IntPolyMTT extends IntPolyFFT {
        static int[] mods = new int[]{469762049, 998244353, 167772161};
        static IntPolyNTT[] ntts = new IntPolyNTT[]{
                new IntPolyNTT(mods[0]),
                new IntPolyNTT(mods[1]),
                new IntPolyNTT(mods[2])
        };
        static ILongModular mod2 = ILongModular.getInstance((long) mods[0] * mods[1]);
        static Power[] powers = new Power[]{
                new Power(mods[0]),
                new Power(mods[1]),
                new Power(mods[2])
        };
        static int inv10 = powers[0].inverse(mods[1]);
        static int inv01 = powers[1].inverse(mods[0]);
        static int inv012 = powers[2].inverse((int) ((long) mods[0] * mods[1] % mods[2]));
        static long p1inv10 = (long) mods[1] * inv10;
        static long p0inv01 = (long) mods[0] * inv01;

        public IntPolyMTT(int mod) {
            super(mod);
        }

        public int[] convolution(int[] a, int[] b) {
            if (Math.min(a.length, b.length) < (int) 2e5) {
                return super.convolution(a, b);
            }
            int rA = rankOf(a);
            int rB = rankOf(b);
            if (Math.min(rA,rB) < MMT_THRESHOLD) {
                return mulBF(a, b);
            }
            int[] c0 = ntts[0].convolution(a, b);
            int[] c1 = ntts[1].convolution(a, b);
            int[] c2 = ntts[2].convolution(a, b);
            int maxlen = Math.max(c0.length, c1.length);
            maxlen = Math.max(maxlen, c2.length);
            int[] ans = PrimitiveBuffers.allocIntPow2(maxlen);
            for (int i = 0; i < maxlen; i++) {
                int a0 = i < c0.length ? c0[i] : 0;
                int a1 = i < c1.length ? c1[i] : 0;
                int a2 = i < c2.length ? c2[i] : 0;
                long t0 = mod2.plus(mod2.mul(a0, p1inv10),
                        mod2.mul(a1, p0inv01));
                long t1 = DigitUtils.mod(a2 - t0, mods[2]) * (long) inv012 % mods[2];
                ans[i] = DigitUtils.mod(t1 * mods[0] % mod * mods[1] + t0, mod);
            }
            PrimitiveBuffers.release(c0, c1, c2);
            return ans;
        }

    }

    static class LongModular implements ILongModular {
        final long m;

        public long getMod() {
            return m;
        }

        public LongModular(long m) {
            this.m = m;
        }

        public long mul(long a, long b) {
            if (b == 0) {
                return 0;
            }
            long ans = mul(a, b >> 1) << 1;
            if (ans >= m) {
                ans -= m;
            }
            ans += a * (b & 1);
            if (ans >= m) {
                ans -= m;
            }
            return ans;
        }

    }

    static class NumberTheoryTransform {
        public static void ntt(int[] p, boolean inv, int mod, int g, Power power) {
            int m = Log2.ceilLog(p.length);
            int n = 1 << m;

            int shift = 32 - Integer.numberOfTrailingZeros(n);
            for (int i = 1; i < n; i++) {
                int j = Integer.reverse(i << shift);
                if (i < j) {
                    int temp = p[i];
                    p[i] = p[j];
                    p[j] = temp;
                }
            }

            int[] ws = PrimitiveBuffers.allocIntPow2(n / 2);
            int unit = power.pow(g, (mod - 1) / n);
            ws[0] = 1;
            for (int i = 1; i < ws.length; i++) {
                ws[i] = (int) ((long) ws[i - 1] * unit % mod);
            }

            for (int d = 0; d < m; d++) {
                //int w1 = power.pow(g, (mod - 1) >> 1 + d);
                //w1=g^{(mod-1)/2^{1+d}}
                //int w1 = ws[d];
                int s = 1 << d;
                int s2 = s << 1;
                int right = n >> (1 + d);
                for (int i = 0; i < n; i += s2) {
                    //int w = 1;
                    for (int j = 0; j < s; j++) {
                        int a = i + j;
                        int b = a + s;
                        int t = (int) ((long) ws[j * right] * p[b] % mod);
                        p[b] = p[a] < t ? p[a] + mod - t : p[a] - t;
                        p[a] = p[a] + t >= mod ? p[a] + t - mod : p[a] + t;
                        //w = g^{j (mod-1)/2^{1+d}}
                        //unit = g^{(mod-1)/n}
                        //w = unit^{j n/2^{1+d}}
                        //w = (int) ((long) w * w1 % mod);
                    }
                }
            }

            PrimitiveBuffers.release(ws);
            if (inv) {
                long invN = power.inverse(n);
                for (int i = 0, j = 0; i <= j; i++, j = n - i) {
                    int a = p[j];
                    p[j] = (int) (p[i] * invN % mod);
                    if (i != j) {
                        p[i] = (int) (a * invN % mod);
                    }
                }
            }
        }

    }

    static class Log2 {
        public static int ceilLog(int x) {
            if (x <= 0) {
                return 0;
            }
            return 32 - Integer.numberOfLeadingZeros(x - 1);
        }

    }

    static class SequenceUtils {
        public static void swap(int[] data, int i, int j) {
            int tmp = data[i];
            data[i] = data[j];
            data[j] = tmp;
        }

        public static void swap(double[] data, int i, int j) {
            double tmp = data[i];
            data[i] = data[j];
            data[j] = tmp;
        }

        public static boolean equal(int[] a, int al, int ar, int[] b, int bl, int br) {
            if ((ar - al) != (br - bl)) {
                return false;
            }
            for (int i = al, j = bl; i <= ar; i++, j++) {
                if (a[i] != b[j]) {
                    return false;
                }
            }
            return true;
        }

    }

    static class LongModularDanger implements ILongModular {
        final long m;

        public long getMod() {
            return m;
        }

        public LongModularDanger(long m) {
            this.m = m;
        }

        public long mul(long a, long b) {
            return DigitUtils.modmul(a, b, m);
        }

    }

    static class ExtGCD {
        public static int extGCD(int a, int b, int[] xy) {
            if (a >= b) {
                return extGCD0(a, b, xy);
            }
            int ans = extGCD0(b, a, xy);
            SequenceUtils.swap(xy, 0, 1);
            return ans;
        }

        private static int extGCD0(int a, int b, int[] xy) {
            if (b == 0) {
                xy[0] = 1;
                xy[1] = 0;
                return a;
            }
            int ans = extGCD0(b, a % b, xy);
            int x = xy[0];
            int y = xy[1];
            xy[0] = y;
            xy[1] = x - a / b * y;
            return ans;
        }

    }

    static class IntegerArrayList implements Cloneable {
        private int size;
        private int cap;
        private int[] data;
        private static final int[] EMPTY = new int[0];

        public IntegerArrayList(int cap) {
            this.cap = cap;
            if (cap == 0) {
                data = EMPTY;
            } else {
                data = new int[cap];
            }
        }

        public IntegerArrayList(int[] data) {
            this(0);
            addAll(data);
        }

        public IntegerArrayList(IntegerArrayList list) {
            this.size = list.size;
            this.cap = list.cap;
            this.data = Arrays.copyOf(list.data, size);
        }

        public IntegerArrayList() {
            this(0);
        }

        public void ensureSpace(int req) {
            if (req > cap) {
                while (cap < req) {
                    cap = Math.max(cap + 10, 2 * cap);
                }
                data = Arrays.copyOf(data, cap);
            }
        }

        public void add(int x) {
            ensureSpace(size + 1);
            data[size++] = x;
        }

        public void addAll(int[] x) {
            addAll(x, 0, x.length);
        }

        public void addAll(int[] x, int offset, int len) {
            ensureSpace(size + len);
            System.arraycopy(x, offset, data, size, len);
            size += len;
        }

        public void addAll(IntegerArrayList list) {
            addAll(list.data, 0, list.size);
        }

        public int[] toArray() {
            return Arrays.copyOf(data, size);
        }

        public String toString() {
            return Arrays.toString(toArray());
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof IntegerArrayList)) {
                return false;
            }
            IntegerArrayList other = (IntegerArrayList) obj;
            return SequenceUtils.equal(data, 0, size - 1, other.data, 0, other.size - 1);
        }

        public int hashCode() {
            int h = 1;
            for (int i = 0; i < size; i++) {
                h = h * 31 + Integer.hashCode(data[i]);
            }
            return h;
        }

        public IntegerArrayList clone() {
            IntegerArrayList ans = new IntegerArrayList();
            ans.addAll(this);
            return ans;
        }

    }

    static class DigitUtils {
        private DigitUtils() {
        }

        public static long round(double x) {
            if (x >= 0) {
                return (long) (x + 0.5);
            } else {
                return (long) (x - 0.5);
            }
        }

        public static int mod(long x, int mod) {
            if (x < -mod || x >= mod) {
                x %= mod;
            }
            if (x < 0) {
                x += mod;
            }
            return (int) x;
        }

        public static int mod(int x, int mod) {
            if (x < -mod || x >= mod) {
                x %= mod;
            }
            if (x < 0) {
                x += mod;
            }
            return x;
        }

        public static long modplus(long a, long b, long mod) {
            long ans = a + b;
            if (ans >= mod) {
                ans -= mod;
            }
            return ans;
        }

        public static long mod(long x, long mod) {
            if (x < -mod || x >= mod) {
                x %= mod;
            }
            if (x < 0) {
                x += mod;
            }
            return x;
        }

        public static long modmul(long a, long b, long mod) {
            long k = DigitUtils.round((double) a / mod * b);
            return DigitUtils.mod(a * b - k * mod, mod);
        }

    }

    static interface ILongModular {
        long getMod();

        default long plus(long a, long b) {
            return DigitUtils.modplus(a, b, getMod());
        }

        long mul(long a, long b);

        static ILongModular getInstance(long mod) {
            if (mod <= (1L << 50)) {
                return new LongModularDanger(mod);
            }
            if (mod == (1L << 61) - 1) {
                return LongModular2305843009213693951.getInstance();
            }
            return new LongModular(mod);//new BigLongModular(mod);//new LongModular(mod);
        }

    }

    static class IntPoly {
        protected int mod;
        protected Power power;

        public IntPoly(int mod) {
            this.mod = mod;
            this.power = new Power(mod);
        }

        public int[] convolution(int[] a, int[] b) {
            return mulBF(a, b);
        }

        public int[] dotMul(int[] a, int[] b) {
            assert a.length == b.length;
            int[] c = PrimitiveBuffers.allocIntPow2(a.length);
            for (int i = 0; i < a.length; i++) {
                c[i] = (int) ((long) a[i] * b[i] % mod);
            }
            return c;
        }

        public int rankOf(int[] p) {
            int r = p.length - 1;
            while (r >= 0 && p[r] == 0) {
                r--;
            }
            return Math.max(0, r);
        }

        public int[] mulBF(int[] a, int[] b) {
            int rA = rankOf(a);
            int rB = rankOf(b);
            if (rA > rB) {
                {
                    int tmp = rA;
                    rA = rB;
                    rB = tmp;
                }
                {
                    int[] tmp = a;
                    a = b;
                    b = tmp;
                }
            }
            int[] c = PrimitiveBuffers.allocIntPow2(rA + rB + 1);
            for (int i = 0; i <= rA; i++) {
                for (int j = 0; j <= rB; j++) {
                    c[i + j] = (int) ((c[i + j] + (long) a[i] * b[j]) % mod);
                }
            }
            return c;
        }

    }

    static class IntPolyNTT extends IntPoly {
        protected int g;
        static final int NTT_THRESHOLD = 50;

        public IntPolyNTT(int mod) {
            super(mod);
            g = PrimitiveRoot.findAnyRoot(mod);
        }

        public int[] convolution(int[] a, int[] b) {
            int rA = rankOf(a);
            int rB = rankOf(b);
            if (Math.min(rA, rB) <= NTT_THRESHOLD) {
                return mulBF(a, b);
            }

            if (a != b) {
                a = PrimitiveBuffers.allocIntPow2(a, rA + rB + 1);
                b = PrimitiveBuffers.allocIntPow2(b, rA + rB + 1);
                NumberTheoryTransform.ntt(a, false, mod, g, power);
                NumberTheoryTransform.ntt(b, false, mod, g, power);
                int[] c = PrimitiveBuffers.replace(dotMul(a, b), a, b);
                NumberTheoryTransform.ntt(c, true, mod, g, power);
                return c;
            } else {
                a = PrimitiveBuffers.allocIntPow2(a, rA + rB + 1);
                NumberTheoryTransform.ntt(a, false, mod, g, power);
                int[] c = PrimitiveBuffers.replace(dotMul(a, a), a);
                NumberTheoryTransform.ntt(c, true, mod, g, power);
                return c;
            }
        }

    }

    static class Factorization {
        public static IntegerArrayList factorizeNumberPrime(int x) {
            IntegerArrayList ans = new IntegerArrayList();
            for (int i = 2; i * i <= x; i++) {
                if (x % i != 0) {
                    continue;
                }
                ans.add(i);
                while (x % i == 0) {
                    x /= i;
                }
            }
            if (x > 1) {
                ans.add(x);
            }
            return ans;
        }

    }

    static class Power implements InverseNumber {
        static IntExtGCDObject extGCD = new IntExtGCDObject();
        int mod;

        public Power(int mod) {
            this.mod = mod;
        }

        public int pow(int x, int n) {
            if (n == 0) {
                return 1 % mod;
            }
            long r = pow(x, n >> 1);
            r = r * r % mod;
            if ((n & 1) == 1) {
                r = r * x % mod;
            }
            return (int) r;
        }

        public int inverse(int x) {
            int ans = inverseExtGCD(x);
//        if(modular.mul(ans, x) != 1){
//            throw new IllegalStateException();
//        }
            return ans;
        }

        public int inverseExtGCD(int x) {
            if (extGCD.extgcd(x, mod) != 1) {
                throw new IllegalArgumentException();
            }
            return DigitUtils.mod(extGCD.getX(), mod);
        }

    }

    static class LongModular2305843009213693951 implements ILongModular {
        private static long mod = 2305843009213693951L;
        private static final LongModular2305843009213693951 INSTANCE = new LongModular2305843009213693951();
        private static long mask = (1L << 32) - 1;

        private LongModular2305843009213693951() {
        }

        public static final LongModular2305843009213693951 getInstance() {
            return INSTANCE;
        }

        public long getMod() {
            return mod;
        }

        public long mul(long a, long b) {
            long l1 = a & mask;
            long h1 = (a >> 32) & mask;
            long l2 = b & mask;
            long h2 = (b >> 32) & mask;
            long l = l1 * l2;
            long m = l1 * h2 + l2 * h1;
            long h = h1 * h2;
            long ret = (l & mod) + (l >>> 61) + (h << 3) + (m >>> 29) + ((m << 35) >>> 3) + 1;
            ret = (ret & mod) + (ret >>> 61);
            ret = (ret & mod) + (ret >>> 61);
            return ret - 1;
        }

    }

    static class PrimitiveRoot {
        private int[] factors;
        private int mod;
        private Power pow;
        private int phi;
        private static HashMap<Integer, Integer> root = new HashMap<>();

        public static int findAnyRoot(int x) {
            if (!root.containsKey(x)) {
                root.put(x, new PrimitiveRoot(x).findMinPrimitiveRoot());
            }
            return root.get(x);
        }

        public PrimitiveRoot(int x) {
            phi = x - 1;
            mod = x;
            pow = new Power(mod);
            factors = Factorization.factorizeNumberPrime(phi).toArray();
        }

        public int findMinPrimitiveRoot() {
            if (mod == 2) {
                return 1;
            }
            return findMinPrimitiveRoot(2);
        }

        private int findMinPrimitiveRoot(int since) {
            for (int i = since; i < mod; i++) {
                boolean flag = true;
                for (int f : factors) {
                    if (pow.pow(i, phi / f) == 1) {
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    return i;
                }
            }
            return -1;
        }

    }

    static class PrimitiveBuffers {
        public static Buffer<int[]>[] intPow2Bufs = new Buffer[30];
        public static Buffer<double[]>[] doublePow2Bufs = new Buffer[30];

        static {
            for (int i = 0; i < 30; i++) {
                int finalI = i;
                intPow2Bufs[i] = new Buffer<>(() -> new int[1 << finalI], x -> Arrays.fill(x, 0));
                doublePow2Bufs[i] = new Buffer<>(() -> new double[1 << finalI], x -> Arrays.fill(x, 0));
            }
        }

        public static void check() {
            for (int i = 0; i < 30; i++) {
                try {
                    intPow2Bufs[i].check();
                    doublePow2Bufs[i].check();
                } catch (IllegalStateException e) {
                    throw new IllegalArgumentException("Alloc more element of size " + i, e);
                }
            }
        }

        public static int[] allocIntPow2(int n) {
            return intPow2Bufs[Log2.ceilLog(n)].alloc();
        }

        public static int[] allocIntPow2(int[] data, int newLen) {
            int[] ans = allocIntPow2(newLen);
            System.arraycopy(data, 0, ans, 0, Math.min(data.length, newLen));
            return ans;
        }

        public static void release(int[] data) {
            intPow2Bufs[Log2.ceilLog(data.length)].release(data);
        }

        public static void release(int[] a, int[] b, int[] c) {
            release(a);
            release(b);
            release(c);
        }

        public static double[] allocDoublePow2(int n) {
            return doublePow2Bufs[Log2.ceilLog(n)].alloc();
        }

        public static double[] allocDoublePow2(double[] data, int newLen) {
            double[] ans = allocDoublePow2(newLen);
            System.arraycopy(data, 0, ans, 0, Math.min(data.length, newLen));
            return ans;
        }

        public static void release(double[] data) {
            doublePow2Bufs[Log2.ceilLog(data.length)].release(data);
        }

        public static void release(double[]... data) {
            for (double[] x : data) {
                release(x);
            }
        }

        public static int[] replace(int[] a, int[] b) {
            release(b);
            return a;
        }

        public static int[] replace(int[] a, int[] b0, int[] b1) {
            release(b0);
            release(b1);
            return a;
        }

    }

    static class IntPolyFFT extends IntPoly {
        static final int FFT_THRESHOLD = 50;
        static final int MMT_THRESHOLD = 500;

        public IntPolyFFT(int mod) {
            super(mod);
        }

        public int[] convolution(int[] a, int[] b) {
            if (a != b) {
                return multiplyMod(a, b);
            } else {
                return pow2(a);
            }
        }

        public int[] pow2(int[] a) {
            int rA = rankOf(a);
            if (rA < FFT_THRESHOLD) {
                return mulBF(a, a);
            }

            int need = rA * 2 + 1;

            double[] aReal = PrimitiveBuffers.allocDoublePow2(need);
            double[] aImag = PrimitiveBuffers.allocDoublePow2(need);
            int n = aReal.length;

            for (int i = 0; i <= rA; i++) {
                int x = DigitUtils.mod(a[i], mod);
                aReal[i] = x & ((1 << 15) - 1);
                aImag[i] = x >> 15;
            }
            FastFourierTransform.fft(new double[][]{aReal, aImag}, false);

            double[] bReal = PrimitiveBuffers.allocDoublePow2(aReal, aReal.length);
            double[] bImag = PrimitiveBuffers.allocDoublePow2(aImag, bReal.length);


            for (int i = 0, j = 0; i <= j; i++, j = n - i) {
                double ari = aReal[i];
                double aii = aImag[i];
                double bri = bReal[i];
                double bii = bImag[i];
                double arj = aReal[j];
                double aij = aImag[j];
                double brj = bReal[j];
                double bij = bImag[j];

                double a1r = (ari + arj) / 2;
                double a1i = (aii - aij) / 2;
                double a2r = (aii + aij) / 2;
                double a2i = (arj - ari) / 2;

                double b1r = (bri + brj) / 2;
                double b1i = (bii - bij) / 2;
                double b2r = (bii + bij) / 2;
                double b2i = (brj - bri) / 2;

                aReal[i] = a1r * b1r - a1i * b1i - a2r * b2i - a2i * b2r;
                aImag[i] = a1r * b1i + a1i * b1r + a2r * b2r - a2i * b2i;
                bReal[i] = a1r * b2r - a1i * b2i + a2r * b1r - a2i * b1i;
                bImag[i] = a1r * b2i + a1i * b2r + a2r * b1i + a2i * b1r;

                if (i != j) {
                    a1r = (arj + ari) / 2;
                    a1i = (aij - aii) / 2;
                    a2r = (aij + aii) / 2;
                    a2i = (ari - arj) / 2;

                    b1r = (brj + bri) / 2;
                    b1i = (bij - bii) / 2;
                    b2r = (bij + bii) / 2;
                    b2i = (bri - brj) / 2;

                    aReal[j] = a1r * b1r - a1i * b1i - a2r * b2i - a2i * b2r;
                    aImag[j] = a1r * b1i + a1i * b1r + a2r * b2r - a2i * b2i;
                    bReal[j] = a1r * b2r - a1i * b2i + a2r * b1r - a2i * b1i;
                    bImag[j] = a1r * b2i + a1i * b2r + a2r * b1i + a2i * b1r;
                }
            }

            FastFourierTransform.fft(new double[][]{aReal, aImag}, true);
            FastFourierTransform.fft(new double[][]{bReal, bImag}, true);

            int[] ans = PrimitiveBuffers.allocIntPow2(need);
            for (int i = 0; i < need; i++) {
                long aa = DigitUtils.mod(Math.round(aReal[i]), mod);
                long bb = DigitUtils.mod(Math.round(bReal[i]), mod);
                long cc = DigitUtils.mod(Math.round(aImag[i]), mod);
                ans[i] = DigitUtils.mod(aa + (bb << 15) + (cc << 30), mod);
            }

            PrimitiveBuffers.release(aReal, bReal, aImag, bImag);
            return ans;
        }

        private int[] multiplyMod(int[] a, int[] b) {
            int rA = rankOf(a);
            int rB = rankOf(b);
            if (Math.min(rA, rB) < FFT_THRESHOLD) {
                return mulBF(a, b);
            }

            int need = rA + rB + 1;

            double[] aReal = PrimitiveBuffers.allocDoublePow2(need);
            double[] aImag = PrimitiveBuffers.allocDoublePow2(need);
            int n = aReal.length;

            for (int i = 0; i <= rA; i++) {
                int x = DigitUtils.mod(a[i], mod);
                aReal[i] = x & ((1 << 15) - 1);
                aImag[i] = x >> 15;
            }
            FastFourierTransform.fft(new double[][]{aReal, aImag}, false);

            double[] bReal = PrimitiveBuffers.allocDoublePow2(need);
            double[] bImag = PrimitiveBuffers.allocDoublePow2(need);
            for (int i = 0; i <= rB; i++) {
                int x = DigitUtils.mod(b[i], mod);
                bReal[i] = x & ((1 << 15) - 1);
                bImag[i] = x >> 15;
            }
            FastFourierTransform.fft(new double[][]{bReal, bImag}, false);


            for (int i = 0, j = 0; i <= j; i++, j = n - i) {
                double ari = aReal[i];
                double aii = aImag[i];
                double bri = bReal[i];
                double bii = bImag[i];
                double arj = aReal[j];
                double aij = aImag[j];
                double brj = bReal[j];
                double bij = bImag[j];

                double a1r = (ari + arj) / 2;
                double a1i = (aii - aij) / 2;
                double a2r = (aii + aij) / 2;
                double a2i = (arj - ari) / 2;

                double b1r = (bri + brj) / 2;
                double b1i = (bii - bij) / 2;
                double b2r = (bii + bij) / 2;
                double b2i = (brj - bri) / 2;

                aReal[i] = a1r * b1r - a1i * b1i - a2r * b2i - a2i * b2r;
                aImag[i] = a1r * b1i + a1i * b1r + a2r * b2r - a2i * b2i;
                bReal[i] = a1r * b2r - a1i * b2i + a2r * b1r - a2i * b1i;
                bImag[i] = a1r * b2i + a1i * b2r + a2r * b1i + a2i * b1r;

                if (i != j) {
                    a1r = (arj + ari) / 2;
                    a1i = (aij - aii) / 2;
                    a2r = (aij + aii) / 2;
                    a2i = (ari - arj) / 2;

                    b1r = (brj + bri) / 2;
                    b1i = (bij - bii) / 2;
                    b2r = (bij + bii) / 2;
                    b2i = (bri - brj) / 2;

                    aReal[j] = a1r * b1r - a1i * b1i - a2r * b2i - a2i * b2r;
                    aImag[j] = a1r * b1i + a1i * b1r + a2r * b2r - a2i * b2i;
                    bReal[j] = a1r * b2r - a1i * b2i + a2r * b1r - a2i * b1i;
                    bImag[j] = a1r * b2i + a1i * b2r + a2r * b1i + a2i * b1r;
                }
            }

            FastFourierTransform.fft(new double[][]{aReal, aImag}, true);
            FastFourierTransform.fft(new double[][]{bReal, bImag}, true);

            int[] ans = PrimitiveBuffers.allocIntPow2(need);
            for (int i = 0; i < need; i++) {
                long aa = DigitUtils.mod(Math.round(aReal[i]), mod);
                long bb = DigitUtils.mod(Math.round(bReal[i]), mod);
                long cc = DigitUtils.mod(Math.round(aImag[i]), mod);
                ans[i] = DigitUtils.mod(aa + (bb << 15) + (cc << 30), mod);
            }

            PrimitiveBuffers.release(aReal, bReal, aImag, bImag);
            return ans;
        }

    }
}
