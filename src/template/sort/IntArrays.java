package template.sort;

class IntArrays {
    private static final int QUICKSORT_NO_REC = 16;
    private static final int QUICKSORT_MEDIAN_OF_9 = 128;

    public static void quickSort(final int[] x, final IntComparator comp) {
        quickSort(x, 0, x.length, comp);
    }

    public static void quickSort(final int[] x, final int from, final int to, final IntComparator comp) {
        final int len = to - from;
        // Selection sort on smallest arrays
        if (len < QUICKSORT_NO_REC) {
            selectionSort(x, from, to, comp);
            return;
        }
        // Choose a partition element, v
        int m = from + len / 2;
        int l = from;
        int n = to - 1;
        if (len > QUICKSORT_MEDIAN_OF_9) { // Big arrays, pseudomedian of 9
            int s = len / 8;
            l = med3(x, l, l + s, l + 2 * s, comp);
            m = med3(x, m - s, m, m + s, comp);
            n = med3(x, n - 2 * s, n - s, n, comp);
        }
        m = med3(x, l, m, n, comp); // Mid-size, med of 3
        final int v = x[m];
        // Establish Invariant: v* (<v)* (>v)* v*
        int a = from, b = a, c = to - 1, d = c;
        while (true) {
            int comparison;
            while (b <= c && (comparison = comp.compare(x[b], v)) <= 0) {
                if (comparison == 0) swap(x, a++, b);
                b++;
            }
            while (c >= b && (comparison = comp.compare(x[c], v)) >= 0) {
                if (comparison == 0) swap(x, c, d--);
                c--;
            }
            if (b > c) break;
            swap(x, b++, c--);
        }
        // Swap partition elements back to middle
        int s;
        s = Math.min(a - from, b - a);
        swap(x, from, b - s, s);
        s = Math.min(d - c, to - d - 1);
        swap(x, b, to - s, s);
        // Recursively sort non-partition-elements
        if ((s = b - a) > 1) quickSort(x, from, from + s, comp);
        if ((s = d - c) > 1) quickSort(x, to - s, to, comp);
    }

    public static void swap(final int x[], final int a, final int b) {
        final int t = x[a];
        x[a] = x[b];
        x[b] = t;
    }

    public static void swap(final int[] x, int a, int b, final int n) {
        for (int i = 0; i < n; i++, a++, b++) swap(x, a, b);
    }

    private static int med3(final int x[], final int a, final int b, final int c, IntComparator comp) {
        final int ab = comp.compare(x[a], x[b]);
        final int ac = comp.compare(x[a], x[c]);
        final int bc = comp.compare(x[b], x[c]);
        return (ab < 0 ? (bc < 0 ? b : ac < 0 ? c : a) : (bc > 0 ? b : ac > 0 ? c : a));
    }
	private static void selectionSort(final int[] a, final int from, final int to, final IntComparator comp) {
		for (int i = from; i < to - 1; i++) {
			int m = i;
			for (int j = i + 1; j < to; j++) if (comp.compare(a[j], a[m]) < 0) m = j;
			if (m != i) {
				final int u = a[i];
				a[i] = a[m];
				a[m] = u;
			}
		}
	}
    @FunctionalInterface
    public interface IntComparator {
        int compare(int k1, int k2);
    }
}
