

import java.util.ArrayDeque;


class SideFirstValue {

    enum Relation {
        LESS,
        LESS_EQUALS,
        GREATER,
        GREATER_EQUALS,
    }

    //返回数组r,r[i]表示i左侧第一个满足relation的下标，如果不存在则为-1
    public int[] leftSide(long[] ar, Relation relation) {
        int n = ar.length;
        int[] res = new int[n];
        ArrayDeque<Integer> stack = new ArrayDeque<>(n + 1);
        for (int i = 0; i < ar.length; i++) {
            while (!stack.isEmpty() && !satisfy(relation, ar[stack.getLast()], ar[i])) {
                stack.removeLast();
            }
            res[i] = stack.isEmpty() ? -1 : stack.getLast();
            stack.add(i);
        }
        return res;
    }

    //返回数组r,r[i]表示i右侧第一个满足relation的下标，如果不存在则为ar.length
    public int[] rightSide(long[] ar, Relation relation) {
        int n = ar.length;
        int[] res = new int[n];
        ArrayDeque<Integer> stack = new ArrayDeque<>(n + 1);
        for (int i = ar.length - 1; i >= 0; i--) {
            while (!stack.isEmpty() && !satisfy(relation, ar[stack.getLast()], ar[i])) {
                stack.removeLast();
            }
            res[i] = stack.isEmpty() ? n : stack.getLast();
            stack.add(i);
        }
        return res;
    }

    //返回数组r, r[0]是leftSide的返回值，r[1]是rightSide的返回值
    public int[][] bothSide(long[] ar, Relation left, Relation right) {
        if (!(left.name().startsWith(right.name()) || right.name().startsWith(left.name())) ||
                (left == right && (left == Relation.LESS || left == Relation.GREATER))) {
            //如果两侧都是是严格的大于或者小于，无法用一次遍历求出，一侧小于一侧大于也不行
            //左侧小于右侧小于等于、左侧大于右侧大于等于...是可以的
            throw new IllegalArgumentException();
        }

        int n = ar.length;
        int[] res0 = new int[n];
        int[] res1 = new int[n];
        ArrayDeque<Integer> stack = new ArrayDeque<>(n + 1);
        if (left == Relation.LESS_EQUALS && right == Relation.LESS ||
                left == Relation.GREATER_EQUALS && right == Relation.GREATER) {
            for (int i = ar.length - 1; i >= 0; i--) {
                res0[i] = -1;
                while (!stack.isEmpty() && !satisfy(right, ar[stack.getLast()], ar[i])) {
                    int last = stack.removeLast();
                    if (res0[last] == -1 && satisfy(left, ar[i], ar[last])) {
                        res0[last] = i;
                    }
                }
                if (!stack.isEmpty() && satisfy(left, ar[i], ar[stack.getLast()])) {
                    res0[stack.getLast()] = i;
                }
                res1[i] = stack.isEmpty() ? n : stack.getLast();
                stack.add(i);
            }
        } else {
            for (int i = 0; i < ar.length; i++) {
                res1[i] = n;
                while (!stack.isEmpty() && !satisfy(left, ar[stack.getLast()], ar[i])) {
                    int last = stack.removeLast();
                    if (res1[last] == n && satisfy(right, ar[i], ar[last])) {
                        res1[last] = i;
                    }
                }
                if (!stack.isEmpty() && satisfy(right, ar[i], ar[stack.getLast()])) {
                    res1[stack.getLast()] = i;
                }
                res0[i] = stack.isEmpty() ? -1 : stack.getLast();
                stack.add(i);
            }
        }
        return new int[][]{res0, res1};
    }

    private boolean satisfy(Relation relation, long v1, long v2) {
        switch (relation) {
            case LESS:
                return v1 < v2;
            case LESS_EQUALS:
                return v1 <= v2;
            case GREATER:
                return v1 > v2;
            case GREATER_EQUALS:
                return v1 >= v2;
        }
        throw new RuntimeException();
    }
}