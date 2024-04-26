package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author Create by CROW
 * @Date 2022/9/11
 */
public class LCUtils {
    private static Scanner scanner = new Scanner(System.in);

    public static int[] nextInts() {
        return parseIntAr(scanner.nextLine());
    }

    public static int[][] parseIntIntAr(String s) {
        s = s.replace(" ", "");
        Pattern pattern = Pattern.compile("\\[[,\\d-]*]");
        Matcher matcher = pattern.matcher(s);
        List<int[]> list = new ArrayList<>();
        while (matcher.find()) {
            list.add(parseIntAr(matcher.group()));
        }
        return list.stream().toArray(int[][]::new);
    }
    public static long[][] parseLongLongAr(String s) {
        s = s.replace(" ", "");
        Pattern pattern = Pattern.compile("\\[[,\\d-]*]");
        Matcher matcher = pattern.matcher(s);
        List<long[]> list = new ArrayList<>();
        while (matcher.find()) {
            list.add(parseLongAr(matcher.group()));
        }
        return list.stream().toArray(long[][]::new);
    }

    public static int[] parseIntAr(String s) {
        s = s.replace(" ", "");
        s = s.substring(1, s.length() - 1);
        return Arrays.stream(s.split(",")).mapToInt(Integer::parseInt).toArray();
    }

    public static long[] parseLongAr(String s) {
        s = s.replace(" ", "");
        s = s.substring(1, s.length() - 1);
        return Arrays.stream(s.split(",")).mapToLong(Long::parseLong).toArray();
    }

    public static String[] parseStringAr(String s) {
        s = s.replace(" ", "");
        s = s.substring(1, s.length() - 1);
        return Arrays.stream(s.split(",")).map(x->x.substring(1,x.length()-1)).toArray(String[]::new);
    }

    public static void main(String[] args) {
    }
}
