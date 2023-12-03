package main.lc;

import java.util.Arrays;

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

class Solution {
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
    public int numberOfSequence(int n, int[] sick) {
        int m= sick.length;
        long num=0;
        long seq=1;
        long M= (long) (1e9+7);
        Comb comb = new Comb(n+1,M);
        for (int i = 1; i < m; i++) {
            int d=sick[i]-sick[i-1]-1;
            if (d>0){
                long pm=ksm(2,d-1,M);
                seq=comb.comb((int) (d+num),d)*seq%M*pm%M;
                num+=d;
            }
        }
        if (sick[0]!=0) {
            seq=comb.comb((int) (num+sick[0]),sick[0])*seq%M;
            num+=sick[0];
        }
        if (sick[m-1]!=n-1){
            int d=n-sick[m-1]-1;
            seq= comb.comb((int) (num+d),d)*seq%M;
        }
        return (int) seq;
    }

    public static void main(String[] args) {
        int i = new Solution().numberOfSequence(4, new int[]{1});
        System.out.println(i);
    }
}