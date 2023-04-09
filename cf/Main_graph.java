import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main_graph {
    static StreamTokenizer in = new StreamTokenizer(new BufferedReader(new InputStreamReader(System.in)));
    static PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
    public static double nextDouble() throws IOException{ in.nextToken(); return in.nval; }
    public static float nextFloat() throws IOException{ in.nextToken(); return (float)in.nval; }
    public static int nextInt() throws IOException { in.nextToken(); return (int)in.nval; }
    public static long nextLong() throws IOException { in.nextToken(); return (long)in.nval; }
    public static String next() throws IOException{ in.nextToken(); return in.sval;}
    static BufferedReader re = new BufferedReader(new InputStreamReader(System.in));
    static void printYesNo(boolean b) {out.println(b?"Yes":"No");}

    List<List<Integer>> g;
    void tarjan(List<List<Integer>> g, int u) {

    }

    void solve() throws Exception {
        int n=nextInt();
        int m=nextInt();
        List<List<Integer>> g=new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            g.add(new ArrayList<>());
        }
        for (int i = 0; i < m; i++) {
            int a=nextInt(),b=nextInt();
            g.get(a).add(b);
            g.get(b).add(a);
        }
    }

    public static void main(String[] args) throws Exception {
        new Main_graph().solve();
        out.flush();
    }
}

