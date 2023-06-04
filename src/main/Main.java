package main;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    int mod = 998244353;
    static long ksm(long a, long pow, long mod) {
        long skt = 1;
        while (pow > 0) {
            if (pow % 2 != 0) {
                skt = skt * a % mod;
            }
            a = a * a % mod;
            pow = pow >> 1;
        }
        return skt % mod;
    }

    String s;
    int n,b,p,k;
    public void solve() throws Exception {
        n=nextInt();
        b=nextInt();
        p=nextInt();
        k=nextInt();
        s=next();
        int sh=0;
        for (int i = 0; i < n; i++) {
            int c = s.charAt(i) - 'a' + 1;
            int ksm = (int) ksm(b, n - i - 1, p);
            sh = (sh + c*ksm) % p;
        }
        Node node = dfs(n - 1, k, sh);
        if (node==null) {
            System.out.println(-1);
        } else {
            List<Character> res=new ArrayList<>();
            while (node!=null) {
                res.add(node.c);
                node=node.node;
            }
            res.remove(n);
            Collections.reverse(res);
            System.out.println(res.stream().map(x->x.toString()).collect(Collectors.joining()));
        }
    }

    static class Key {
        int i,k,hash;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Key key = (Key) o;
            return i == key.i && k == key.k && hash == key.hash;
        }

        @Override
        public int hashCode() {
            return Objects.hash(i, k, hash);
        }

        public Key(int i, int k, int hash) {
            this.i = i;
            this.k = k;
            this.hash = hash;
        }
    }
    static class Node {
        Node node;
        char c;

        public Node(Node node, char c) {
            this.node = node;
            this.c = c;
        }
    }
    static Node NULL = new Node(null,'a');
    Map<Key,Node> map = new HashMap<>();
    Node dfs(int i, int k, int hash) {
        if (i+1<k) {
            return null;
        }
        Key key=new Key(i,k,hash);
        if (i==-1) {
            if (k==0&&hash==0) {
                return new Node(null,'0');
            }
            return null;
        }
        Node node = map.get(key);
        if (node==NULL) return null;
        if (node!=null){
            return node;
        }
        for (int j = 1; j <= 26; j++) {
            int nhash = (int) ((hash - j * ksm(b,n-i-1,p) + 30 * p) % p);
            if (j==s.charAt(i)-'a'+1) {
                Node r = dfs(i - 1, k - 1, nhash);
                if (r != null) {
                    Node node1 = new Node(r, (char) (j + 'a' - 1));
                    map.put(key, node1);
                    return node1;
                }
            } else {
                Node r = dfs(i - 1, k, nhash);
                if (r != null) {
                    Node node1 = new Node(r, (char) (j + 'a' - 1));
                    map.put(key, node1);
                    return node1;
                }
            }
        }
        map.put(key, NULL);
        return null;
    }

    public static void main(String[] args) throws Exception {
        int t = nextInt();
        for (int i = 0; i < t; i++) {
            new Main().solve();
        }
    }

    static PrintWriter out = new PrintWriter(System.out, true);
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

