package template;

import java.util.*;

/**
 * @Author Create by CROW
 * @Date 2023/4/10
 */
public class IterTools {

    /**
     * r[i]=sum(abs(a[j]-a[i]) for j in range(n))
     */
    public static long[] each_abs_sum(int[] a) {
        Arrays.sort(a);
        int n = a.length;
        long[] prefix = new long[n + 1];
        for (int i = 0; i < n; i++) {
            prefix[i + 1] = prefix[i] + a[i];
        }
        long[] ans = new long[n];
        for (int i = 0; i < n; i++) {
            long left = (long) i * a[i] - prefix[i]; // 必须做long转换
            long right = prefix[n] - prefix[i + 1] - (long) (n - i - 1) * a[i]; // 必须做long转换
            ans[i] = left + right;
        }
        return ans;
    }

    /**
     * 字典序的下一个排列，相同的元素交换位置看做相同排列，可以用来求N选M的方案
     */
    static boolean next_permutation(int[] p) {
        for (int a = p.length - 2; a >= 0; --a) {
            if (p[a] < p[a + 1]) {
                for (int b = p.length - 1;; --b){
                    if (p[b] > p[a]) {
                        int t = p[a];
                        p[a] = p[b];
                        p[b] = t;
                        for (++a, b = p.length - 1; a < b; ++a, --b) {
                            t = p[a];
                            p[a] = p[b];
                            p[b] = t;
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
