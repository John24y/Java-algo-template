package main.at;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.TreeMap;


class UnionFind {
    int[] fa;
    int[] sz;//包含节点数量
    int[] edgeCnt;//包含边数

    public UnionFind(int n) {
        fa=new int[n+1];
        sz=new int[n+1];
        edgeCnt=new int[n+1];
        for (int i = 0; i <= n; i++) {
            fa[i]=i;
            sz[i]=1;
        }
    }

    int find(int x) {
        if (fa[x]!=x){
            fa[x]=find(fa[x]);
        }
        return fa[x];
    }

    void union(int i, int j) {
        int f1=find(i);
        int f2=find(j);
        edgeCnt[f1]++;
        if (f1==f2)return;
        fa[f2]=f1;
        sz[f1]+=sz[f2];
        edgeCnt[f1]+=edgeCnt[f2];
    }

}
public class Main implements Runnable {

    int h,w;
    int id(int x,int y) {
        return (x*w)+y;
    }
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

    void solve() {
        int M=998244353;
        h=nextInt();
        w=nextInt();
        String[] grid=new String[h];
        for (int i = 0; i < h; i++) {
            grid[i]=next();
        }
        UnionFind find = new UnionFind(h*w);
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                if (grid[i].charAt(j)=='.') continue;
                if (i<h-1 && grid[i+1].charAt(j)=='#'){
                    find.union(id(i,j),id(i+1,j));
                }
                if (j<w-1 && grid[i].charAt(j+1)=='#'){
                    find.union(id(i,j),id(i,j+1));
                }
            }
        }
        long sum = 0;
        long com = 0;
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                if (grid[i].charAt(j)=='#'&&id(i,j)==find.find(id(i,j)))com++;
            }
        }
        int red=0;
        int[][] ds=new int[][]{new int[]{1,0},new int[]{-1,0},new int[]{0,1},new int[]{0,-1}};
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                if (grid[i].charAt(j)=='#') {
                    red++;
                    continue;
                }
                List<Integer> set = new ArrayList<>();
                for (int[] d: ds) {
                    int ii=d[0]+i;
                    int jj=d[1]+j;
                    if (ii>=0&&ii<h&&jj>=0&&jj<w&&grid[ii].charAt(jj)=='#'){
                        int fa = find.find(id(ii, jj));
                        if (!set.contains(fa)) {
                            set.add(fa);
                        }
                    }
                }
                sum+=com+1-set.size();
                sum%=M;
            }
        }
        out.println(sum*ksm((long)h*w-red,M-2,M)%M);
    }


    public static void main(String[] args) throws Exception {
        new Thread(null, new Main(), "CustomThread", 1024 * 1024 * 100).start();
    }


    @Override
    public void run() {
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

