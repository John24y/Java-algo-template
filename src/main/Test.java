package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * @Author Create by jiaxiaozheng
 * @Date 2023/6/24
 */
public class Test {

    public static void main(String[] args) {
        int M = 1000000;
        for (int j = 0; j < 10; j++) {
            List<Integer> list = new ArrayList<>(M);
            int[] ar = new int[M];
            ThreadLocalRandom random = ThreadLocalRandom.current();
            for (int i = 0; i < M; i++) {
                list.add(random.nextInt());
                ar[i]=random.nextInt();
            }
            bench("list", () -> {
                Collections.sort(list);
            });
            bench("ar", () -> {
                Arrays.sort(ar);
            });
        }
    }

    static void bench(String mark, Runnable runnable) {
        long millis = System.currentTimeMillis();
        runnable.run();
        long pass = System.currentTimeMillis() - millis;
        System.out.println(mark + ":" + pass + "ms");
    }
}
