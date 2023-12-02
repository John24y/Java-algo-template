package main.at;

import java.io.*;
import java.nio.channels.IllegalSelectorException;
import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

class IntervalTree {
    //是否合并相邻的区间
    private static final boolean MERGE_ADJACENT = true;

    //<end1,[end2,h]>
    TreeMap<Long,long[]> map=new TreeMap<>();

    //区间[l,r]置1
    public void add(long l, long r) {
        modify(l, r, true);
    }

    //区间[l,r]置0
    public void remove(long l, long r) {
        modify(l, r, false);
    }

    private void modify(long l, long r, boolean add) {
        long L = l, R = r;
        Map.Entry<Long,long[]> entry = null;
        while ((entry=map.ceilingEntry(MERGE_ADJACENT ? l-1 : l))!=null) {
            //map上已存在区间都是互不相交的，左右端点交替出现
            //所以只要按顺序遍历右端点>=l的区间找交集，当区间左端点>r，后续再无交集
            if (entry.getValue()[0]>(MERGE_ADJACENT ? r+1 : r)) break;
            R = Math.max(R, entry.getValue()[1]);
            L = Math.min(L, entry.getValue()[0]);
            internalDel(entry.getValue());
        }
        if (add) {
            internalPut(new long[]{L,R});
        } else {
            if (L<l) {
                internalPut(new long[]{L, l - 1});
            }
            if (R>r) {
                internalPut(new long[]{r+1, R});
            }
        }
    }

    public long[] getCover(long i) {
        Map.Entry<Long, long[]> entry = map.ceilingEntry(i);
        if (entry==null || entry.getValue()[0]>i) return null;
        return entry.getValue();
    }

    //移除当前覆盖点i的连续区间，而不是只移除点i
    public long[] removeCover(long i) {
        long[] value = getCover(i);
        internalDel(value);
        return value;
    }

    private void internalPut(long[] longs) {
        map.put(longs[0], longs);
        if (longs[0]!=longs[1]) {
            map.put(longs[1], longs);
        }
    }
    private void internalDel(long[] longs) {
        map.remove(longs[0]);
        if (longs[0]!=longs[1]) {
            map.remove(longs[1]);
        }
    }
}
class Counter<K> extends HashMap<K,Long> {

    long add(K k, long val) {
        Long pre = put(k, getOrDefault(k, 0L) + val);
        return (pre==null?0:pre)+val;
    }

    @Override
    public Long get(Object key) {
        return super.getOrDefault(key, 0L);
    }
}
public class Main {

    public void solve() throws Exception {
        int n=nextInt(),q=nextInt();
        int[] a=new int[n+1];
        IntervalTree tree = new IntervalTree();
        Counter<Integer> counter = new Counter<>();
        for (int i = 1; i <= n; i++) {
            a[i]=nextInt();
            tree.add(a[i],a[i]);
            counter.add(a[i],1);
        }
        for (int t = 0; t < q; t++) {
            int i = nextInt(), x = nextInt();
            if (counter.add(a[i],-1)==0) {
                tree.remove(a[i],a[i]);
            }
            a[i]=x;
            if (counter.add(a[i],1)==1) {
                tree.add(a[i],a[i]);
            }
            long[] entry = tree.map.entrySet().iterator().next().getValue();
            if (entry[0]!=0) {
                System.out.println(0);
            } else {
                System.out.println(entry[1]+1);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        new Main().solve();
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

