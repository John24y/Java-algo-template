package template.sort;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @Author Create by crow
 * @Date 2024/6/2
 */
public class KthPartition {

    /**
     * 找到第k大，并且完成第k大左右的划分
     */
    public static int kthPartition(List<Integer> list, int k, Comparator<Integer> comparator) {
        return kthPartition(list, 0, list.size() - 1, k, comparator);
    }

    // [left,right]
    private static int kthPartition(List<Integer> list, int left, int right, int k, Comparator<Integer> comparator) {
        if (left == right) {
            return left;
        }
        int p = partition(list, left, right, comparator);
        if (p - left + 1 == k) {
            return p;
        } else if (p - left + 1 > k) {
            return kthPartition(list, left, p - 1, k, comparator);
        } else {
            return kthPartition(list, p + 1, right, k - (p - left + 1), comparator);
        }
    }

    private static int partition(List<Integer> list, int left, int right, Comparator<Integer> comparator) {
        int p = ThreadLocalRandom.current().nextInt(left, right + 1);
        Collections.swap(list, right, p);
        int pi = list.get(right);
        int j = 0;
        for (int i = left; i < right; i++) {
            if (comparator.compare(list.get(i), pi) <= 0) {
                Collections.swap(list, i, j);
                j++;
            }
        }
        Collections.swap(list, j, right);
        return j;
    }
}
