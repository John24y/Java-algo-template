
import java.util.function.IntPredicate;

/**
 * @Author Create by jiaxiaozheng
 * @Date 2022/9/20
 */
public class BinarySearch {

    /**
     * 返回第一个为true的位置 [0,n]
     */
    public int bisect(int low, int high, IntPredicate predicate) {
        while (low<=high){
            int mid=low+high>>1;
            if (predicate.test(mid)) {
                high=mid-1;
            } else {
                low=mid+1;
            }
        }
        return low;
    }
}
