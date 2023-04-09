

import java.util.Arrays;

public class ZFunction {

    public int[] z(String s) {
        int n=s.length();
        int[] z=new int[n];
        z[0]=n;
        int left=0,right=-1;
        for (int i = 1; i < n; i++) {
            if (right>=i && z[i-left] < right-i+1) {
                z[i]=z[i-left];
            } else {
                left=i;
                right=Math.max(right,i-1);
                while (right<n-1 && s.charAt(right+1) == s.charAt(right-i+1)){
                    right++;
                }
                z[i]=right-i+1;
            }
        }
        return z;
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(new ZFunction().z("abcabcdabc")));
    }
}
