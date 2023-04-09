import java.util.*;
import java.util.function.Consumer;

/**
 * @Author Create by jiaxiaozheng
 * @Date 2023/2/15
 */
public class A {
    class Solution {
        int[] fa;
        int[] sz;

        int find(int x) {
            if (fa[x]==x){
                return x;
            }
            return fa[x]=find(fa[x]);
        }
        void union(int i, int j) {
            int f1=find(fa[i]);
            int f2=find(fa[j]);
            if (f1==f2)return;
            if (f1>f2){
                int t=f2;
                f2=f1;
                f1=t;
            }
            fa[f2]=f1;
            sz[f1]+=sz[f2];
        }

        public int[] groupStrings(String[] words) {
            int n =words.length;
            fa=new int[n];
            sz=new int[n];
            for (int i = 0; i < n; i++) {
                fa[i]=i;
                sz[i]=1;
            }
            Map<Integer,Integer> add=new HashMap<>(50000);
            Map<Integer,Integer> sub=new HashMap<>(50000);
            for (int i = 0; i < n; i++) {
                int b=0;
                for (int j = 0; j < words[i].length(); j++) {
                    b|=1<<(words[i].charAt(j)-'a');
                }
                for (int j = 0; j < 26; j++) {
                    if ((b&(1<<j))>0) {
                        int k=b-(1<<j);
                        sub.putIfAbsent(k,i);
                        union(i, sub.get(k));
                    } else {
                        int k=b+(1<<j);
                        add.putIfAbsent(k,i);
                        union(i, add.get(k));
                    }
                }
                sub.putIfAbsent(b,i);
                union(i, sub.get(b));
                add.putIfAbsent(b,i);
                union(i, add.get(b));
            }
            int maxg=0;
            int g=0;
            for (int i = 0; i < n; i++) {
                if (fa[i]==i){
                    g+=1;
                    maxg=Math.max(maxg,sz[i]);
                }
            }
            return new int[]{g, maxg};
        }
    }

    public static void main(String[] args) {
        Scanner scanner=new Scanner(System.in);
        String input = scanner.nextLine();
        new A().new Solution().groupStrings(LCUtils.parseStringAr(input));
    }
}

