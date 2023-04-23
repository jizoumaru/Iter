package iter.fetch.tank;

import java.util.ArrayDeque;

import iter.Fetch;
import iter.Holder;

public final class TakeLastFetch<T> extends Fetch<T> {
	private final Fetch<T> fetch;
	private final int count;
	private ArrayDeque<Holder<T>> queue;

	public TakeLastFetch(Fetch<T> fetch, int count) {
		this.fetch = fetch;
		this.count = count;
		this.queue = new ArrayDeque<Holder<T>>(count);
	}

	@Override
	protected final Holder<T> internalNext() {
		while (true) {
			var holder = fetch.next();
			
			if (!holder.exists()) {
				break;
			}
			
			if (queue.size() >= count) {
				queue.removeFirst();
			}
			
			queue.addLast(holder);
		}

		if (queue.isEmpty()) {
			return Holder.none();
		} else {
			return queue.removeFirst();
		}
	}

	@Override
	protected final void internalClose() {
		try (var _fetch = fetch) {
		}
	}

}
