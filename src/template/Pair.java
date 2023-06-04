package template;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair pair = (Pair) o;
        return k == pair.k && v == pair.v;
    }

    @Override
    public int hashCode() {
        return Objects.hash(k, v);
    }
}
