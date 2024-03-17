package main.nk;

import java.io.*;
import java.util.*;
public class Main {
    boolean[] sel;
    int n,h,w;
    int[][] rs;
    int[][] g;
    boolean succ;
    void solve() {
        n=nextInt();h=nextInt();w=nextInt();
        sel=new boolean[n];
        rs=new int[n][2];
        for (int i = 0; i < n; i++) {
            int a=nextInt(),b=nextInt();
            rs[i][0]=a;
            rs[i][1]=b;
        }
        g=new int[h][w];
        dfs();
        out.println(succ ? "Yes" : "No");
    }
    
    int[] find(int x,int y) {
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                if (g[i][j]==0) {
                    if (i+x>h || j+y>w) {
                        return null;
                    }
                    for (int k = 0; k < y; k++) {
                        if (g[i][k+j]==1) return null;
                    }
                    return new int[]{i,j};
                }
            }
        }
        return null;
    }
    
    void place(int x, int y, int h, int w, int v) {
        for (int i = x; i < x+h; i++) {
            for (int j = y; j < y+w; j++) {
                g[i][j]=v;
            }
        }
    }

    void dfs() {
        if (succ) return;
        int cnt=0;
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                cnt+=g[i][j];
            }
        }
        if (cnt==h*w){
            succ=true;
            return;
        }
        for (int i = 0; i < n; i++) {
            if (sel[i])continue;
            for (int j = 0; j < 2; j++) {
                int rs0 = j == 0 ? rs[i][0] : rs[i][1];
                int rs1 = j == 0 ? rs[i][1] : rs[i][0];
                int[] xy = find(rs0, rs1);
                if (xy != null) {
                    sel[i]=true;
                    place(xy[0],xy[1],rs0, rs1,1);
                    dfs();
                    place(xy[0],xy[1],rs0, rs1,0);
                    sel[i]=false;
                }
            }
        }
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

