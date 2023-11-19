package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * @Author Create by CROW
 * @Date 2023/6/24
 */
public class Test {
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
    public static void main(String[] args) {
        int[] a=new int[]{1,2,2,3};
        do {
            System.out.println(Arrays.toString(a));
        } while (next_permutation(a));
    }


    static void bench(String mark, Runnable runnable) {
        long millis = System.currentTimeMillis();
        runnable.run();
        long pass = System.currentTimeMillis() - millis;
        System.out.println(mark + ":" + pass + "ms");
    }
}
