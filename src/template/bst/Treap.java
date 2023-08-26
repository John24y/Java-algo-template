package template.bst;

import java.util.Random;

/**
 * @Author Create by CROW
 * @Date 2023/7/28
 */
class Treap {
    public static final Node NULL = new Node();
    //好像种子越大取数越慢。如果卡常，可以试试换个种子，改变树结构。
    private static final Random random = new Random(System.currentTimeMillis() % 200);

    static class Node {
        Node left = NULL;
        Node right = NULL;
        int size;
        int key;
        int lazyIncKey;
        int priority;
    }

    Node root = NULL;

    public void incKey(Node node, int x) {
        node.lazyIncKey += x;
        node.key += x;
    }

    private void pushDown(Node node) {
        if (node == NULL) {
            return;
        }
        if (node.lazyIncKey != 0) {
            incKey(node.left, node.lazyIncKey);
            incKey(node.right, node.lazyIncKey);
            node.lazyIncKey = 0;
        }
    }

    private void reduce(Node node) {
        if (node == NULL) {
            return;
        }
        node.size = node.left.size + node.right.size + 1;
    }

    /**
     * split by rank and the node whose rank is argument will stored at result[0]
     */
    public Node[] splitByRank(Node root, int rank) {
        if (root == NULL) {
            return new Node[]{NULL, NULL};
        }
        pushDown(root);
        Node[] result;
        if (root.left.size >= rank) {
            result = splitByRank(root.left, rank);
            root.left = result[1];
            result[1] = root;
        } else {
            result = splitByRank(root.right, rank - (root.size - root.right.size));
            root.right = result[0];
            result[0] = root;
        }
        reduce(root);
        return result;
    }

    public Node merge(Node left, Node right) {
        if (left == NULL) {
            return right;
        }
        if (right == NULL) {
            return left;
        }
        if (left.priority < right.priority) {
            pushDown(left);
            left.right = merge(left.right, right);
            reduce(left);
            return left;
        } else {
            pushDown(right);
            right.left = merge(left, right.left);
            reduce(right);
            return right;
        }
    }

    /**
     * nodes with key <= arguments will stored at result[0]
     */
    public Node[] splitByKey(Node root, int key) {
        if (root == NULL) {
            return new Node[]{NULL, NULL};
        }
        pushDown(root);
        Node[] result;
        if (root.key > key) {
            result = splitByKey(root.left, key);
            root.left = result[1];
            result[1] = root;
        } else {
            result = splitByKey(root.right, key);
            root.right = result[0];
            result[0] = root;
        }
        reduce(root);
        return result;
    }

    public int getKeyByRank(Node root, int k) {
        while (root.size > 1) {
            pushDown(root);
            if (root.left.size >= k) {
                root = root.left;
            } else {
                k -= root.left.size;
                if (k == 1) {
                    break;
                }
                k--;
                root = root.right;
            }
        }
        return root.key;
    }

    public int getRankByKey(Node root, int k) {
        int rank = 0;
        while (root != NULL) {
            if (root.key == k) {
                rank += root.left.size + 1;
                return rank;
            } else if (root.key < k) {
                rank += root.left.size + 1;
                root = root.right;
            } else {
                root = root.left;
            }
        }
        return rank;
    }

    public void insert(int x) {
        Node[] s0 = splitByKey(root, x);
        Node node = new Node();
        node.key = x;
        node.size = 1;
        node.priority = random.nextInt();
        reduce(node);
        root = merge(s0[0], node);
        root = merge(root, s0[1]);
    }

    /**
     * 前k大的数-1，如果不足k个，或者-1之后<0，则不操作并返回false
     */
    public boolean reduceTopK(int k) {
        if (root.size < k) {
            return false;
        }
        Node[] sp = splitByRank(root, root.size - k);
        int minKey = getKeyByRank(sp[1], 1);
        if (minKey <= 0) {
            root = merge(sp[0], sp[1]);
            return false;
        }
        Node[] s1 = splitByKey(sp[0], minKey - 1);
        incKey(sp[1], -1);
        Node[] s2 = splitByKey(sp[1], minKey - 1);
        root = merge(merge(s1[0], s2[0]), merge(s1[1], s2[1]));
        return true;
    }
}
