package main.at;

import java.io.*;
import java.util.*;

class SA {

    String s;
    int n, m;
    int[] sa, rk, oldrk, tmpsa, cnt, height;

    public SA(String s) {
        this.s = s;
        n = s.length();
        m = 128;
        sa = new int[Math.max(n, m) + 1];
        rk = new int[Math.max(n, m) + 1];
        cnt = new int[Math.max(n, m) + 2];
        tmpsa = new int[Math.max(n, m) + 2];//+1会WA??
        oldrk = new int[2 * Math.max(n, m) + 2];
        sa();
    }

    /**
     * time: O(nlogn)
     * 用开始下标表示后缀字符串 (1<=i<=n)
     * sa[i]是第i小后缀 (sa of i-th smallest)
     * rk[i]是后缀i的排名 (rk of suffix i)
     */
    private void sa() {
        int n = s.length(), p = 0, w = 0, i = 0;
        for (i = 1; i <= n; i++) {
            //tmpsa是按照第2关键字排序后的sa，第一轮排序用不到，可随意初始化
            tmpsa[i] = i;
            rk[i] = s.charAt(i - 1);
        }
        radixSort();
        // m=p 就是优化计数排序值域
        for (w = 1; ; w <<= 1, m = p) {
            //按照第2关键字排序，最后w个后缀没有第2关键字(关键字为0)，字典序最小，所以放在最前面
            for (p = 0, i = n; i > n - w; --i) tmpsa[++p] = i;
            //剩下的后缀要保留上一轮的排序，按照上轮名次枚举。
            //检查当前rk是否可做为某个后缀的第2关键字，应该刚好有n-w个rk满足条件
            for (i = 1; i <= n; ++i) if (sa[i] > w) tmpsa[++p] = sa[i] - w;

            radixSort();
            System.arraycopy(rk, 0, oldrk, 0, rk.length);
            //把sa翻译到rk，2个关键字都相同的后缀，rk要一样
            for (p = 0, i = 1; i <= n; i++) {
                //还需要上一轮的rk数组来对比是否相同大小的后缀
                if (oldrk[sa[i]] == oldrk[sa[i - 1]] && oldrk[sa[i] + w] == oldrk[sa[i - 1] + w]) {
                    rk[sa[i]] = p;
                } else {
                    rk[sa[i]] = ++p;
                }
            }
            if (p == n) {
                //如果有n个不同的名次了，可以提前返回结果。
                //w是倍增的，当w超过n时，按第2关键字排完顺序不变，再按第1关键字排完必然得到n个不同名次。
                for (i = 1; i <= n; ++i) sa[rk[i]] = i;
                break;
            }
        }
    }

    /**
     * height[i]表示 sa[i] 与 sa[i-1] 的最长公共前缀
     */
    public int[] height() {
        height = new int[n+1];
        int h=0;
        for (int i = 1; i <= n; i++) {
            if (rk[i]==1) continue;
            if (h>0) h--;
            int j = sa[rk[i]-1];
            while (i+h<=n && j+h<=n && s.charAt(i+h-1) == s.charAt(j+h-1)){
                h++;
            }
            height[rk[i]]=h;
        }
        return height;
    }

    private void radixSort() {
        java.util.Arrays.fill(cnt, 0);
        for (int i = 1; i <= n; i++) ++cnt[rk[i]];
        for (int i = 1; i <= m; i++) cnt[i] += cnt[i - 1];
        //tmpsa是按照第2关键字排序后的sa，此时要做按照第1关键字做稳定排序
        //因为是稳定排序，所以必须按照tmpsa中的顺序来遍历suffix，然后放到新的sa数组中
        for (int i = n; i >= 1; i--) sa[cnt[rk[tmpsa[i]]]--] = tmpsa[i];
    }

}
public class Main {
    public List<List<Integer>> equalSubStr(String s, int m) {
        SA sa = new SA(s);
        int n = s.length();
        int[] height = sa.height();
        List<List<Integer>> ans = new ArrayList<>();
        for (int i = 1; i <= n; ) {
            if (sa.sa[i] + m > n + 1) {
                //跳过长度不足m的后缀
                ++i;
                continue;
            }
            int j = i+1;
            List<Integer> same = new ArrayList<>();
            same.add(sa.sa[i]);
            for (;j <= n && height[j]>=m; j++) {
                same.add(sa.sa[j]);
            }
            Collections.sort(same);
            ans.add(same);
            i=j;
        }
        return ans;
    }
    void solve() {
        int n=readInt(),m=readInt(),k=readInt();
        String s=read();
        List<List<Integer>> list = equalSubStr(s, m);
        int res=0;
        for (List<Integer> li : list) {
            int cnt=0;
            int last = Integer.MIN_VALUE;
            for (Integer v : li) {
                if (v>=last+m) {
                    cnt++;
                    last=v;
                }
            }
            if (cnt==k){
                res++;
            }
        }
        out.println(res);
    }

    public static void main(String[] args) throws Exception {
        new Main().solve();
        out.flush();
    }

    static PrintWriter out = new PrintWriter(System.out, false);
    static InputReader in = new InputReader(System.in);
    static String read() { return in.next(); }
    static int readInt() { return Integer.parseInt(in.next()); }
    static long readLong() { return Long.parseLong(in.next()); }
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

