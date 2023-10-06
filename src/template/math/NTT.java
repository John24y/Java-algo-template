package template.math;

class NTT {
    static final int P = 998244353, G = 3, Gi = 332748118;

    /**
     * 多项式A*B, a[i]表示多项式A的i次项系数
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

        for(int i = 0; i < limit; i++) {
            rev[i] = (rev[i >> 1] >> 1) | ((i & 1) << (L - 1));
        }

        NTT(A[0], limit, 1, rev);
        NTT(A[1], limit, 1, rev);
        for(int i = 0; i < limit; i++) A[0][i] = (int) (((long)A[0][i] * (long)A[1][i]) % P);
        NTT(A[0], limit, -1, rev);

        int[] ans = new int[t + 1 + resPaddingLen];
        long inv = fastpow(limit, P - 2);
        for(int i = 0; i <= t; i++) ans[i] = (int) (((long) A[0][i] * inv) % P);
        return ans;
    }

    /**
     * 多项式A*A, a[i]表示多项式A的i次项系数
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

        for(int i = 0; i < limit; i++) {
            rev[i] = (rev[i >> 1] >> 1) | ((i & 1) << (L - 1));
        }

        NTT(A, limit, 1, rev);
        for(int i = 0; i < limit; i++) A[i] = (int) (((long)A[i] * (long)A[i]) % P);
        NTT(A, limit, -1, rev);

        int[] ans = new int[t + 1 + resPaddingLen];
        long inv = fastpow(limit, P - 2);
        for(int i = 0; i <= t; i++) ans[i] = (int) (((long) A[i] * inv) % P);
        return ans;
    }

    private static void NTT(int[] A, int limit, int type, int[] rev) {
        for(int i = 0; i < limit; i++){
            if(i < rev[i]) {
                int temp = A[i];
                A[i] = A[rev[i]];
                A[rev[i]] = temp;
            }
        }
        for(int mid = 1; mid < limit; mid <<= 1) {
            long Wn = fastpow( type == 1 ? G : Gi , (P - 1) / (mid << 1));
            for(int j = 0; j < limit; j += (mid << 1)) {
                long w = 1;
                for(int k = 0; k < mid; k++, w = (w * Wn) % P) {
                    long x = A[j + k], y = w * (long) A[j + k + mid] % P;
                    A[j + k] = (int) ((x + y) % P);
                    A[j + k + mid] = (int) ((x - y + P) % P);
                }
            }
        }
    }

    private static long fastpow(long a, long k) {
        long base = 1;
        while(k != 0) {
            if((k & 1) != 0)
                base = (base * a) % P;
            a = (a * a) % P;
            k >>= 1;
        }
        return base % P;
    }
}
