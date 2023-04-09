package main;

import java.io.*;

public class Round829_c1 {
    /**
     * 快读快写
     */
    static StreamTokenizer in = new StreamTokenizer(new BufferedReader(new InputStreamReader(System.in)));
    static PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
    static public double nextDouble() throws IOException{ in.nextToken(); return in.nval; }
    static public float nextFloat() throws IOException{ in.nextToken(); return (float)in.nval; }
    static public int nextInt() throws IOException { in.nextToken(); return (int)in.nval; }
    static public String next() throws IOException{ in.nextToken(); return in.sval;}
    static BufferedReader re = new BufferedReader(new InputStreamReader(System.in));

    void solve(int[] ar) {
        long sum=0;
        for (int i : ar) {
            sum+=i;
        }
        int n=ar.length;
        if (sum==0){
            out.println(n);
            for (int i = 0; i < n; i++) {
                out.println((i+1)+" "+(i+1));
            }
            return;
        }
        //插入负号
        int target=(int) Math.abs(sum/2);
        int[][] dp=new int[n][2];//0 前面不插入
        int i = 0;
        for (i = 1; i < n; i++) {
            if ((sum>0)!=(ar[i]>0)) {
                dp[i][0]=Math.max(dp[i-1][0],dp[i-1][1]);
                continue;
            }
            dp[i][0]=Math.max(dp[i-1][0],dp[i-1][1]);
            dp[i][1]=dp[i-1][0]+1;
            if (dp[i][1]==target){
                break;
            }
        }
        if (i==n){
            out.println(-1);
            return;
        }
        int t=target;
        boolean[] revert=new boolean[n];
        for (int j = i; j >=0 ; j--) {
            if (dp[j][1]==t){
                revert[j]=true;
                j--;
                t--;
            }
            if (t==0){
                break;
            }
        }

        int group=n-target;
        out.println(group);
        for (int j = 0; j < n; j++) {
            if (j<n-1&&revert[j+1]){
                out.println((j+1)+" "+(j+2));
                j++;
            } else {
                out.println((j+1)+" "+(j+1));
            }
        }
    }

    public static void main(String[] args) throws Exception {
        int t = nextInt();
        for (int i = 0; i < t; i++) {
            int n=nextInt();
            int[] ar=new int[n];
            for (int j = 0; j < n; j++) {
                ar[j]=nextInt();
            }
            new Round829_c1().solve(ar);
            out.flush();
        }
    }


}

