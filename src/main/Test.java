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
        System.out.println(1 % 5);
    }

    static void bench(String mark, Runnable runnable) {
        long millis = System.currentTimeMillis();
        runnable.run();
        long pass = System.currentTimeMillis() - millis;
        System.out.println(mark + ":" + pass + "ms");
    }
}
