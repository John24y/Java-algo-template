package template;

class SparseTable {

    interface Merger {
        long merge(long sum1, long sum2);
    }

    private int[] lg;
    private long[][] f;
    private Merger merger;

    public SparseTable(long[] ar, Merger merger) {
        this.merger=merger;
        int n=ar.length;
        lg = new int[n+1];
        for (int i = 2; i <= n; i++) {
            lg[i]=lg[i>>1]+1;
        }
        f=new long[lg[n]+1][n];
        System.arraycopy(ar,0, f[0],0,n);
        for (int i = 1; i <= lg[n]; i++) {
            for (int j = 0; j+(1<<i) <= n; j++) {
                f[i][j]=merger.merge(f[i-1][j], f[i-1][j+(1<<(i-1))]);
            }
        }
    }

    //查询闭区间 [l,r]，0<=l,r<n
    public long query(int l,int r) {
        int len=r-l+1;
        return merger.merge(f[lg[len]][l], f[lg[len]][r-(1<<lg[len])+1]);
    }

    //查询闭区间 [l,r]，0<=l,r<n. 复杂度O(logn)
    public long queryNoRepeat(int l,int r) {
        int len=r-l+1;
        int remain=len-(1<<lg[len]);
        if (remain==0){
            return f[lg[len]][l];
        } else {
            return merger.merge(f[lg[len]][l], queryNoRepeat(l+(1<<lg[len]), r));
        }
    }

}

