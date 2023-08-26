package template.math;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Miller-Rabin素性测试
 *
 * @Author Create by CROW
 * @Date 2023/5/20
 */
class MillerRabinPrimeTest {
    //快速积运算，快速求出a^m^*a^m^并且能防止爆掉
    static long qul_mul(long a, long b, long n) {
        long num = 0;

        while (b > 0) {
            if ((b & 1) > 0) {
                num = (num + a) % n;
            }

            a = (a + a) % n;
            b >>= 1;
        }

        return num;
    }

    //快速幂计算快速求出a^m^的值
    static long qul_pow(long a, long b, long n) {
        long sum = 1;

        while (b>0) {
            if ((b & 1)>0) {
                sum = sum * a % n;
            }

            a = a * a % n;
            b >>= 1;
        }

        return sum;
    }

    static boolean millerRabinPrimeTest(long n) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        long m = n - 1;
        int t = 0;

        if (n == 2) {
            return true;
        } else if (n < 2 || (n & 1) == 0) {
            return false;
        }

        //求出n-1 = m*2^t的m和t。
        while ((m & 1) == 0) {
            m >>= 1;
            t++;
        }

        for (int i = 0; i < 20; i++) {
            //随机数a
            long a = Math.abs(random.nextLong()) % (n - 1) + 1;
            // 计算a^m
            long x = qul_pow(a, m, n), y;

            for (int j = 0; j < t; j++) {
                y = qul_mul(x, x, n);  //进行(x*x)%n操作。
                //不满足二次探测定理，也就是y得1了但是x并不等于1或者n-1，那么n就一定不是质数。
                if (y == 1 && x != 1 && x != n - 1) {
                    return false;
                }
                x = y;
            }
            //上面循环跑完了，如果最后x都不等于1，那么一定是一个合数了。
            if (x != 1) {
                return false;
            }
        }
        return true;
    }
}
