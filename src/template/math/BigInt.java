package template.math;

import java.util.Arrays;

/**
 * 十进制大数运算。假设数字长度为n，Java自带大数运算乘法复杂度O(n^2)，此类用NTT实现乘法复杂度为O(nlogn).
 * FFT可能被卡精度，需要用NTT，因为不能对数位取模（否则也不会用大数乘法了），最大的位不能超过模数，除法也不能用逆元。
 * 两个长度为n的数字相乘后，最大的位会达到 n*9*9，所以要及时进位，否则连续相乘会溢出。
 * 运算结果原地修改。
 *
 * https://www.luogu.com.cn/problem/P2000
 */
class BigInt {

    interface PolyMul {
        int[] polyMul(int[] a, int[] b, int resPaddingLen);
        int[] polySquare(int[] a, int resPaddingLen);
    }

    static PolyMul polyMul = new PolyMul() {
        @Override
        public int[] polyMul(int[] a, int[] b, int resPaddingLen) {
            return NTT.polyMul(a, b, resPaddingLen);
        }

        @Override
        public int[] polySquare(int[] a, int resPaddingLen) {
            return NTT.polySquare(a, resPaddingLen);
        }
    };

    int[] digit;

    public BigInt(String s) {
        int n=s.length();
        digit=new int[n];
        for (int i = 0; i < s.length(); i++) {
            digit[i]=s.charAt(n-i-1)-'0';
        }
    }

    public BigInt(int[] digit) {
        this.digit=digit;
    }

    public BigInt add(int val) {
        assert val>=0;
        int n=digit.length;
        int rem=0;
        digit[0]+=val;
        assert digit[0]>=0;
        for (int i = 0; i < n; i++) {
            if (digit[i]>9) {
                if (i+1>=n){
                    rem=digit[i]/10;
                } else {
                    digit[i+1]+=digit[i]/10;
                }
                digit[i]%=10;
            }
        }
        if (rem>0) {
            int[] a=new int[n+12];
            System.arraycopy(digit,0,a,0,n);
            a[n]=rem;
            for (int i = 0; i < 12; i++) {
                if (a[n+i]>9){
                    a[n+i+1]+=a[n+i]/10;
                    a[n+i]%=10;
                }
            }
            this.digit=a;
        }
        return this;
    }

    public BigInt sub(int val) {
        assert val>=0;
        int n=digit.length;
        digit[0]-=val;
        for (int i = 0; i < n; i++) {
            if (digit[i]>=0) break;
            //不支持减到负数
            if (i+1>=n) throw new UnsupportedOperationException();
            int b=(-digit[i]%10);
            digit[i+1]-=1+(-digit[i]/10);
            digit[i]=b==0?0:10-b;
        }
        return this;
    }

    public BigInt mul(BigInt b) {
        digit=polyMul.polyMul(digit, b.digit, 15);
        add(0);
        return this;
    }

    public BigInt square() {
        digit=polyMul.polySquare(this.digit, 15);
        add(0);
        return this;
    }

    public BigInt div(int val) {
        assert val>=0;
        int n=digit.length,c=0;
        for (int i = n-1; i >= 0; i--) {
            c=c%val*10+digit[i];
            digit[i]=c/val;
        }
        return this;
    }

    @Override
    public String toString() {
        int h=digit.length-1;
        while (h>=0&&digit[h]==0) h--;
        if (h<0) return "0";
        StringBuilder sb=new StringBuilder();
        while (h>=0) {
            if (digit[h]>=10) {
                //toString不能改变对象状态
                return Arrays.toString(digit);
            }
            sb.append((char)('0'+digit[h]));
            h--;
        }
        return sb.toString();
    }
}
