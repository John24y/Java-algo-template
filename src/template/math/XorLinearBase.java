package template.math;

class XorLinearBase {
    long[] p=new long[64], d=new long[64];
    int cnt=0; //线性基的个数
    boolean zero; //原数列是否有0，有0的话能异或得到0

    void insert(long x) {
        if (x==0) {
            zero=true;
            return;
        }
        for(int i=62;i>=0;i--){
            if((x>>i)>0) {
                if(p[i]==0) {
                    p[i]=x;
                    cnt++;
                    break;
                } else {
                    x^=p[i];
                }
            }
        }
    }

    boolean gen(long x) {
        for(int i=62;i>=0;i--) {
            if((x>>i)>0) x^=p[i];
        }
        return x==0;
    }

    long max() {
        if (zero&&cnt==0) return 0;
        long ans=0;
        for(int i=62;i>=0;i--){
            if((ans^p[i])>ans) ans^=p[i];
        }
        return ans;
    }

    long min() {
        if(zero) return 0;
        for(int i=0;i<=62;i++) {
            if(p[i]>0) return p[i];
        }
        return -1;
    }

    void rebuild() {
        cnt=0;
        for(int i=62;i>=0;i--) {
            for(int j=i-1;j>=0;j--) {
                if((p[i]&(1L<<j))>0) p[i]^=p[j];
            }
        }
        for(int i=0;i<=62;i++) {
            if(p[i]>0) d[cnt++]=p[i];
        }
    }

    long kth(int k) {
        if (k==1 && zero) return 0;
        if(k>=(1L<<cnt)) return -1;
        long ans=0;
        for(int i=62;i>=0;i--) {
            if((k&(1L<<i))>0) ans^=d[i];
        }
        return ans - (zero ? 1 : 0);
    }

    int rank(long x) {
        int ans = 0;
        for(int i = cnt - 1; i >= 0; i --) {
            if(x >= d[i]) {
                ans += (1 << i);
                x ^= d[i];
            }
        }
        return ans + (zero ? 1 : 0);
    }

}
