package template;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * 单调栈找左侧或右侧第一个满足关系的下标。
 * 以下所有实现都使栈中可以存在相等的元素。
 */
public class MonoStack {

    /************************ 大于 ************************/

    /**
     * left[i]是左边第一个>=nums[i]的下标
     * right[i]是右边第一个>nums[i]的下标
     */
    public static int[][] leftGeRightGt(int[] nums) {
        int n = nums.length;
        int[] left = new int[n];
        int[] right = new int[n];
        LinkedList<Integer> stack = new LinkedList<>();
        Arrays.fill(left, -1);
        Arrays.fill(right, n);
        for (int i = 0; i < n; i++) {
            while (!stack.isEmpty() && nums[stack.peek()] < nums[i]) {
                right[stack.pop()] = i;
            }
            if (!stack.isEmpty()) {
                left[i] = stack.peek();
            }
            stack.push(i);
        }
        return new int[][] { left, right };
    }

    /**
     * left[i]是左边第一个>nums[i]的下标
     * right[i]是右边第一个>=nums[i]的下标
     */
    public static int[][] leftGtRightGe(int[] nums) {
        int n = nums.length;
        int[] left = new int[n];
        int[] right = new int[n];
        LinkedList<Integer> stack = new LinkedList<>();
        Arrays.fill(left, -1);
        Arrays.fill(right, n);
        for (int i = n - 1; i >= 0; i--) {
            while (!stack.isEmpty() && nums[stack.peek()] < nums[i]) {
                left[stack.pop()] = i;
            }
            if (!stack.isEmpty()) {
                right[i] = stack.peek();
            }
            stack.push(i);
        }
        return new int[][] { left, right };
    }

    /**
     * left[i]是左边第一个>=nums[i]的下标
     * right[i]是右边第一个>=nums[i]的下标
     */
    public static int[][] leftGeRightGe(int[] nums) {
        int n = nums.length;
        int[] left = new int[n];
        int[] right = new int[n];
        LinkedList<Integer> stack = new LinkedList<>();
        Arrays.fill(left, -1);
        Arrays.fill(right, n);
        for (int i = 0; i < n; i++) {
            while (!stack.isEmpty() && nums[stack.peek()] < nums[i]) {
                int j = stack.pop();
                right[j] = Math.min(right[j], i);
            }
            if (!stack.isEmpty()) {
                left[i] = stack.peek();
                if (nums[stack.peek()] == nums[i]) {
                    right[stack.peek()] = i;
                }
            }
            stack.push(i);
        }
        return new int[][] { left, right };
    }

    /************************ 小于 ************************/

    /**
     * left[i]是左边第一个<=nums[i]的下标
     * right[i]是右边第一个<nums[i]的下标
     */
    public static int[][] leftLeRightLt(int[] nums) {
        int n = nums.length;
        int[] left = new int[n];
        int[] right = new int[n];
        LinkedList<Integer> stack = new LinkedList<>();
        Arrays.fill(left, -1);
        Arrays.fill(right, n);
        for (int i = 0; i < n; i++) {
            while (!stack.isEmpty() && nums[stack.peek()] > nums[i]) {
                right[stack.pop()] = i;
            }
            if (!stack.isEmpty()) {
                left[i] = stack.peek();
            }
            stack.push(i);
        }
        return new int[][] { left, right };
    }

    /**
     * left[i]是左边第一个<nums[i]的下标
     * right[i]是右边第一个<=nums[i]的下标
     */
    public static int[][] leftLtRightLe(int[] nums) {
        int n = nums.length;
        int[] left = new int[n];
        int[] right = new int[n];
        LinkedList<Integer> stack = new LinkedList<>();
        Arrays.fill(left, -1);
        Arrays.fill(right, n);
        for (int i = n - 1; i >= 0; i--) {
            while (!stack.isEmpty() && nums[stack.peek()] > nums[i]) {
                left[stack.pop()] = i;
            }
            if (!stack.isEmpty()) {
                right[i] = stack.peek();
            }
            stack.push(i);
        }
        return new int[][] { left, right };
    }

    /**
     * left[i]是左边第一个<=nums[i]的下标
     * right[i]是右边第一个<=nums[i]的下标
     */
    public static int[][] leftLeRightLe(int[] nums) {
        int n = nums.length;
        int[] left = new int[n];
        int[] right = new int[n];
        LinkedList<Integer> stack = new LinkedList<>();
        Arrays.fill(left, -1);
        Arrays.fill(right, n);
        for (int i = 0; i < n; i++) {
            while (!stack.isEmpty() && nums[stack.peek()] > nums[i]) {
                int j = stack.pop();
                right[j] = Math.min(right[j], i);
            }
            if (!stack.isEmpty()) {
                left[i] = stack.peek();
                if (nums[stack.peek()] == nums[i]) {
                    right[stack.peek()] = i;
                }
            }
            stack.push(i);
        }
        return new int[][] { left, right };
    }

}
