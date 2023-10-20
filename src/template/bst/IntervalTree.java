package template.bst;

import java.util.Map;
import java.util.TreeMap;

class IntervalTree {
    //<end1,[end2,h]>
    TreeMap<Long,long[]> map=new TreeMap<>();

    //区间[l,r]置1
    public void add(long l, long r) {
        modify(l, r, true);
    }

    //区间[l,r]置0
    public void remove(long l, long r) {
        modify(l, r, false);
    }

    private void modify(long l, long r, boolean add) {
        long L = l, R = r;
        Map.Entry<Long,long[]> entry = null;
        while ((entry=map.ceilingEntry(l))!=null) {
            //遍历左或右端点>=l的区间，然后通过迭代区间左端点判断是否有交集
            if (entry.getValue()[0]>r) break;
            R = Math.max(R, entry.getValue()[1]);
            L = Math.min(L, entry.getValue()[0]);
            internalDel(entry.getValue());
        }
        if (add) {
            internalPut(new long[]{L,R});
        } else {
            if (L<l) {
                internalPut(new long[]{L, l - 1});
            }
            if (R>r) {
                internalPut(new long[]{r+1, R});
            }
        }
    }

    public long[] getCover(long i) {
        Map.Entry<Long, long[]> entry = map.ceilingEntry(i);
        if (entry==null || entry.getValue()[0]>i) return null;
        return entry.getValue();
    }

    //移除当前覆盖点i的连续区间，而不是只移除点i
    public long[] removeCover(long i) {
        long[] value = getCover(i);
        internalDel(value);
        return value;
    }

    private void internalPut(long[] longs) {
        map.put(longs[0], longs);
        if (longs[0]!=longs[1]) {
            map.put(longs[1], longs);
        }
    }
    private void internalDel(long[] longs) {
        map.remove(longs[0]);
        if (longs[0]!=longs[1]) {
            map.remove(longs[1]);
        }
    }

}