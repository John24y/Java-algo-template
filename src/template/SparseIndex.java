package template;

import java.util.*;

class HashSparseIndex {
    Map<Long,Integer> map = new HashMap<>();
    Map<Integer,Long> rev = new HashMap<>();
    public void add(long num) {
        map.put(num,-1);
    }
    public void generateIndex() {
        List<Long> list = new ArrayList<>(map.keySet());
        Collections.sort(list);
        for (int i = 0; i < list.size(); i++) {
            map.put(list.get(i),i);
            rev.put(i,list.get(i));
        }
    }
    public int getIndex(long num) {
        return map.get(num);
    }
    public long revertIndex(int index) {
        return rev.get(index);
    }
}

class ArraySparseIndex {
    Set<Long> set = new HashSet<>();
    long[] ar;
    public void add(long num) {
        set.add(num);
    }
    public void generateIndex() {
        ar=new long[set.size()];
        set.add(Long.MIN_VALUE);
        set.add(Long.MAX_VALUE);
        ar=new long[set.size()];
        int i=0;
        for (Long l : set) {
            ar[i++]=l;
        }
        Arrays.sort(ar);
        set = null;
    }
    public int ge(long num) {
        return bisect(num, true);
    }
    public int le(long num) {
        return bisect(num, true)-1;
    }
    public int ge(double val) {
        return ge((long)Math.ceil(val));
    }
    public int le(double val) {
        return le((long)val);
    }
    public long revertIndex(int index) {
        return ar[index];
    }
    public int bisect(long val, boolean bisectLeft) {
        int low=0,high= ar.length-1;
        while (low<=high){
            int mid=low+high>>1;
            boolean pred = bisectLeft ? ar[mid]>=val : ar[mid] > val;
            if (pred) {
                high=mid-1;
            } else {
                low=mid+1;
            }
        }
        return low;
    }
}