package main.at;

import java.io.*;
import java.util.*;
import java.util.function.IntPredicate;
@SuppressWarnings("all")
class Counter<K> extends HashMap<K,Integer> {

    public Counter() {
    }

    public Counter(Collection<K> collection) {
        for (K k : collection) {
            add(k, 1);
        }
    }

    public void addInts(int[] ints) {
        for (int anInt : ints) {
            add((K) (Integer) anInt, 1);
        }
    }

    public void addLongs(long[] longs) {
        for (long v : longs) {
            add((K) (Long) v, 1);
        }
    }

    public void add(K key, int cnt) {
        int newV = get(key) + cnt;
        if (newV == 0) {
            remove(key);
        } else {
            put(key, newV);
        }
    }

    @Override
    public Integer get(Object key) {
        return super.getOrDefault(key, 0);
    }
}
class BinarySearch {

    /**
     * 返回第一个为true的位置 [0,n]
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
}
public class Main implements Runnable{
    public void solve() {
        int n=nextInt();
        int[] a=new int[n];
        for (int i = 0; i < n; i++) {
            a[i]=nextInt();
        }
        Counter<Integer> counter = new Counter<>();
        counter.addInts(a);
        ArrayList<Map.Entry<Integer, Integer>> list = new ArrayList<>(counter.entrySet());
        list.sort(new Comparator<Map.Entry<Integer, Integer>>() {
            @Override
            public int compare(Map.Entry<Integer, Integer> o1, Map.Entry<Integer, Integer> o2) {
                return Integer.compare(o1.getKey(), o2.getKey());
            }
        });
        n=list.size();
        int[] pre = new int[n+1];
        for (int i = 0; i < n; i++) {
            pre[i+1]=pre[i]+list.get(i).getValue();
        }
        long res=0;
        for (int i = 0; i < n; i++) {
            int last=i+1;
            res+= (long) list.get(i).getValue() *(list.get(i).getValue()-1)/2;
            for (int m = 2; m < 999999999; m++) {
                int f=list.get(i).getKey()*m;
                int j=BinarySearch.bisect(0, n - 1, new IntPredicate() {
                    @Override
                    public boolean test(int value) {
                        return list.get(value).getKey()>=f;
                    }
                });
                res+= (long) (pre[j] - pre[last]) *(m-1) * list.get(i).getValue();
                last = j;
                if (j>=n) {
                    break;
                }
            }
        }
        out.println(res);
    }

    public static void main(String[] args) throws Exception {
        new Thread(null, new Main(), "CustomThread", 1024 * 1024 * 100).start();
    }

    @Override
    public void run() {
        int t = 1;
        for (int i = 0; i < t; i++) {
            new Main().solve();
        }
        out.flush();
    }

    static PrintWriter out = new PrintWriter(System.out, false);
    static InputReader in = new InputReader(System.in);
    static String next() { return in.next(); }
    static int nextInt() { return Integer.parseInt(in.next()); }
    static long nextLong() { return Long.parseLong(in.next()); }
    static class InputReader {
        public BufferedReader reader;
        public StringTokenizer tokenizer;

        public InputReader(InputStream stream) {
            reader = new BufferedReader(new InputStreamReader(stream), 32768);
            tokenizer = null;
        }

        public String next() {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {
                    tokenizer = new StringTokenizer(reader.readLine());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return tokenizer.nextToken();
        }
    }
}