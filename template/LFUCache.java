import java.util.*;

/**
 * @Author Create by jiaxiaozheng
 * @Date 2023/1/29
 */
class LFUCache {

    static final int MAX_CALL=100000;

    class Node {
        int key;
        int value;
        boolean removed;
        int freq;
    }

    List<LinkedList<Node>> freq = new ArrayList<>();
    int[] freqCount = new int[MAX_CALL+1];
    Map<Integer,Node> map=new HashMap<>();
    int capa;
    int minFreq=1;

    public LFUCache(int capacity) {
        capa=capacity;
        for (int i = 0; i <= MAX_CALL; i++) {
            freq.add(new LinkedList<>());
        }
    }

    public int get(int key) {
        Node node = map.get(key);
        if (node==null){
            return -1;
        }
        return incFreq(node).value;
    }

    public void put(int key, int value) {
        if (capa==0){
            return;
        }
        Node node = map.get(key);
        if (node==null){
            if (map.size()>=capa) {
                evict();
            }
            Node node1 = new Node();
            node1.key=key;
            incFreq(node1).value=value;
        } else {
            incFreq(node).value=value;
        }
    }

    private void evict() {
        while (freqCount[minFreq]==0) {
            minFreq++;
        }
        LinkedList<Node> list = freq.get(minFreq);
        assert !list.isEmpty();
        while (list.peekFirst().removed) {
            list.removeFirst();
        }
        assert !list.isEmpty();
        Node node = list.removeFirst();
        map.remove(node.key);
        freqCount[minFreq]--;
    }

    private Node incFreq(Node node) {
        node.removed=true;
        Node node1 = new Node();
        node1.key=node.key;
        node1.value=node.value;
        node1.freq=node.freq+1;
        freq.get(node1.freq).addLast(node1);
        freqCount[node.freq]--;
        freqCount[node1.freq]++;
        map.put(node1.key, node1);
        minFreq=Math.min(node1.freq, minFreq);
        return node1;
    }

    public static void main(String[] args) {
        LFUCache cache = new LFUCache(0);
        cache.put(0,0);
        int a = cache.get(0);
        System.out.println(a);
    }
}