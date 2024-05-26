package template;

import java.util.Collection;
import java.util.HashMap;

@SuppressWarnings("all")
class Counter<K> extends HashMap<K,Integer> {

    public Counter() {
    }

    public Counter(Collection<K> collection) {
        for (K k : collection) {
            add(k, 1);
        }
    }

    public void addInts(int[] ints) {
        for (int anInt : ints) {
            add((K) (Integer) anInt, 1);
        }
    }

    public void addLongs(long[] longs) {
        for (long v : longs) {
            add((K) (Long) v, 1);
        }
    }

    public void add(K key, int cnt) {
        int newV = get(key) + cnt;
        if (newV == 0) {
            remove(key);
        } else {
            put(key, newV);
        }
    }

    @Override
    public Integer get(Object key) {
        return super.getOrDefault(key, 0);
    }
}
