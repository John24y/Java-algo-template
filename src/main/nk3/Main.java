package main.nk3;

import java.io.*;
import java.util.*;
public class Main {
    public void solve() throws Exception {
        TreeMap<Long,Long> t1=new TreeMap<>();
        TreeMap<Long,Long> t2=new TreeMap<>();
        Set<Long> seg=new HashSet<>();
        long n=readLong();
        int m1=readInt(),m2=readInt();
        long cc=0;
        for (int i = 0; i < m1; i++) {
            long u=readLong(),c=readLong();
            t1.put(cc,u);
            seg.add(cc);
            cc+=c;
        }
        seg.add(cc);
        cc=0;
        for (int i = 0; i < m2; i++) {
            long u=readLong(),c=readLong();
            t2.put(cc,u);
            seg.add(cc);
            cc+=c;
        }
        seg.add(cc);

        long res=0;
        List<Long> segg=new ArrayList<>(seg);
        Collections.sort(segg);
        for (int i = 0; i < segg.size() - 1; i++) {
            long seggLen = segg.get(i+1)-segg.get(i);
            Map.Entry<Long, Long> e1 = t1.floorEntry(segg.get(i));
            Map.Entry<Long, Long> e2 = t2.floorEntry(segg.get(i));
            if (!Objects.equals(e1.getValue(), e2.getValue())){
                res+=seggLen-1;
            }
            if (i!=segg.size()-2) {
                Set<Long> ss=new HashSet<>();
                ss.add(e1.getValue());
                ss.add(e2.getValue());
                ss.add(t1.floorEntry(segg.get(i+1)).getValue());
                ss.add(t2.floorEntry(segg.get(i+1)).getValue());
                if (ss.size()==2){
                    res++;
                }
            }
        }
        out.println(res);
    }

    public static void main(String[] args) throws Exception {
        new Main().solve();
        out.flush();
    }

    static PrintWriter out = new PrintWriter(System.out, false);
    static InputReader in = new InputReader(System.in);
    static String next() { return in.next(); }
    static int readInt() { return Integer.parseInt(in.next()); }
    static long readLong() { return Long.parseLong(in.next()); }
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

