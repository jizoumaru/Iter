package iter.sort;

import java.util.Comparator;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public final class SerialParallelSort<T> extends RecursiveAction {
	private static final int PARALLEL_THRESHOLD = 900;
	
	public static final <T> void sort(T[] a, Comparator<T> c) {
		if (a.length < PARALLEL_THRESHOLD) {
			InsertionMergeSort.sort(a, c);
		} else {
			var task = new SerialParallelSort<T>(a, a.clone(), 0, a.length, c);
			var pool = ForkJoinPool.commonPool();
			var future = pool.submit(task);
			future.join();
		}
	}

	private static final long serialVersionUID = 1L;
	private final T[] a;
	private final T[] b;
	private final int f;
	private final int t;
	private final Comparator<T> c;

	public SerialParallelSort(T[] a, T[] b, int f, int t, Comparator<T> c) {
		this.a = a;
		this.b = b;
		this.f = f;
		this.t = t;
		this.c = c;
	}

	@Override
	protected final void compute() {
		if (t - f < PARALLEL_THRESHOLD) {
			InsertionMergeSort.sort(a, b, f, t, c);
			return;
		}

		var m = (t - f) / 2 + f;

		invokeAll(
				new SerialParallelSort<T>(b, a, f, m, c),
				new SerialParallelSort<T>(a, b, m, t, c));

		for (int i = f, l = f, u = m; l < m; i++) {
			a[i] = (u < t && c.compare(b[l], a[u]) > 0) ? a[u++] : b[l++];
		}
	}

}
