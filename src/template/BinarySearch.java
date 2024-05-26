package template;

import java.util.function.IntPredicate;

/**
 * @Author Create by CROW
 * @Date 2022/9/20
 */
class BinarySearch {

    /**
     * 返回第一个为true的位置 [low,high]
     */
    public static int bisect(int low, int high, IntPredicate predicate) {
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

    public static int bisectLeft(int[] ar, int x) {
        return bisect(0, ar.length - 1, new IntPredicate() {
            @Override
            public boolean test(int value) {
                return ar[value] >= x;
            }
        });
    }

    public static int bisectRight(int[] ar, int x) {
        return bisect(0, ar.length - 1, new IntPredicate() {
            @Override
            public boolean test(int value) {
                return ar[value] > x;
            }
        });
    }

    public static int bisectLeft(long[] ar, long x) {
        return bisect(0, ar.length - 1, new IntPredicate() {
            @Override
            public boolean test(int value) {
                return ar[value] >= x;
            }
        });
    }

    public static int bisectRight(long[] ar, long x) {
        return bisect(0, ar.length - 1, new IntPredicate() {
            @Override
            public boolean test(int value) {
                return ar[value] > x;
            }
        });
    }
}
