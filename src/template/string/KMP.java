package template.string;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Create by CROW
 * @Date 2023/3/3
 */
class KMP {
    public static int[] table(String p){
        int[] match = new int[p.length()];
        int maxLen = 0;

        for(int i = 1; i < p.length(); i++){
            while(maxLen > 0 && p.charAt(maxLen) != p.charAt(i)){
                maxLen = match[maxLen - 1];
            }
            if(p.charAt(maxLen) == p.charAt(i)){
                maxLen++;
            }
            match[i] = maxLen;
        }
        return match;
    }

    public static List<Integer> findMatch(String t, String p){
        int[] m = table(p);
        List<Integer> res = new ArrayList<>();
        int count = 0;
        for(int i = 0; i < t.length(); i++){
            while(count > 0 && p.charAt(count) != t.charAt(i)){
                count = m[count - 1];
            }
            if(p.charAt(count) == t.charAt(i)){
                count++;
            }
            if(count == p.length()){
                res.add(i - p.length() + 1);
                count = m[count - 1];
            }
        }
        return res;
    }
}
