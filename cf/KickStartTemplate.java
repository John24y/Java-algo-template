
import java.io.*;

public class KickStartTemplate {
    static StreamTokenizer in = new StreamTokenizer(new BufferedReader(new InputStreamReader(System.in)));
    static PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
    public static double nextDouble() throws IOException{ in.nextToken(); return in.nval; }
    public static float nextFloat() throws IOException{ in.nextToken(); return (float)in.nval; }
    public static int nextInt() throws IOException { in.nextToken(); return (int)in.nval; }
    public static String next() throws IOException{ in.nextToken(); return in.sval;}
    static BufferedReader re = new BufferedReader(new InputStreamReader(System.in));

    public long solve(int l,long n) throws Exception {
        return 0;
    }

    public static void main(String[] args) throws Exception {
        int T=nextInt();
        for (int i = 0; i < T; i++) {
            int L=nextInt();
            int N=nextInt();
            long r = new KickStartTemplate().solve(L,N);
            System.out.printf("Case #%d: %d\n", i+1,r);
        }
    }

}

