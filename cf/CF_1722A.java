
import java.util.*;

/**
 * @Author Create by jiaxiaozheng
 * @Date 2022/9/1
 */
public class CF_1722A {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();

        for (int i = 0; i < n; i++) {
            int m=scanner.nextInt();
            Map<String,Integer> map=new HashMap<>();
            List<List<String>> list=new ArrayList<>();
            for (int j = 0; j < 3; j++) {
                list.add(new ArrayList<>());
                for (int k = 0; k < m; k++) {
                    String s=scanner.next();
                    list.get(j).add(s);
                    map.put(s,map.getOrDefault(s,0)+1);
                }
            }
            int[] res=new int[3];
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < m; k++) {
                    int r=map.get(list.get(j).get(k));
                    if (r==1){
                        res[j]+=3;
                    } else if (r==2) {
                        res[j]+=1;
                    }
                }
            }
            System.out.print(res[0]);
            System.out.print(res[1]);
            System.out.print(res[2]);
            System.out.print("\n");
        }
    }
}
