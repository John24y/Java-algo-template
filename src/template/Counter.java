package template;

import java.util.HashMap;
import java.util.function.BiFunction;

class Counter<K> extends HashMap<K,Long> {

    long add(K k, long val) {
        return compute(k, new BiFunction<K, Long, Long>() {
            @Override
            public Long apply(K k, Long aLong) {
                return (aLong==null?0:aLong)+val;
            }
        });
    }

    @Override
    public Long get(Object key) {
        return super.getOrDefault(key, 0L);
    }
}
