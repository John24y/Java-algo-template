package template.math;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Create by CROW
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

/**
 * 从n个元素[0,n-1]选出m个，枚举方案
 * 建议优先用 next_permutation.
 */
class CombSolution {
    List<List<Integer>> dp = new ArrayList<>();

    public CombSolution(int n, int m) {
        for (int i = 0; i <= m+1; i++) {
            dp.add(new ArrayList<>());
        }
        dp.get(0).add(0);
        for (int i = 0; i < n; i++) {
            for (int j = m; j > 0; j--) {
                for (int s : dp.get(j-1)) {
                    if (accept(s,i)) {
                        dp.get(j).add(s|(1<<i));
                    }
                }
            }
        }
    }

    //state:已选择集合, i是否可以添加i
    boolean accept(int state, int i) {
        return true;
    }
}
