package template.math;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @Author Create by CROW
 * @Date 2023/1/23
 */
public class MathTemplate {

    public static long gcd(long a, long b) {
        if (a == 0) {
            return b;
        }
        return gcd(b % a, a);
    }

    /**
     * x=xy[0],y=xy[1]
     * 求解a*x+b*y=gcd(a,b)的一组特解
     */
    public static long exgcd(long a, long b, long[] xy) {
        if (b == 0) {
            xy[0] = 1;
            xy[1] = 0;
            return a;
        }
        long g = exgcd(b, a % b, xy);
        long temp = xy[0];
        xy[0] = xy[1];
        xy[1] = temp - a / b * xy[0];
        return g;
    }

    /**
     * 求逆元，m必须是素数
     */
    static long modinv(long a, long m) {
        return ksm(a, m - 2, m);
    }

    /**
     * 快速幂，求(a^pow)%mod
     */
    static long ksm(long a, long pow, long mod) {
        long skt = 1;
        while (pow > 0) {
            if (pow % 2 != 0) {
                skt = skt * a % mod;
            }
            a = a * a % mod;
            pow = pow >> 1;
        }
        return skt % mod;
    }

    /**
     * 组合式ret[n][m]表示C(n,m),对mod求模
     */
    public static int[][] comb(int n, int mod) {
        int[][] c = new int[n + 1][n + 1];
        for (int i = 0; i <= n; i++) {
            c[i][0] = 1;
        }
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= i; j++) {
                c[i][j] = c[i - 1][j - 1] + c[i - 1][j];
                c[i][j] %= mod;
            }
        }
        return c;
    }

    /**
     * 求阶乘的逆元. mod必须是质数
     */
    public static int[][] faci(int n, int mod) {
        int[] fac = new int[n + 1];
        int[] faci = new int[n + 1];
        fac[0] = faci[0] = 1;
        for (int i = 1; i <= n; i++) {
            fac[i] = fac[i - 1] * i % mod;
            faci[i] = (int) ksm(fac[i], mod - 2, mod);
        }
        return new int[][]{fac, faci};
    }

    /**
     * 判断是否素数
     */
    public static boolean isPrime(int n) {
        if (n < 2) return false;
        for (int i = 2; i * i <= n; i++) {
            if (n % i == 0) return false;
        }
        return true;
    }

    /**
     * r[i]: i是否素数
     */
    public static boolean[] isPrimes(int n) {
        boolean[] isPrime = new boolean[n + 1];
        Arrays.fill(isPrime, true);
        isPrime[0] = false;
        isPrime[1] = false;
        int i = 2;
        while (i * i <= n) {
            if (isPrime[i]) {
                for (int j = i * i; j <= n; j += i) {
                    isPrime[j] = false;
                }
            }
            i++;
        }
        return isPrime;
    }

    public static List<Integer> getPrimes(int upTo) {
        List<Integer> res=new ArrayList<>();
        boolean[] notPrime = new boolean[upTo + 1];
        notPrime[0] = true;
        notPrime[1] = true;
        for (int i = 2; i <= upTo; i++) {
            if (!notPrime[i]) {
                res.add(i);
                for (int j = i + i; j <= upTo; j += i) {
                    notPrime[j] = true;
                }
            }
        }
        return res;
    }

    /**
     * 获取n的不同的素因子，如果n是素数，包括n
     */
    List<Integer> factors(int n) {
        List<Integer> res=new ArrayList<>();
        int p=2,x=n;
        while (p*p<=n){
            if (x%p==0){
                res.add(p);
                while (x%p==0){
                    x/=p;
                }
            }
            p++;
        }
        if (x!=1) {
            res.add(x); //如果n是素数，包括n
        }
        return res;
    }

    /**
     * 获取n的所有素因子的重复次数，如果n是素数，包括n
     * r[i][0] 因子，r[i][1]因子重复次数
     */
    List<int[]> factorsRepeat(int n) {
        List<int[]> res=new ArrayList<>();
        int p=2,x=n;
        while (p*p<=n){
            if (x%p==0){
                int t=0;
                while (x%p==0){
                    x/=p;
                    t++;
                }
                res.add(new int[] {p,t});
            }
            p++;
        }
        if (x!=1){
            res.add(new int[] {x,1}); //如果n是素数，包括n
        }
        return res;
    }

    static class Point {
        long x,y;

        public Point(long x, long y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Point point = (Point) o;
            return x == point.x && y == point.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    public static void main(String[] args) {
        System.out.println(modinv(5,998244353));
    }
}
