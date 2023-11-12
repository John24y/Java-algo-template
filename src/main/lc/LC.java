package main.lc;

import java.util.Arrays;

class RecurBitTrie {
    public static final int MAX_BIT = 31;

    static class Node {
        Node[] child = new Node[2];
        int prefix = 0;
        int count = 0;
        int max = 0;
        int min = Integer.MAX_VALUE;
    }

    Node root;

    public RecurBitTrie() {
        root = new Node();
    }

    public void add(int val, int cnt) {
        add(root, MAX_BIT - 1, val, cnt);
    }

    private void add(Node node, int i, int val, int cnt) {
        if (i < 0) {
            //node是不包含第i位的前缀
            node.max = node.min = val;
            node.count += cnt;
            return;
        }
        int bit = (val >> i) & 1;
        if (node.child[bit] == null) {
            node.child[bit] = new Node();
            node.child[bit].prefix = node.prefix | (bit << i);
        }
        node.count += cnt;
        add(node.child[bit], i - 1, val, cnt);
        node.max = 0;
        node.min = Integer.MAX_VALUE;
        if (node.child[0] != null && node.child[0].count > 0) {
            node.max = Math.max(node.max, node.child[0].max);
            node.min = Math.min(node.min, node.child[0].min);
        }
        if (node.child[1] != null && node.child[1].count > 0) {
            node.max = Math.max(node.max, node.child[1].max);
            node.min = Math.min(node.min, node.child[1].min);
        }
    }

    // 求 num^x 最大的x，x是在树中存在的数字
    public int maxXor(int num, int lowerBound) {
        Node node = root;
        int ans = 0;
        for (int i = MAX_BIT - 1; i >= 0; i--) {
            int bit = (num >> i) & 1;
            if (node.child[1 - bit] != null && node.child[1 - bit].count > 0
                    && node.child[1 - bit].max >= lowerBound) {
                bit = 1 - bit;
                ans |= 1 << i;
            } else if (node.child[bit] != null && node.child[bit].count > 0
                    && node.child[bit].max >= lowerBound) {
            } else {
                return -1;
            }
            node = node.child[bit];
        }
        return ans;
    }

    public int minXor(int num, int upperBound) {
        Node node = root;
        int ans = 0;
        for (int i = MAX_BIT - 1; i >= 0; i--) {
            int bit = (num >> i) & 1;
            if (node.child[bit] != null && node.child[bit].count > 0
                    && node.child[bit].min <= upperBound) {
            } else if (node.child[1 - bit] != null && node.child[1 - bit].count > 0
                    && node.child[1 - bit].min <= upperBound) {
                bit = 1 - bit;
                ans |= 1 << i;
            } else {
                return -1;
            }
            node = node.child[bit];
        }
        return ans;
    }
}

class Solution {
    public int maximumStrongPairXor(int[] nums) {
        int n = nums.length;
        Arrays.sort(nums);
        int res = 0;
        RecurBitTrie trie = new RecurBitTrie();
        for (int i = 0; i < n; i++) {
            int t = (nums[i] >> 1) + (nums[i] & 1);
            res = Math.max(res, trie.maxXor(nums[i], t));
            trie.add(nums[i], 1);
        }
        return res;
    }

    public static void main(String[] args) {
        int i = new Solution().maximumStrongPairXor(new int[]{500, 520});
        System.out.println(i);
    }
}
