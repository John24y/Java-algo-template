package main;

import java.util.Scanner;


public class CF558C {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] ar = new int[n];
        for (int i = 0; i < n; i++) {
            ar[i] = sc.nextInt();
        }
        //求最长二进制公共前缀
        int comm = ar[0];
        for (int i : ar) {
            while (comm != i) {
                if (comm > i) {
                    comm >>= 1;
                } else {
                    i >>= 1;
                }
            }
        }
        int res=Integer.MAX_VALUE;
        for (int i = 0; i < 20; i++) {
            int cur=0;
            if (comm<<i>>i != comm) {
                continue;
            }
            int target=comm<<i;
            for (int k : ar) {
                //如果a整除b，那么a和b是移位可得关系
                while (k > target || k!=target && target%k !=0){
                    k>>=1;
                    cur++;
                }
                while (k!=target){
                    k<<=1;
                    cur++;
                }
            }
            res=Math.min(res,cur);
        }
        System.out.println(res);
    }

}