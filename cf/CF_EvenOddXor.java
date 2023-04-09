import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * @Author Create by jiaxiaozheng
 * @Date 2022/9/1
 */
public class CF_EvenOddXor {

    static List<Integer> slv(int n){
        List<Integer> list = new ArrayList<>();
        int last=0;
        if (n%4==1){
            list.addAll(Arrays.asList(2,0,4,5,3));
            last=6;
        }
        if (n%4==2){
            list.addAll(Arrays.asList(4,1,2,12,3,8));
            last=13;
        }
        if (n%4==3){
            list.addAll(Arrays.asList(2,1,3));
            last=4;
        }
        if (last%2==1){
            last++;
        }
        while (list.size()<n){
            list.add(last);
            list.add(last+2);
            list.add(last+1);
            list.add(last+3);
            last+=4;
        }
        return list;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int l = scanner.nextInt();
        for (int i = 0; i < l; i++) {
            int n=scanner.nextInt();
            List<Integer> list = slv(n);
            int odd=list.get(1);
            int even=list.get(0);
            for (int j = 2; j < list.size(); j++) {
                if (j%2==0){
                    even^=list.get(j);
                }else {
                    odd^=list.get(j);
                }
            }
            if (odd!=even){
                System.out.println("ERR");
            }
            System.out.println(list.stream().map(x->x+"").collect(Collectors.joining(" ")));
        }
    }
}
