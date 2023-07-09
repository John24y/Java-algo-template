package template.math;

/**
 * @Author Create by jiaxiaozheng
 * @Date 2023/7/9
 */
class FWT {
    static final int MOD = 998244353;
    static final int MOD_I2 = 499122177; // 2的逆元
    static final int CAL_OR = 1;
    static final int CAL_AND = 2;
    static final int CAL_XOR = 3;

    /**
     * r[i]=sum(a[i]*b[j] for i,j in range(n) if i⊕j=k)
     */
    static int[] fwtOr(int[] a) { return fwt(a, null, CAL_OR); }
    static int[] fwtAnd(int[] a) { return fwt(a, null, CAL_AND); }
    static int[] fwtXor(int[] a) { return fwt(a, null, CAL_XOR); }
    static int[] fwtOr(int[] a, int[] b) { return fwt(a, b, CAL_OR); }
    static int[] fwtAnd(int[] a, int[] b) { return fwt(a, b, CAL_AND); }
    static int[] fwtXor(int[] a, int[] b) { return fwt(a, b, CAL_XOR); }

    static int[] fwt(int[] a, int[] b, int calType) {
        int n = Math.max(a.length, b == null ? 0 : b.length);
        int limit = 1;
        // a和b的长度向上补齐到2^x，位运算变换结果可以是小于2^x的任何数
        while (limit < n) {
            limit <<= 1;
        }
        a = ensureLen(a, limit);
        if (b!=null) b = ensureLen(b, limit);
        if (calType == CAL_OR) {
            or(a, true);
            if (b!=null) or(b, true);
            dotMul(a, b == null ? a : b, a);
            or(a, false);
        } else if (calType == CAL_AND) {
            and(a, true);
            if (b!=null) and(b, true);
            dotMul(a, b == null ? a : b, a);
            and(a, false);
        } else if (calType == CAL_XOR) {
            xor(a, true);
            if (b!=null) xor(b, true);
            dotMul(a, b == null ? a : b, a);
            xor(a, false);
        } else {
            assert false;
        }
        return a;
    }

    private static void dotMul(int[] a, int[] b, int[] res) {
        for (int i = 0; i < a.length; i++) res[i] = (int) (((long) a[i] * (long) b[i]) % MOD);
    }

    static int[] ensureLen(int[] a, int len) {
        if (a.length == len) return a; // 注释掉则每次调用拷贝入参
        int[] tmp = new int[len];
        System.arraycopy(a, 0, tmp, 0, a.length);
        return tmp;
    }

    static void or(int[] a, boolean forward) {
        int x = forward ? 1 : MOD - 1;
        for (int l = 2, k = 1; l <= a.length; l <<= 1, k <<= 1)
            for (int i = 0; i < a.length; i += l)
                for (int j = 0; j < k; j++)
                    a[i + j + k] = (int) ((a[i + j + k] + (long) a[i + j] * x) % MOD);
    }

    static void and(int[] a, boolean forward) {
        int x = forward ? 1 : MOD - 1;
        for (int l = 2, k = 1; l <= a.length; l <<= 1, k <<= 1)
            for (int i = 0; i < a.length; i += l)
                for (int j = 0; j < k; j++)
                    a[i + j] = (int) ((a[i + j] + (long) a[i + j + k] * x) % MOD);
    }

    static void xor(int[] a, boolean forward) {
        int x = forward ? 1 : MOD_I2;
        for (int l = 2, k = 1; l <= a.length; l <<= 1, k <<= 1)
            for (int i = 0; i < a.length; i += l)
                for (int j = 0; j < k; j++) {
                    long tmp1 = (long) (a[i + j] + a[i + j + k]) * x % MOD;
                    long tmp2 = (long) (a[i + j] - a[i + j + k] + MOD) * x % MOD;
                    a[i + j] = (int) tmp1;
                    a[i + j + k] = (int) tmp2;
                }
    }

}
