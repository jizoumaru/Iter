package iter.fetch.tank;

import iter.Fetch;
import iter.Holder;

public class ReverseFetch<T> extends Fetch<T> {
	private final Fetch<T> fetch;
	private T[] array;
	private int iindex;

	public ReverseFetch(Fetch<T> fetch) {
		this.fetch = fetch;
		this.array = null;
		this.iindex = 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Holder<T> internalNext() {
		if (array == null) {
			this.array = (T[]) fetch.toArray();
			this.iindex = array.length;
		}
		if (0 < iindex) {
			return Holder.of(array[--iindex]);
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
