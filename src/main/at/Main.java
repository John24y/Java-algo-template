package main.at;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

class Node {
    String str;
    List<Node> ch=new ArrayList<>();
}

public class Main implements Runnable{
    int bi;
    String s;
    int n;
    StringBuilder res=new StringBuilder();
    List<Node> build() {
        List<Node> r=new ArrayList<>();
        while (bi<n){
            if (s.charAt(bi)=='(') {
                Node node = new Node();
                bi++;
                node.ch = build();
                bi++;
                r.add(node);
            } else if (s.charAt(bi) == ')') {
                return r;
            } else {
                StringBuilder builder = new StringBuilder();
                while (bi<n && s.charAt(bi)!='(' && s.charAt(bi)!=')') {
                    builder.append(s.charAt(bi));
                    bi+=1;
                }
                Node node = new Node();
                node.str = builder.toString();
                r.add(node);
            }
        }
        return r;
    }

    public void solve() {
        s = next();
        n=s.length();
        List<Node> r = build();
        dfs(r, false);
        System.out.println(res.toString());
    }

    /**
     * @param list
     * @param rev
     */
    void dfs(List<Node> list, boolean rev) {
        for (Node node : list) {
            if (node.str != null) {
                if (!rev) {
                    res.append(node.str);
                } else {
                    for (int i = node.str.length() - 1; i >= 0; i--) {
                        char c = node.str.charAt(i);
                        if (c>='a' && c<='z') {
                            res.append((char)(c-'a'+'A'));
                        } else {
                            res.append((char)(c-'A'+'a'));
                        }
                    }
                }
            } else {
                boolean nrev=!rev;
                if (nrev) {
                    Collections.reverse(node.ch);
                    dfs(node.ch, nrev);
                } else {
                    dfs(node.ch, nrev);
                }
            }
        }
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