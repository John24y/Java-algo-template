import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    static StreamTokenizer in = new StreamTokenizer(new BufferedReader(new InputStreamReader(System.in)));
    static PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
    public static double nextDouble() throws IOException{ in.nextToken(); return in.nval; }
    public static float nextFloat() throws IOException{ in.nextToken(); return (float)in.nval; }
    public static int nextInt() throws IOException { in.nextToken(); return (int)in.nval; }
    public static long nextLong() throws IOException { in.nextToken(); return (long)in.nval; }
    public static String next() throws IOException{ in.nextToken(); return in.sval;}
    static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    static void printYesNo(boolean b) {out.println(b?"Yes":"No");}

    public static long gcd(long a, long b) {
        if (a == 0) return b;
        return gcd(b%a, a);
    }

    public boolean resolve() throws Exception {
        int n=nextInt();
        List<Integer> a=new ArrayList<>();
        for (int i = 0; i < n; i++) {
            a.add(nextInt());
        }
        List<List<Integer>> res=new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            Set<Integer> set=new HashSet<>(a);
            if (set.size()==1) {
                printYesNo(true);
                out.println(res.size());
                for (List<Integer> re : res) {
                    out.println(re.stream().map(x->x+"").collect(Collectors.joining(" ")));
                }
                return true;
            }
            List<Integer> b=new ArrayList<>();
            for (int j = 0; j < n; j++) {
                b.add(j+1);
            }
            Collections.sort(b, Comparator.comparingInt(x->-a.get(x-1)));
            for (int j = 0; j < n; j++) {
                a.set(j, a.get(j)+b.get(j));
            }
            res.add(b);
        }
        printYesNo(false);
        return false;
    }

    public static void main(String[] args) throws Exception {
        new Main().resolve();
        out.flush();
    }
}

