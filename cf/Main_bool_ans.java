//
//import java.io.*;
//import java.sql.Array;
//import java.util.*;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;
//
//public class Main {
//    static StreamTokenizer in = new StreamTokenizer(new BufferedReader(new InputStreamReader(System.in)));
//    static PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
//    public static double nextDouble() throws IOException{ in.nextToken(); return in.nval; }
//    public static float nextFloat() throws IOException{ in.nextToken(); return (float)in.nval; }
//    public static int nextInt() throws IOException { in.nextToken(); return (int)in.nval; }
//    public static long nextLong() throws IOException { in.nextToken(); return (long)in.nval; }
//    public static String next() throws IOException{ in.nextToken(); return in.sval;}
//    static BufferedReader re = new BufferedReader(new InputStreamReader(System.in));
//
//    static boolean resv(int n,int m) throws Exception {
//        if (m!=n-1){
//            return false;
//        }
//        List<List<Integer>> g=new ArrayList<>();
//        for (int i = 0; i < n + 1; i++) {
//            g.add(new ArrayList<>());
//        }
//        for (int i = 0; i < m; i++) {
//            int v1=nextInt(),v2=nextInt();
//            g.get(v1).add(v2);
//            g.get(v2).add(v1);
//        }
//        boolean[] vis=new boolean[n+1];
//        LinkedList<Integer> q=new LinkedList<>();
//        q.add(1);
//        vis[1]=true;
//        int res=1;
//        while(q.size()>0){
//            Integer v = q.pollFirst();
//            for (Integer ne : g.get(v)) {
//                if (!vis[ne]) {
//                    vis[ne]=true;
//                    res++;
//                    q.add(ne);
//                }
//            }
//        }
//        return res==n;
//    }
//
//    public static void main(String[] args) throws Exception {
//        int n=nextInt();
//        int m=nextInt();
//        out.println(resv(n,m)?"Yes":"No");
//        out.flush();
//    }
//}
//
