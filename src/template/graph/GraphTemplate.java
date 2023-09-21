package template.graph;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Create by CROW
 * @Date 2023/7/13
 */
public class GraphTemplate {

    static int nextInt() { throw new UnsupportedOperationException(); };

    public List<List<Integer>> readGraph(int nVertex, int mEdge) {
        List<List<Integer>> g=new ArrayList<>();
        for (int i = 0; i <= nVertex; i++) {
            g.add(new ArrayList<>());
        }
        for (int i = 0; i < mEdge; i++) {
            int a=nextInt(),b=nextInt();
            g.get(a).add(b);
            g.get(b).add(a);
        }
        return g;
    }
}
