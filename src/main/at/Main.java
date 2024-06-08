package main.at;
import java.io.*;
import java.util.*;

class RingTree {
    List<List<Integer>> cycList=new ArrayList<>();//所有环的list
    int[] cycId;//每个节点所在环(cyc_list)的下标，不在环上是-1
    int[] idxInCyc;
    int[] edges;

    //节点i指向edges[i]
    public RingTree(int[] edges) {
        int n = edges.length;
        cycId=new int[n];
        idxInCyc=new int[n];
        this.edges=edges;
        Arrays.fill(cycId, -1);
        Arrays.fill(idxInCyc, -1);
        boolean[] vis=new boolean[n];
        boolean[] inStack=new boolean[n];
        List<Integer> stack=new ArrayList<>();
        for (int i = 0; i < n; i++) {
            dfs(i, vis, inStack, stack, edges);
        }
    }

    void dfs(int i, boolean[] vis, boolean[] inStack, List<Integer> stack, int[] edges) {
        if (inStack[i]) {
            int j=edges[i];
            List<Integer> cyc=new ArrayList<>();
            cycList.add(cyc);
            while (j!=i){
                cyc.add(j);
                j=edges[j];
            }
            cyc.add(i);
            for (int k = 0; k < cyc.size(); k++) {
                int v=cyc.get(k);
                cycId[v]=cycList.size()-1;
                idxInCyc[v]=k;
            }
            return;
        }
        if (vis[i])return;
        vis[i]=true;
        inStack[i]=true;
        stack.add(i);
        dfs(edges[i], vis, inStack, stack, edges);
        stack.remove(stack.size()-1);
        inStack[i]=false;
    }

    /**
     * 到圆环的距离，每条边的长度算1
     */
    int[] distanceToCycle() {
        int n = edges.length;
        boolean[] vis = new boolean[n];
        int[] dis = new int[n];
        for (int i = 0; i < n; i++) {
            distanceToCycle(i, edges, vis, dis);
        }
        return dis;
    }

    private void distanceToCycle(int i, int[] edges, boolean[] vis, int[] dis) {
        if (vis[i]) return;
        vis[i] = true;
        distanceToCycle(edges[i], edges, vis, dis);
        if (cycId[i] == -1) {
            dis[i] = dis[edges[i]] + 1;
        } else {
            dis[i]=cycList.get(cycId[i]).size();
        }
    }

}
public class Main implements Runnable {

    public void solve() {
        int n=nextInt();
        int[] edges = new int[n];
        for (int i = 0; i < n; i++) {
            edges[i]=nextInt()-1;
        }
        RingTree tree = new RingTree(edges);
        int[] ints = tree.distanceToCycle();
        long res=0;
        for (int anInt : ints) {
            res+=anInt;
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