package template.graph;

/**
 * 计算基环树（所有点的出度都为1）上从x跳y步之后的位置。倍增算法。
 */
class RingTreeJump {
    int[][] table;
    static final int MX_BIT = 30;

    // i指向graph[i]
    public RingTreeJump(int graph[]) {
        int n=graph.length;
        table=new int[MX_BIT+1][n];
        for (int i = 0; i < n; i++) {
            table[0][i]=i;
            table[1][i]=graph[i];
        }
        for (int k = 2; k < MX_BIT; k++) {
            for (int i = 0; i < n; i++) {
                table[k][i]=table[k-1][table[k-1][i]];
            }
        }
    }

    // 从start跳step步
    public int jump(int start, int step) {
        if (start>=table[0].length) throw new IllegalArgumentException();
        for (int j = 0; j < MX_BIT - 1; j++) {
            if (((1<<j)&step)>0){
                start=table[j+1][start];
            }
        }
        return start;
    }
}
