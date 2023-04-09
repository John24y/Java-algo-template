package template;

/**
 * @Author Create by jiaxiaozheng
 * @Date 2023/1/23
 */
public class MathFunc {

    public static long gcd(long a,long b){
        if (a==0) {
            return b;
        }
        return gcd(b%a,a);
    }

    public static long ksm(long a, long pow, long mod) {
        long skt=1;
        while (pow>0) {
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
        int[][] c=new int[n+1][n+1];
        for (int i = 0; i <= n; i++) {
            c[i][0]=1;
        }
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= i; j++) {
                c[i][j]=c[i-1][j-1]+c[i-1][j];
                c[i][j]%=mod;
            }
        }
        return c;
    }

    /**
     * 求逆元
     */
    public static int[] invMod() {
        return null;
    }
}
