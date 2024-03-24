package main.lc;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

class Trie {
    static int DIM = 26;
    Trie[] child = new Trie[DIM];
    int len = Integer.MAX_VALUE;
    int i = Integer.MAX_VALUE;

    void add(String s, int len, int idx) {
        Trie cur = this;
        for (int i = 0; i < s.length(); i++) {
            int c = s.charAt(i) - 'a';
            if (len < cur.len) {
                cur.len = len;
                cur.i = idx;
            } else if (len == cur.len) {
                cur.i = Math.min(cur.i, idx);
            }
            if (cur.child[c] == null) cur.child[c] = new Trie();
            cur = cur.child[c];
        }
        if (len < cur.len) {
            cur.len = len;
            cur.i = idx;
        } else if (len == cur.len) {
            cur.i = Math.min(cur.i, idx);
        }
    }

    int get(String s) {
        Trie cur = this;
        for (int i = 0; i < s.length(); i++) {
            int c = s.charAt(i) - 'a';
            if (cur.child[c] == null) {
                //当前字符c匹配失败，返回s最后匹配节点的val
                return cur.i;
            } else {
                cur = cur.child[c];
            }
        }
        return cur.i;
    }
}
class Solution {
    public int[] stringIndices(String[] wordsContainer, String[] wordsQuery) {
        Trie trie = new Trie();
        for (int i = 0; i < wordsContainer.length; i++) {
            trie.add(new StringBuilder(wordsContainer[i]).reverse().toString(), wordsContainer[i].length(),i
                    );
        }
        int[] res=new int[wordsQuery.length];
        for (int i = 0; i < wordsQuery.length; i++) {
            res[i]=trie.get(new StringBuilder(wordsQuery[i]).reverse().toString());
        }
        return res;
    }

    public static void main(String[] args) {
        int[] ints = new Solution().stringIndices(new String[]{"abcd", "bcd", "xbcd"}, new String[]{"cd", "bcd", "xyz"});
        System.out.println(ints);
    }
}