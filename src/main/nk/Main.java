package main.nk;

import java.io.*;
import java.util.*;

public class Main {
    int[] letc = new int[26];
    ArrayList<Integer>[] lets = new ArrayList[26];
    String s,t;
    long n;
    void solve() {
        n=nextLong();
        s=next();
        t=next();
        for (int i = 0; i < 26; i++) {
            lets[i]=new ArrayList<>();
        }
        for (int i = 0; i < s.length() * 20; i++) {
            int v=s.charAt(i%s.length()) - 'a';
            lets[v].add(i);
            if (i<s.length()) {
                letc[v]+=1;
            }
        }
        long low=0,high=(long) 1e18;
//        long low=0,high=(long) 3;
        while (low<=high) {
            long mid = low + (high - low + 1) / 2;
            if (check(mid)) {
                low=mid+1;
            } else {
                high=mid-1;
            }
        }
        System.out.println(high);
    }

    boolean check(long k) {
        if (k==0) return true;
        long rep=0;
        int i=0;
        for (int j = 0; j < t.length(); j++) {
            int chr=t.charAt(j)-'a';
            if (letc[chr]==0) return false;
            long kk=k;
            if (kk>letc[chr]) {
                if (kk%letc[chr] == 0) {
                    rep+=kk/letc[chr]-1;
                    kk=letc[chr];
                } else {
                    rep+=kk/letc[chr];
                    kk%=letc[chr];
                }
            }
            i = findNext(lets[chr], (int)kk, i);
            if (i>=s.length()) {
                rep++;
                i%=s.length();
            }
            if (rep>n || rep==n&&i>0){
                return false;
            }
        }
        return true;
    }

    int findNext(List<Integer> list, int kk, int i) {
        if (kk==0) return i;
        int lo=0,hi=list.size()-1;
        while (lo<=hi) {
            int mid=lo+hi>>1;
            if (list.get(mid)>=i) {
                hi=mid-1;
            } else {
                lo=mid+1;
            }
        }
        return list.get(lo+kk-1)+1;
    }

    public static void main(String[] args) throws Exception {
        int t=1;
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

