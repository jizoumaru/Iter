package iter.fetch.tank;

import java.util.ArrayDeque;

import iter.Fetch;
import iter.Holder;

public final class SkipLastFetch<T> extends Fetch<T> {
	private final Fetch<T> fetch;
	private final int count;
	private final ArrayDeque<Holder<T>> queue;

	public SkipLastFetch(Fetch<T> fetch, int count) {
		this.fetch = fetch;
		this.count = count;
		this.queue = new ArrayDeque<Holder<T>>(count);
	}

	@Override
	protected final Holder<T> internalNext() {
		while (queue.size() < count) {
			var holder = fetch.next();
			if (!holder.exists()) {
				break;
			}
			queue.addLast(holder);
		}
		
		var holder = fetch.next();
		
		if (holder.exists()) {
			var result = queue.removeFirst();
			queue.addLast(holder);
			return result;
		} else {
			return holder;
		}
	}

	@Override
	protected final void internalClose() {
		try (var _fetch = fetch) {
		}
	}
}
