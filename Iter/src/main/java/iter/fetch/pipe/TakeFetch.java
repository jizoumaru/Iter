package iter.fetch.pipe;

import iter.Fetch;
import iter.Holder;

public final class TakeFetch<T> extends Fetch<T> {
	private final Fetch<T> fetch;
	private final int count;
	private int index;

	public TakeFetch(Fetch<T> fetch, int count) {
		this.fetch = fetch;
		this.count = count;
		this.index = 0;
	}

	@Override
	protected final Holder<T> internalNext() {
		if (index < count) {
			index++;
			return fetch.next();
		} else {
			return Holder.none();
		}
	}

	@Override
	protected final void internalClose() {
		try (var _fetch = fetch) {
		}
	}

}
