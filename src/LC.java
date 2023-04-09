import java.util.*;
import java.util.stream.IntStream;

/**
 * @Author Create by jiaxiaozheng
 * @Date 2022/10/15
 */
public class LC {
    public static void main(String[] args) {
        new Solution().collectTheCoins(LCUtils.parseIntAr("[1,0,0,0,0,1]"),
                LCUtils.parseIntIntAr("[[0,1],[1,2],[2,3],[3,4],[4,5]]"));
    }
}

class Solution {
    int[][] cnt;
    //[总coin数,距离1内的coin数,必须移动到的节点数]
    int[] dfs(int i, int p) {
        cnt[i][0]=coins[i];
        cnt[i][1]=coins[i];
        for (Integer ch : g.get(i)) {
            if (ch==p)continue;
            int[] r = dfs(ch, i);
            cnt[i][0]+=r[0];
            cnt[i][2]+=r[2];
            if (coins[ch]==1){
                cnt[i][1]++;
            }
        }
        if (cnt[i][0]!=cnt[i][1]){
            cnt[i][2]++;
        }
        return cnt[i];
    }

    int res;
    void dfs2(int i,int p,int[] pcnt) {
        int r=cnt[i][2]+pcnt[2];
        if (cnt[i][0]!=cnt[i][1])r--;
        res=Math.min(res, r*2);
        for (Integer ch : g.get(i)) {
            if (ch == p) continue;
            int[] npcnt=new int[] {
                    cnt[i][0]-cnt[ch][0]+pcnt[0],
                    cnt[i][1]-coins[ch]+(p==-1?0:coins[p]),
                    cnt[i][2]-cnt[ch][2]+pcnt[2]
            };
            if (cnt[i][0]!=cnt[i][1])npcnt[2]--;//原本cnt[i][2]受目标孩子影响,需要去掉重新算
            if (npcnt[0]!=npcnt[1])npcnt[2]++;
            dfs2(ch,i, npcnt);
        }
    }

    List<List<Integer>> g;
    int[] coins;
    public int collectTheCoins(int[] coins, int[][] edges) {
        g=new ArrayList<>();
        int n=coins.length;
        cnt=new int[n][3];
        this.coins=coins;
        res=n*3;
        for (int i = 0; i < n; i++) {
            g.add(new ArrayList<>());
        }
        for (int[] edge : edges) {
            g.get(edge[0]).add(edge[1]);
            g.get(edge[1]).add(edge[0]);
        }
        dfs(0,-1);
        dfs2(0, -1, new int[3]);
        return res;
    }
}
