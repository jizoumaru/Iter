package iter.sort;

import java.util.Comparator;

public final class InsertionMergeSort {
	private static final int MERGE_THRESHOLD = 32;

	public static final <T> void sort(T[] a, Comparator<T> c) {
		if (a.length < MERGE_THRESHOLD) {
			InsertionSort.sort(a, c);
		} else {
			sort(a, a.clone(), 0, a.length, c);
		}
	}

	static final <T> void sort(T[] a, T[] b, int f, int t, Comparator<T> c) {
		if (t - f < MERGE_THRESHOLD) {
			InsertionSort.sort(a, f, t, c);
			return;
		}

		var m = (t - f) / 2 + f;

		sort(b, a, f, m, c);
		sort(a, b, m, t, c);

		for (int i = f, l = f, u = m; l < m; i++) {
			a[i] = (u < t && c.compare(b[l], a[u]) > 0) ? a[u++] : b[l++];
		}
	}

}
