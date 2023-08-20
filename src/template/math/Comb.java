package template.math;

/**
 * @Author Create by jiaxiaozheng
 * @Date 2023/4/14
 */
class Comb {

    long[] p;
    long[] pi;
    long mod;

    public Comb(int n, long mod) {
        this.mod=mod;
        p=new long[n+1];
        pi=new long[n+1];
        p[0] = 1;
        pi[0] = 1;
        for (int i = 1; i <= n; i++) {
            p[i] = p[i - 1] * i % mod;
        }
        pi[n] = modinv(p[n], (int) mod);
        for (int i = n; i > 1; i--) {
            pi[i-1] = pi[i] * (long) i % mod;
        }
    }

    public long comb(int n, int r) {
        if (n < r) return 0;
        return p[n] * pi[r] % mod * pi[n - r] % mod;
    }

    public long perm(int n, int r) {
        if (n < r) return 0;
        return p[n] * pi[n - r] % mod;
    }

    public static long modinv(long a, long m) {
        long b=m;
        long prevx = 1, x = 0;
        long prevy = 0, y = 1;
        long q, r;
        while (b>0) {
            q = a / b;
            r = a % b;

            long tmp = x;
            x = prevx - q * x;
            prevx = tmp;

            tmp = y;
            y = prevy - q * y;
            prevy = tmp;

            a = b;
            b = r;
        }

        prevx %= m;
        if (prevx < 0) prevx += m;
        return prevx;
    }

    public static void main(String[] args) {
        System.out.println(-6%5);
    }
}
