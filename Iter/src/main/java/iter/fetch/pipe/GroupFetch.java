package iter.fetch.pipe;

import iter.Fetch;
import iter.Holder;
import iter.Iter;

public final class GroupFetch<T> extends Fetch<Iter<T>> {
	private final Fetch<T> fetch;
	private final int count;
	private int index;

	public GroupFetch(Fetch<T> fetch, int count) {
		this.fetch = fetch;
		this.count = count;
		this.index = count;
	}

	@SuppressWarnings("resource")
	@Override
	protected final Holder<Iter<T>> internalNext() {
		while (valueNext().exists()) {
		}
		if (fetch.peek().exists()) {
			index = 0;
			return Holder.of(new Iter<T>(new Fetch<T>() {
				@Override
				public Holder<T> internalNext() {
					return valueNext();
				}
			}));
		} else {
			return Holder.none();
		}
	}

	private final Holder<T> valueNext() {
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
