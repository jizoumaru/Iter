package iter.sort;

import java.util.Comparator;

public final class InsertionSort {
	public static <T> void sort(T[] a, Comparator<T> c) {
		sort(a, 0, a.length, c);
	}
	
	public static final <T> void sort(T[] a, int f, int t, Comparator<T> c) {
		for (var i = f + 1; i < t; i++) {
			if (c.compare(a[i - 1], a[i]) > 0) {
				var j = i;
				var o = a[j];
				do {
					a[j] = a[j - 1];
					j--;
				} while (j > f && c.compare(a[j - 1], o) > 0);
				a[j] = o;
			}
		}
	}

}
