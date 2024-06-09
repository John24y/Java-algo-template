package main.lc;

import java.lang.reflect.Field;
import java.util.*;

class BitSets {

    /**
     * 超过max的位丢弃
     */
    public static BitSet shiftLeft(BitSet bits, int n, int max) {
        if (n == 0) {
            return (BitSet) bits.clone();
        }

        max = Math.min(max, bits.size() + n);

        int length = 1 + (max / 64);

        long [] res = new long [length];
        int start = n / 64;
        n %= 64;
        long[] raw = bits.toLongArray();
        long carry = 0;

        for (int j = start, i = 0; j < res.length; ++j, ++i) {
            long val = i >= raw.length ? 0 : raw[i];

            if (n != 0) {
                long newcarry = val >>> (64 - n);
                res[j] = (val << n) | carry;
                carry = newcarry;
            } else {
                res[j] = val;
            }

            if (carry == 0 && i >= raw.length) {
                break;
            }
        }

        return BitSet.valueOf(res);
    }

}
class Solution {
    public int maxTotalReward(int[] rewardValues) {
        Arrays.sort(rewardValues);

        BitSet current = new BitSet();

        current.set(0);

        BitSet clone, shifted;

        int max = 0;

        for (int num : rewardValues)
            max = Math.max(max, num);

        for (int num : rewardValues) {
            clone = (BitSet)current.clone();
            int size = clone.size();

            if (size > num)
                clone.clear(num, size);

            shifted = BitSets.shiftLeft(clone, num, 2 * max);
            current.or(shifted);
        }

        // System.out.println(current);
        return current.previousSetBit(100_000);
    }

}