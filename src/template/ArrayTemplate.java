package template;

import java.util.Arrays;

/**
 * @Author Create by crow
 * @Date 2024/4/27
 */
public class ArrayTemplate {

    /**
     *
     * r[i]=sum(abs(a[j]-a[i]) for j in range(n))
     */
    public static long[] eachTotalDistance(int[] a) {
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


}
