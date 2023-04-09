package main;

import java.io.*;
import java.util.*;

public class CF_BridgeAndGap {

    public static void main(String[] args) {
        CF_BridgeAndGap main = new CF_BridgeAndGap();
        main.solve();
        out.flush();
        out.close();
        in.close();
    }

    void solve() {
        int n = in.nextInt();
        int m = in.nextInt();
        // 存储区间
        long[][] inv = new long[n][2];
        for(int i = 0;i < n;i++) {
            long s = in.nextLong();
            long e = in.nextLong();
            inv[i][0] = s;
            inv[i][1] = e;
        }
        // 区间长度差
        long[][] delta = new long[n - 1][3];
        for(int i = 1;i < n;i++) {
            // 桥梁最小长度
            delta[i - 1][0] = inv[i][0] - inv[i - 1][1];
            // 桥梁最大长度
            delta[i - 1][1] = inv[i][1] - inv[i - 1][0];
            delta[i - 1][2] = i;
        }
        // 最大长度从大到小排序
        Arrays.sort(delta,(a,b)->-Long.compare(a[0],b[0]));
        // 桥梁长度和编号
        TreeMap<Long,Queue<Integer>> bridge = new TreeMap<>();
        for(int i = 0;i < m;i++) {
            long len = in.nextLong();
            bridge.computeIfAbsent(len,v->new LinkedList<>()).add(i + 1);
        }
        int[] ans = new int[n];
        for(int i = 0;i < n - 1;i++) {
            long min = delta[i][0];
            long max = delta[i][1];
            // 在>=min的区间内，找<=max的最大值
            Map.Entry<Long, Queue<Integer>> ceiling = bridge.floorEntry(max);
            if(ceiling == null || ceiling.getKey() < min) {
                out.println("No");
                return;
            }
            Queue<Integer> q = ceiling.getValue();
            ans[(int)delta[i][2]] = q.poll();
            if(q.isEmpty()) {
                bridge.remove(ceiling.getKey());
            }
        }
        out.println("Yes");
        for(int i = 1;i < n;i++) {
            out.print(ans[i]);
            out.print(" ");
        }
    }

    static class FastReader {

        BufferedReader reader;

        StringTokenizer tokenizer;

        FastReader(InputStream stream) {
            reader = new BufferedReader(new InputStreamReader(stream), 32768);
            tokenizer = null;
        }

        // reads in the next string
        String next() {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {
                    tokenizer = new StringTokenizer(reader.readLine());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return tokenizer.nextToken();
        }

        // reads in the next int
        int nextInt() {
            return Integer.parseInt(next());
        }

        // reads in the next long
        long nextLong() {
            return Long.parseLong(next());
        }

        // reads in the next double
        double nextDouble() {
            return Double.parseDouble(next());
        }

        void close() {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static final FastReader in = new FastReader(System.in);

    private static final PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)));

}