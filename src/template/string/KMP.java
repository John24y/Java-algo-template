package template.string;

import java.util.Arrays;

/**
 * @Author Create by CROW
 * @Date 2023/3/3
 */
class KMP {

    static int[] next(String p) {
        int i = 1, j = 0;
        int m = p.length();
        int[] next = new int[m];
        Arrays.fill(next, -1);
        while (i < m && j < m) {
            if (p.charAt(i) == p.charAt(j)) {
                next[i] = j;
                i++;
                j++;
            } else {
                if (j == 0) {
                    i++;
                } else {
                    j = next[j - 1] + 1;
                }
            }
        }
        return next;
    }

    static int match(String s, String p) {
        int[] next = next(p);
        int n = s.length(), m = p.length();
        int i = 0, j = 0;
        while (i < n) {
            if (s.charAt(i) == p.charAt(j)) {
                i++;
                j++;
                if (j == m) {
                    return i - m;
                }
            } else {
                if (j == 0) {
                    i++;
                } else {
                    j = next[j - 1] + 1;
                }
            }
        }
        return -1;
    }
}
