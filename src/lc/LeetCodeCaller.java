package lc;

import java.util.List;

/**
 * @Author Create by CROW
 * @Date 2022/7/7
 */
public class LeetCodeCaller {
    static long gcd(long a, long b) {
        if (a == 0) return b;
        return gcd(b%a, a);
    }

    // Compute (a^n) % m
    static long bigMod(long a, long n, long m) {
        if (n == 0) return 1;
        if (n == 1) return a % m;
        long ret = bigMod(a, n/2, m);
        ret = (ret * ret) % m;
        if (n % 2 == 1) return (ret * a) % m;
        return ret;
    }

    // Function to find (1/a mod m).
// This function can find mod inverse if m are prime
    static long modInverseFarmetsTheorem(long a, long m) {
        if (gcd(a, m) != 1) return -1;

        return bigMod(a, m-2, m);
    }

    // This function finds ncr using modular multiplicative inverse
    static long ncr(long n, long r, long m) {
        if (n == r) return 1;
        if (r == 1) return n;

        long start = n - Math.max(r, n - r) + 1;

        long ret = 1;
        for (long i = start; i <= n; i++) ret = (ret * i) % m;

        long until = Math.min(r, n - r), denom = 1;
        for (long i = 1; i <= until; i++) denom = (denom * i)  % m;

        ret = (ret * modInverseFarmetsTheorem(denom, m)) % m;

        return ret;
    }

    /**
     * 示例输入：
     * ["MyCalendar","book","book","book","book","book","book","book","book","book","book"]
     * [[],[47,50],[33,41],[39,45],[33,42],[25,32],[26,35],[19,25],[3,8],[8,13],[18,27]]
     * 示例输出：
     * [null,true,true,true,true,true,true,true,true,true,true]
     */
    public static List<Object> callMethods(String input) {
        String[] rows = input.split("\n");

        return null;
    }

    public static void main(String[] args) {

    }
}
