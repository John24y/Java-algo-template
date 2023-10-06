package main.at;

import java.util.*;
import java.io.*;

class NTT {
    static final int P = 998244353, G = 3, Gi = 332748118;

    /**
     * 多项式A*B, a[i]表示多项式A的i次项系数
     *
     * @return C=A*B,c[i]表示i次项的系数
     */
    static int[] polyMul(int[] a, int[] b, int resPaddingLen) {
        int N = a.length - 1, M = b.length - 1;
        int t = N + M;
        N = Math.max(N, M);
        int limit = 1, L = 0;
        // 为什么lim要大于 n << 1，因为答案是n+m次项的，虽然a最高是n次项，但对a FFT 之后，要代入至少n+m个点
        while (limit <= (N << 1)) {
            limit <<= 1;
            ++L;
        }

        int[][] A = new int[2][limit + 1];
        int[] rev = new int[limit + 1];
        System.arraycopy(a, 0, A[0], 0, a.length);
        System.arraycopy(b, 0, A[1], 0, b.length);

        for (int i = 0; i < limit; i++) {
            rev[i] = (rev[i >> 1] >> 1) | ((i & 1) << (L - 1));
        }

        NTT(A[0], limit, 1, rev);
        NTT(A[1], limit, 1, rev);
        for (int i = 0; i < limit; i++) A[0][i] = (int) (((long) A[0][i] * (long) A[1][i]) % P);
        NTT(A[0], limit, -1, rev);

        int[] ans = new int[t + 1 + resPaddingLen];
        long inv = fastpow(limit, P - 2);
        for (int i = 0; i <= t; i++) ans[i] = (int) (((long) A[0][i] * inv) % P);
        return ans;
    }

    /**
     * 多项式A*A, a[i]表示多项式A的i次项系数
     *
     * @return C=A*A,c[i]表示i次项的系数
     */
    static int[] polySquare(int[] a, int resPaddingLen) {
        int N = a.length - 1;
        int t = N + N;
        int limit = 1, L = 0;
        while (limit <= (N << 1)) {
            limit <<= 1;
            ++L;
        }

        int[] A = new int[limit + 1];
        int[] rev = new int[limit + 1];
        System.arraycopy(a, 0, A, 0, a.length);

        for (int i = 0; i < limit; i++) {
            rev[i] = (rev[i >> 1] >> 1) | ((i & 1) << (L - 1));
        }

        NTT(A, limit, 1, rev);
        for (int i = 0; i < limit; i++) A[i] = (int) (((long) A[i] * (long) A[i]) % P);
        NTT(A, limit, -1, rev);

        int[] ans = new int[t + 1 + resPaddingLen];
        long inv = fastpow(limit, P - 2);
        for (int i = 0; i <= t; i++) ans[i] = (int) (((long) A[i] * inv) % P);
        return ans;
    }

    private static void NTT(int[] A, int limit, int type, int[] rev) {
        for (int i = 0; i < limit; i++) {
            if (i < rev[i]) {
                int temp = A[i];
                A[i] = A[rev[i]];
                A[rev[i]] = temp;
            }
        }
        for (int mid = 1; mid < limit; mid <<= 1) {
            long Wn = fastpow(type == 1 ? G : Gi, (P - 1) / (mid << 1));
            for (int j = 0; j < limit; j += (mid << 1)) {
                long w = 1;
                for (int k = 0; k < mid; k++, w = (w * Wn) % P) {
                    long x = A[j + k], y = w * (long) A[j + k + mid] % P;
                    A[j + k] = (int) ((x + y) % P);
                    A[j + k + mid] = (int) ((x - y + P) % P);
                }
            }
        }
    }

    private static long fastpow(long a, long k) {
        long base = 1;
        while (k != 0) {
            if ((k & 1) != 0)
                base = (base * a) % P;
            a = (a * a) % P;
            k >>= 1;
        }
        return base % P;
    }
}

