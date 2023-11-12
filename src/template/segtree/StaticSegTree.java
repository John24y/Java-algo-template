package template.segtree;

class StaticSegTree {
    //对查询结果相加需要注意溢出
    static final long INIT = Long.MIN_VALUE;
    static final byte OP_ADD = 1;
    static final byte OP_SET = 2;

    long[] maxVal;
    int[] maxId;
    byte[] lazyType;
    long[] lazyVal;
    long[] sum;

    int maxN, varStart, varIdx;

    public StaticSegTree(int maxN, long[] initVals) {
        this.maxN = maxN;
        varIdx = varStart = Integer.highestOneBit(maxN) << 2;
        int len = varStart + 36;
        maxVal = new long[len];
        maxId = new int[len];
        lazyType = new byte[len];
        lazyVal = new long[len];
        sum = new long[len];
        //build(1, initVals, 0, maxN);
        for (int i = 0; i < maxId.length; i++) {
            maxId[i]=i;
            maxVal[i]=INIT;
        }
    }

    private void build(int i, long[] vals, int ls, int rs) {
        if (ls == rs) {
            maxId[i] = ls;
            maxVal[i] = vals == null ? INIT : vals[ls];
            return;
        }
        int mid = ls + rs >> 1;
        build(i * 2, vals, ls, mid);
        build(i * 2 + 1, vals, mid + 1, rs);
        reduce(i, i * 2, i * 2 + 1, ls, rs);
    }

    void apply(int i, int ls, int rs, byte type, long val) {
        lazyType[i] = type;
        if (type == OP_ADD) {
            lazyVal[i] += val;
            sum[i] += (rs - ls + 1) * val;
            maxVal[i] += val;
        } else if (type == OP_SET) {
            lazyVal[i] = val;
            sum[i] = (rs - ls + 1) * val;
            maxVal[i] = val;
        }
    }

    void reduce(int i, int left, int right, int ls, int rs) {
        sum[i] = sum[left] + sum[right];
        if (maxVal[right] > maxVal[left]) {
            maxId[i] = maxId[right];
            maxVal[i] = maxVal[right];
        } else {
            maxId[i] = maxId[left];
            maxVal[i] = maxVal[left];
        }
    }

    public void add(int l, int r, long val) {
        update(1, l, r, 0, maxN, OP_ADD, val);
    }

    public void set(int l, int r, long val) {
        update(1, l, r, 0, maxN, OP_SET, val);
    }

    /**
     * 当前Node的范围: [ls,rs]
     */
    private void update(int i, int l, int r, int ls, int rs, byte type, long val) {
        if (l < 0 || r > maxN) {
            throw new IllegalArgumentException();
        }
        if (l <= ls && rs <= r) {
            apply(i, ls, rs, type, val);
            return;
        }

        pushDown(i, ls, rs);
        int mid = ls + rs >> 1;
        //左子树[ls,mid]
        //右子树[mid+1,rs]
        if (l <= mid) {
            update(i * 2, l, r, ls, mid, type, val);
        }
        if (r >= mid + 1) {
            update(i * 2 + 1, l, r, mid + 1, rs, type, val);
        }
        reduce(i, i * 2, i * 2 + 1, ls, rs);
    }

    //下发lazy值
    void pushDown(int i, int ls, int rs) {
        int mid = ls + rs >> 1;
        if (lazyType[i] != 0) {
            apply(i * 2, ls, mid, lazyType[i], lazyVal[i]);
            apply(i * 2 + 1, mid + 1, rs, lazyType[i], lazyVal[i]);
            lazyType[i] = 0;
            lazyVal[i] = 0;
        }
    }

    public long sum(int l, int r) {
        varIdx = varStart;
        return sum[query(1, l, r, 0, maxN)];
    }

    //查询最大值
    public long queryMaxVal(int l, int r) {
        varIdx = varStart;
        return maxVal[query(1, l, r, 0, maxN)];
    }

    //查询最大值的下标，如果有多个则取下标最小的
    public int queryMaxId(int l, int r) {
        varIdx = varStart;
        return maxId[query(1, l, r, 0, maxN)];
    }

    private void cleanNode(int i) {
        maxVal[i]=INIT;
        maxId[i]=-1;
    }

    private int query(int i, int l, int r, int ls, int rs) {
        if (l < 0 || r > maxN) {
            throw new IllegalArgumentException();
        }
        if (l <= ls && rs <= r) {
            return i;
        }
        pushDown(i, ls, rs);
        int res = ++varIdx;
        cleanNode(res);
        int leftRes=varStart, rightRes=varStart;
        int mid = ls + rs >> 1;
        if (l <= mid) {
            leftRes=query(i*2, l, r, ls, mid);
        }
        if (r >= mid + 1) {
            rightRes=query(i*2+1, l, r, mid + 1, rs);
        }
        reduce(res, leftRes, rightRes, ls, rs);
        return res;
    }

}