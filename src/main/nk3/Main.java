package main.nk3;
import java.io.*;
import java.util.*;
import java.text.DecimalFormat;

public class Main {

    static final double INF = 1e18;
    static final int MAXN = 305;

    static class MaxMatch {
        int n;
        double[][] W = new double[MAXN][MAXN];
        double[] Lx = new double[MAXN];
        double[] Ly = new double[MAXN];
        boolean[] S = new boolean[MAXN];
        boolean[] T = new boolean[MAXN];
        int[] left = new int[MAXN];

        boolean match(int i) {
            S[i] = true;
            for (int j = 1; j <= n; j++) {
                if (Math.abs(Lx[i] + Ly[j] - W[i][j]) < 1e-7 && !T[j]) {
                    T[j] = true;
                    if (left[j] == -1 || match(left[j])) {
                        left[j] = i;
                        return true;
                    }
                }
            }
            return false;
        }

        void update() {
            double a = INF;
            for (int i = 1; i <= n; i++) {
                if (S[i]) {
                    for (int j = 1; j <= n; j++) {
                        if (!T[j]) {
                            a = Math.min(a, Lx[i] + Ly[j] - W[i][j]);
                        }
                    }
                }
            }
            for (int i = 1; i <= n; i++) {
                if (S[i]) Lx[i] -= a;
                if (T[i]) Ly[i] += a;
            }
        }

        double solve(int n) {
            this.n = n;
            Arrays.fill(left, -1);
            for (int i = 1; i <= n; i++) {
                Lx[i] = 0;
                for (int j = 1; j <= n; j++) {
                    Lx[i] = Math.max(Lx[i], W[i][j]);
                }
            }
            for (int i = 1; i <= n; i++) {
                while (true) {
                    Arrays.fill(S, false);
                    Arrays.fill(T, false);
                    if (match(i)) break;
                    else update();
                }
            }
            double ans = 0;
            for (int i = 1; i <= n; i++) {
                ans += W[left[i]][i];
            }
            return -ans;
        }
    }

    static class Pair {
        int first, second;

        Pair(int f, int s) {
            this.first = f;
            this.second = s;
        }
    }

    static MaxMatch KM = new MaxMatch();
    static Pair[] E = new Pair[1005];
    static int[] wa = new int[1005];
    static int[] wb = new int[1005];
    static int n, m;

    static boolean chk(double ans) {
        for (int i = 1; i <= n; i++) {
            Arrays.fill(KM.W[i], -INF);
        }
        for (int i = 1; i <= m; i++) {
            int u = E[i].first;
            int v = E[i].second;
            KM.W[u][v] = Math.max(KM.W[u][v], wa[i] - wb[i] * ans);
        }
        double nw = KM.solve(n);
        return nw <= 0;
    }

    static void BellaKira() {
        Scanner sc = new Scanner(System.in);
        n = sc.nextInt();
        m = sc.nextInt();
        for (int i = 1; i <= m; i++) {
            int u = sc.nextInt();
            int v = sc.nextInt();
            E[i] = new Pair(u, v);
            wa[i] = sc.nextInt();
            wb[i] = sc.nextInt();
        }
        double l = 0, r = (1 << 30);
        while (r - l > 1e-7) {
            double mid = l + (r - l) / 2;
            if (chk(mid)) {
                l = mid;
            } else {
                r = mid;
            }
        }
        DecimalFormat df = new DecimalFormat("#.######");
        System.out.println(df.format(l));
    }

    public static void main(String[] args) {
        int T = 1;
        while (T-- > 0) {
            BellaKira();
        }
    }
}
