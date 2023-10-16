package lc.a;

import java.math.BigInteger;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

class Solution {
    public static void main(String[] args) {
//		String line = new Scanner(System.in).nextLine();
//		List<Integer> ar = Arrays.stream(line.split(",")).map(x -> Integer.parseInt(x)).collect(Collectors.toList());
//        int i = new Solution().countSubMultisets(ar,
//				2219
//                ,17215);
//        int i = new Solution().countSubMultisets(Arrays.asList(3,2,12,38,13,36,4,9,15,1,1,2,15,21,31,6,10,0,4,29,11,41,5,5,27,56,5,14,33,28,35,52,17,9,17,4,3,44,27,3,15,79,31,10,19,22,1,8,5,38,46,17,3,2,12,1,45,25,4,48,17,3,6,36,1,8,25,14,15,43,11,70,4,12,10,25,35,23,4,14,19,15,5,51,11,1,30,3,38,37,4,2,47,8,8,5,8,1,5,27,130,12,22,9,67,2,7,75,5,10,8,11,4,2,48,3,9,8,43,1,1,9,13,22,5,14,42,6,0,76,1,3,26,4,5,6,9,9,2,2,2,9,7,9,1,0,26,22,15,17,36,24,8,13,81,1,16,27,12,0,19,14,6,14,29,4,17,25,69,9,11,5,7,4,30,13,3,35,28,6,3,10,28,1,8,56,114,1,21,51,24,14,9,30,22,0,34,3,4,8,16,37,63,1,33,34,3,47,6,6,20,25,15,75,23,17,13,76,4,10,13,3,17,14,5,13,3,116,13,51,10,73,21,2,5,2,1,0,62,29,12,4,19,7,20,3,6,31,28,10,6,0,36,38,38,20,5,2,19,43,38,64,11,5,33,6,31,14,19,48,3,0,8,21,5,31,12,4,14,18,13,12,83,3,11,3,8,6,31,35,3,22,6,13,67,29,10,15,63,9,7,28,34,34,0,17,109,13,8,17,5,29,22,2,2,20,11,0,12,4,7,14,1,10,9,10,6,13,4,32,2,22,8,15,8,4,7,4,23,30,2,18,32,1,4,47,51,7,17,21,82,30,49,16,23,12,13,15,2,67,7,8,8,11,37,9,2,6,13,15,21,5,3,22,6,21,23,3,4,86,3,4,30,14,35,13,7,3,28,9,4,6,49,12,2,8,4,15,3,7,33,23,42,30,23,10,26,53,1,19,78,20,15,13,51,42,24,13,60,8,5,23,12,44,13,62,4,7,7,10,3,8,25,18,13,4,3,0,5,18,18,4,3,55,37,50,44,20,5,7,0,35,19,6,2,10,12,0,18,1,54,11,2,60,20,29,52,2,0,20,11,2,58,4,18,22,39,25,23,27,9,4,14,10,18,19,21,4,9,28,13,6,17,14,7,11,21,27,8,20,5,4,10,6,11,7,11,5,50,20,24,26,39,17,10,11,10,4,7,4,0,22,1,2,37,10,2,22,25,7,19,5,8,11,16,1,34,0,14,11,27,43,4,13,0,34,2,20,71,15,1,8,14,43,33,24,3,0,6,17,16,12,3,32,30,4,8,6,6,4,33,43,38,14,34,33,3,8,10,16,8,23,4,46,9,1,2,1,53,34,1,7,9,33,64,5,7,0,27,21,5,2,21,2,37,6,0,4,25,3,5,10,9,2,56,50,2,13,13,3,33,4,3,73,2,14,14,21,18,17,15,6,16,25,12,45,5,15,11,4,24,2,27,30,3,5,21,10,9,33,30,7,4,11,13,3,8,22,19,25,6,11,10,10,8,18,22,1,14,2,5,26,6,3,42,3,48,15,10,25,16,7,2,22,24,10,1,1,8,24,0,40,53,0,5,18,5,18,0,53,19,2,3,3,12,4,7,24,6,8,33,16,32,14,1,33,9,9,14,3,17,23,17,4,64,3,24,41,4,8,14,20,18,11,37,2,17,20,42,33,53,2,1,14,5,69,10,7,1,35,4,3,10,7,19,3,8,7,11,19,39,3,25,45,20,7,44,7,10,26,14,5,6,8,17,38,42,9,18,19,12,28,0,35,22,1,10,15,11,7,26,1,15,18,13,1,34,12,55,13,34,1,30,28,6,13,13,6,15,6,2,11,28,2,6,36,8,2,15,13,55,18,33,43,34,54,8,40,23,46,27,5,1,5,33,22,5,26,19,10,19,26,5,3,3,2,15,29,1,29,23,29,8,6,1,2,4,7,19,1,79,2,27,31,7,39,35,42,4,1,64,10,15,30,15,12,13,1,13,41,8,6,6,25,15,22,8,22,15,28,27,22,8,9,7,69,3,20,2,9,3,2,10,19,8,6,18,17,43,3,36,32,77,9,27,2,13,38,3,7,9,9,1,27,3,2,21,6,10,8,19,8,5,17,9,58,1,1,19,1,37,7,4,19,39,6,37,20,30,4,2,14,20,33,1,24,11,1,7,7,3,0,9,22,0,0,11,4,2,43,19,2,2,1,48,18,61,2,49,3,16,32,7,39,5,22,7,0,1,21,3,9,4,3,18,3,54,1,4,32,6,62,39,7,7,7,0,0,34,6,1,0,6,5,14,69,71,3,24,4,3,8,6,28,6,4,85,22,82,25,2,25,45,30,41,4,11,30,3,11,0,6,1,21,38,6,10,32,39,29,38,16,18,9,6,6,9,6,0,3,6,13,8,7,19,9,24,23,5,4,3,12,9,13,27,6,68,25,14,25,47,14,68,1,25,48,3,12,5,4,31,36,27,2,30,16,27,7,35,10,24,4,25,38,36,58,0,23,36,1,45,7,22,10,12,13,35,3,2,7,6,19,0,16,15,13,6),
//
//				10556, 13159);
//        System.out.println(i);

//        int score = new Solution().maximumScore(Arrays.asList(8, 3, 9, 3, 8), 2);
//        System.out.println(score);

//        String input = scanner.nextLine();
//        long r = new A().new Solution().getSchemeCount(3, 3, LCUtils.parseStringAr("[\"..R\",\"..B\",\"?R?\"]"));
//        System.out.println(r);
    }
}

