package iter.fetch.pipe;

import java.util.ArrayDeque;

import iter.Fetch;
import iter.Holder;
import iter.ValueLag;

public final class LagFetch<T> extends Fetch<ValueLag<T>> {
	private final Fetch<T> fetch;
	private final int count;
	private final ArrayDeque<T> queue;
	private final T defaultValue;

	public LagFetch(Fetch<T> fetch, int offset, T defaultValue) {
		this.fetch = fetch;
		this.count = offset + 1;
		this.queue = new ArrayDeque<T>(count);
		this.defaultValue = defaultValue;
	}

	@Override
	protected final Holder<ValueLag<T>> internalNext() {
		var holder = fetch.next();
		
		if (holder.exists()) {
			queue.addLast(holder.value());
			var value = queue.getLast();
			var lag = queue.size() == count ? queue.removeFirst() : defaultValue;
			return Holder.of(new ValueLag<T>(value, lag));
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
