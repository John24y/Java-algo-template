//import java.io.*;
//
//public class Main1 {
//    static StreamTokenizer in = new StreamTokenizer(new BufferedReader(new InputStreamReader(System.in)));
//    static PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
//    public static double nextDouble() throws IOException{ in.nextToken(); return in.nval; }
//    public static float nextFloat() throws IOException{ in.nextToken(); return (float)in.nval; }
//    public static int nextInt() throws IOException { in.nextToken(); return (int)in.nval; }
//    public static long nextLong() throws IOException { in.nextToken(); return (long)in.nval; }
//    public static String next() throws IOException{ in.nextToken(); return in.sval;}
//    static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
//
//    public static long gcd(long a, long b) {
//        if (a == 0) return b;
//        return gcd(b%a, a);
//    }
//
//    public boolean resolve() throws Exception {
//        long a=nextLong(),b=nextLong();
//        long ans=0;
//        while (a>0 && b>0) {
//            long gcd = gcd(a, b);
//            a-=gcd;
//            b-=gcd;
//            ans+=1;
//        }
//        out.println(ans);
//        return true;
//    }
//
//    public static void main(String[] args) throws Exception {
//        new Main1().resolve();
//        out.flush();
//    }
//}
//
