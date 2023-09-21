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

    public static void main(String[] args) {
        int[] a = new int[]{1, 1, 2, 2, 2, 2, 2, 3};
        System.out.println(Arrays.binarySearch(a, 2));
        List<Integer> list = Arrays.asList(1, 1, 2, 2, 2, 2, 2, 3);
        System.out.println(Collections.binarySearch(list, 2));
    }

    static void bench(String mark, Runnable runnable) {
        long millis = System.currentTimeMillis();
        runnable.run();
        long pass = System.currentTimeMillis() - millis;
        System.out.println(mark + ":" + pass + "ms");
    }
}
