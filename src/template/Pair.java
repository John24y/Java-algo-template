package template;

public class Pair implements Comparable<Pair> {
    int k;
    int v;

    public Pair(int k, int v) {
        this.v = v;
        this.k = k;
    }

    @Override
    public int compareTo(Pair o) {
        int compare = Integer.compare(this.k, o.k);
        if (compare == 0) {
            return Integer.compare(this.v, o.v);
        }
        return compare;
    }
}
