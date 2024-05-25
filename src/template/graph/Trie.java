package template.graph;

import java.util.Map;

class Trie {
    static int DIM = 26;
    Trie[] child = new Trie[DIM];
    int val = 0;

    void add(String s, int val) {
        Trie cur = this;
        for (int i = 0; i < s.length(); i++) {
            int c = s.charAt(i) - 'a';
            // cur是c的父节点, cur.child[c]是c的节点
            cur.val += val;
            if (cur.child[c] == null) cur.child[c] = new Trie();
            cur = cur.child[c];
        }
        cur.val += val;
    }

    int get(String s) {
        Trie cur = this;
        for (int i = 0; i < s.length(); i++) {
            int c = s.charAt(i) - 'a';
            if (cur.child[c] == null) {
                //当前字符c匹配失败，返回s最后匹配节点的val
                return cur.val;
            } else {
                cur = cur.child[c];
            }
        }
        return cur.val;
    }
}
