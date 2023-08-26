package util;

import java.io.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @Author Create by CROW
 * @Date 2022/8/28
 */
public class Utils {
    /**
     * 快读快写
     */
    static StreamTokenizer in = new StreamTokenizer(new BufferedReader(new InputStreamReader(System.in)));
    static PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
    public static double nextDouble() throws IOException{ in.nextToken(); return in.nval; }
    public static float nextFloat() throws IOException{ in.nextToken(); return (float)in.nval; }
    public static int nextInt() throws IOException { in.nextToken(); return (int)in.nval; }
    public static String next() throws IOException{ in.nextToken(); return in.sval;}
    static BufferedReader re = new BufferedReader(new InputStreamReader(System.in));

    public static long gcd(long a, long b) {
        if (a == 0) return b;
        return gcd(b%a, a);
    }

    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(1,1,2,2,2,2,2,3);
        System.out.println(Collections.binarySearch(list,2));
    }
}