/**
 * 十进制大数运算。假设数字长度为n，Java自带大数运算乘法复杂度O(n^2)，此类用NTT实现乘法复杂度为O(nlogn).
 * FFT可能被卡精度，需要用NTT，因为不能对数位取模（否则也不会用大数乘法了），最大的位不能超过模数，除法也不能用逆元。
 * 两个长度为n的数字相乘后，最大的位会达到 n*9*9，所以要及时进位，否则连续相乘会溢出。
 * 运算结果原地修改。
 */
class BigInt {
    
    interface PolyMul {
        int[] polyMul(int[] a, int[] b, int resPaddingLen);
        int[] polySquare(int[] a, int resPaddingLen);
    }
    
    static PolyMul polyMul = new PolyMul() {
        @Override
        public int[] polyMul(int[] a, int[] b, int resPaddingLen) {
            return NTT.polyMul(a, b, resPaddingLen);
        }

        @Override
        public int[] polySquare(int[] a, int resPaddingLen) {
            return NTT.polySquare(a, resPaddingLen);
        }
    };

    int[] digit;

    public BigInt(String s) {
        int n=s.length();
        digit=new int[n];
        for (int i = 0; i < s.length(); i++) {
            digit[i]=s.charAt(n-i-1)-'0';
        }
    }

    public BigInt(int[] digit) {
        this.digit=digit;
    }

    public BigInt add(int val) {
        assert val>=0;
        int n=digit.length;
        int rem=0;
        digit[0]+=val;
        assert digit[0]>=0;
        for (int i = 0; i < n; i++) {
            if (digit[i]>9) {
                if (i+1>=n){
                    rem=digit[i]/10;
                } else {
                    digit[i+1]+=digit[i]/10;
                }
                digit[i]%=10;
            }
        }
        if (rem>0) {
            int[] a=new int[n+12];
            System.arraycopy(digit,0,a,0,n);
            a[n]=rem;
            for (int i = 0; i < 12; i++) {
                if (a[n+i]>9){
                    a[n+i+1]+=a[n+i]/10;
                    a[n+i]%=10;
                }
            }
            this.digit=a;
        }
        return this;
    }

    public BigInt sub(int val) {
        assert val>=0;
        int n=digit.length;
        digit[0]-=val;
        for (int i = 0; i < n; i++) {
            if (digit[i]>=0) break;
            //不支持减到负数
            if (i+1>=n) throw new UnsupportedOperationException();
            int b=(-digit[i]%10);
            digit[i+1]-=1+(-digit[i]/10);
            digit[i]=b==0?0:10-b;
        }
        return this;
    }

    public BigInt mul(BigInt b) {
        digit=polyMul.polyMul(digit, b.digit, 15);
        add(0);
        return this;
    }

    public BigInt square() {
        digit=polyMul.polySquare(this.digit, 15);
        add(0);
        return this;
    }

    public BigInt div(int val) {
        assert val>=0;
        int n=digit.length,c=0;
        for (int i = n-1; i >= 0; i--) {
            c=c%val*10+digit[i];
            digit[i]=c/val;
        }
        return this;
    }

    @Override
    public String toString() {
        int h=digit.length-1;
        while (h>=0&&digit[h]==0) h--;
        if (h<0) return "0";
        StringBuilder sb=new StringBuilder();
        while (h>=0) {
            if (digit[h]>=10) {
                //toString
                return Arrays.toString(digit);
            }
            sb.append((char)('0'+digit[h]));
            h--;
        }
        return sb.toString();
    }
}

public class Main {
    public void solve() throws Exception {
        String s=next();
        BigInt n1 = new BigInt(s).add(1);
        BigInt n4 = new BigInt(s).add(4);
        String res = n1.mul(n4).add(1).square().sub(1).div(24).toString();
        System.out.println(res);
    }

    public static void main(String[] args) throws Exception {
        new Main().solve();
        out.flush();
    }

    static PrintWriter out = new PrintWriter(System.out, false);
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


