package iter.fetch.tank;

import iter.Fetch;
import iter.Holder;
import iter.ValueNth;

public final class NthFetch<T> extends Fetch<ValueNth<T>> {
	private final Fetch<T> fetch;
	private final int n;
	private T[] array;
	private int index;
	private T nthValue;

	public NthFetch(Fetch<T> fetch, int n) {
		this.fetch = fetch;
		this.n = n;
	}

	@Override
	@SuppressWarnings("unchecked")
	public final Holder<ValueNth<T>> internalNext() {
		if (array == null) {
			this.array = (T[]) fetch.toArray();
			this.index = 0;
			this.nthValue = n - 1 < array.length ? array[n - 1] : null;
		}

		if (index < array.length) {
			return Holder.of(new ValueNth<T>(array[index++], nthValue));
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
