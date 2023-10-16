package template.bst;

import java.util.Map;
import java.util.TreeMap;

class IntervalTree {
    //<end1,[end2,h]>
    TreeMap<Integer,int[]> map=new TreeMap<>();

    //[l,r], -MaxInt < h < MaxInt
    public void add(int l, int r, int h) {
        Map.Entry<Integer, int[]> entry = null;
        while ((entry=map.ceilingEntry(l))!=null) {

            entry=map.ceilingEntry(l);
        }
    }

    public void height(int i) {

    }
}