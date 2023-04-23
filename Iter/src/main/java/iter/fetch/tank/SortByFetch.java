package iter.fetch.tank;

import java.util.Comparator;

import iter.Fetch;
import iter.Holder;
import iter.sort.SerialParallelSort;

public class SortByFetch<T> extends Fetch<T> {
	private final Fetch<T> fetch;
	private final Comparator<T> comparator;
	private T[] array;
	private int index;

	public SortByFetch(Fetch<T> fetch, Comparator<T> comparator) {
		this.fetch = fetch;
		this.comparator = comparator;
		this.array = null;
		this.index = 0;
	}

	@Override
	public Holder<T> internalNext() {
		if (array == null) {
			@SuppressWarnings("unchecked")
			var a = (T[]) fetch.toArray();
			SerialParallelSort.sort(a, comparator);
			this.array = a;
			this.index = 0;
		}

		if (index < array.length) {
			return Holder.of(array[index++]);
		} else {
			return Holder.none();
		}
	}

	@Override
	public final void internalClose() {
		try (var _fetch = fetch) {
		}
	}
}
