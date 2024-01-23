package template.graph;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/**
 * @Author Create by crow
 * @Date 2023/12/24
 */
public class BFS {

    public static long[] distanceDijkstra(int start, List<List<int[]>> graph) {
        PriorityQueue<int[]> q = new PriorityQueue<>(new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
                return Integer.compare(o1[1], o2[1]);
            }
        });
        int n = graph.size();
        q.add(new int[] {start, 0});
        long[] dis = new long[n];
        dis[start] = 0;
        Arrays.fill(dis, Long.MAX_VALUE);
        while (!q.isEmpty()) {
            int[] poll = q.poll();
            int v = poll[0], cost = poll[1];
            if (cost > dis[v]) {
                continue;
            }
            for (int[] ints : graph.get(v)) {
                int newCost = cost + ints[1];
                if (newCost < dis[ints[0]]) {
                    q.add(new int[] { ints[0], newCost });
                    dis[ints[0]] = newCost;
                }
            }
        }
        return dis;
    }

}
