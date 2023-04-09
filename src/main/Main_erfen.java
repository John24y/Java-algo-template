//import java.io.*;
//import java.util.Arrays;
//import java.util.Comparator;
//
//public class Main_erfen {
//    static StreamTokenizer in = new StreamTokenizer(new BufferedReader(new InputStreamReader(System.in)));
//    static PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
//    public static double nextDouble() throws IOException{ in.nextToken(); return in.nval; }
//    public static float nextFloat() throws IOException{ in.nextToken(); return (float)in.nval; }
//    public static int nextInt() throws IOException { in.nextToken(); return (int)in.nval; }
//    public static long nextLong() throws IOException { in.nextToken(); return (long)in.nval; }
//    public static String next() throws IOException{ in.nextToken(); return in.sval;}
//    static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
//
//    public void resolve() throws Exception {
//        int n=nextInt(),m=nextInt();
//        long k=nextLong();
//        int[][] a=new int[n][2];
//        int[][] b=new int[m][2];
//        for (int i = 0; i < n; i++) {
//            a[i][0]=nextInt();
//            a[i][1]=nextInt();
//        }
//        for (int i = 0; i < m; i++) {
//            b[i][0]=nextInt();
//            b[i][1]=nextInt();
//        }
//        Arrays.sort(a, Comparator.comparingDouble(x->-x[0]/(double)(x[0]+x[1])));
//        Arrays.sort(b, Comparator.comparingDouble(x->-x[0]/(double)(x[0]+x[1])));
////        if (k==n*m){
////            out.println(per(a[n-1][0]+b[m-1][0],a[n-1][1]+b[m-1][1]));
////        }
//        double lo=0,hi=1;
//        double P=10e-12;
//        while (hi>=lo) {
//            double mid=(lo+hi)/2;
//            if (lo==mid||hi==mid){
//                lo=mid;
//                break;
//            }
//            long c=0;
//            for (int i = 0; i < n; i++) {
//                c+=ge(a[i],b,mid);
//            }
//            if (c<k) {
//                hi=mid;
//            } else {
//                lo=mid;
//            }
//        }
//        out.println(lo*100);
//    }
//
//    long ge(int[] a, int[][] b,double p) {
//        int lo=0,hi=b.length-1;
//        while (lo<=hi) {
//            int mid=lo+hi>>1;
//            if (per(a[0]+b[mid][0],a[1]+b[mid][1])<p) {
//                hi=mid-1;
//            } else {
//                lo=mid+1;
//            }
//        }
//        return lo;
//    }
//
//    double per(int[] a) {
//        return per(a[0],a[1]);
//    }
//    double per(int a, int b) {
//        return a/(double)(a+b);
//    }
//
//    public static void main(String[] args) throws Exception {
//        new Main_erfen().resolve();
//        out.flush();
//    }
//}
//
