package main.nk3;
import java.io.*;
import java.util.Arrays;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Main {
    public static final int MAX = 100005;
    public static SegmentTree[] t = new SegmentTree[MAX * 400];
    public static Operation[] q = new Operation[MAX];
    public static int[] a = new int[MAX];
    public static int[] o = new int[MAX << 1];
    public static int[] rt = new int[MAX];
    public static int[][] temp = new int[2][20];
    public static int[] cnt = new int[2];
    public static int n, m, len, tot;

    static {
        for (int i = 0; i < MAX * 400; i++) {
            t[i] = new SegmentTree();
        }
        for (int i = 0; i < MAX; i++) {
            q[i] = new Operation();
        }
    }

    public static class SegmentTree {
        int v, ls, rs;
    }

    public static class Operation {
        boolean b;
        int l, r, k, pos, t;
    }

    public static void modify(int now, int l, int r, int pos, int val) {
        t[now].v += val;
        if (l == r) return;
        int mid = (l + r) >> 1;
        if (pos <= mid) {
            if (t[now].ls == 0) t[now].ls = ++tot;
            modify(t[now].ls, l, mid, pos, val);
        } else {
            if (t[now].rs == 0) t[now].rs = ++tot;
            modify(t[now].rs, mid + 1, r, pos, val);
        }
    }

    public static void prepare_Modify(int x, int val) {
        int k = Arrays.binarySearch(o, 1, len + 1, a[x]);
        for (int i = x; i <= n; i += i & -i){
            if (rt[i]==0) rt[i]=++tot;
            modify(rt[i], 1, len, k, val);
        }
    }

    static int Query(int l,int r,int k)
    {
        if (l==r) return l;
        int mid=l+r>>1,sum=0;
        for (int i=1;i<=cnt[1];i++) sum+=t[t[temp[1][i]].ls].v;
        for (int i=1;i<=cnt[0];i++) sum-=t[t[temp[0][i]].ls].v;
        if (k<=sum)
        {
            for (int i=1;i<=cnt[1];i++) temp[1][i]=t[temp[1][i]].ls;
            for (int i=1;i<=cnt[0];i++) temp[0][i]=t[temp[0][i]].ls;
            return Query(l,mid,k);
        }
        else
        {
            for (int i=1;i<=cnt[1];i++) temp[1][i]=t[temp[1][i]].rs;
            for (int i=1;i<=cnt[0];i++) temp[0][i]=t[temp[0][i]].rs;
            return Query(mid+1,r,k-sum);
        }
    }
    static int prepare_Query(int l,int r,int k)
    {
        cnt[0]=cnt[1]=0;
        for (int i=r;i>0;i-=i&-i) temp[1][++cnt[1]]=rt[i];
        for (int i=l-1;i>0;i-=i&-i) temp[0][++cnt[0]]=rt[i];
        return Query(1,len,k);
    }

    public static void main(String[] args) {
        n = nextInt();
        m = nextInt();
        for (int i = 1; i <= n; i++) {
            a[i] = nextInt();
            o[++len] = a[i];
        }
        for (int i = 1; i <= m; i++) {
            String opt = next();
            q[i].b = opt.equals("Q");
            if (q[i].b) {
                q[i].l = nextInt();
                q[i].r = nextInt();
                q[i].k = nextInt();
            } else {
                q[i].pos = nextInt();
                q[i].t = nextInt();
                o[++len] = q[i].t;
            }
        }
        Arrays.sort(o, 1, len + 1);
        Arrays.sort(o, 1, len + 1);
        int uniqueLen = 1;
        for (int i = 2; i <= len; i++) {
            if (o[i] != o[uniqueLen]) {
                o[++uniqueLen] = o[i];
            }
        }
        len = uniqueLen;
        for (int i = 1; i <= n; i++) prepare_Modify(i, 1);
        for (int i = 1; i <= m; i++) {
            if (q[i].b) {
                out.println(o[prepare_Query(q[i].l, q[i].r, q[i].k)]);
            } else {
                prepare_Modify(q[i].pos, -1);
                a[q[i].pos] = q[i].t;
                prepare_Modify(q[i].pos, 1);
            }
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
