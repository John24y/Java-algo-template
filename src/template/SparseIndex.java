package template;

import java.util.*;

class SparseIndex {
    Map<Long,Integer> map = new HashMap<>();
    public void add(long num) {
        map.put(num,-1);
    }
    public void generateIndex() {
        List<Long> list = new ArrayList<>(map.keySet());
        Collections.sort(list);
        for (int i = 0; i < list.size(); i++) {
            map.put(list.get(i),i);
        }
    }
    public int getIndex(long num) {
        return map.get(num);
    }
}
