package iter.fetch.pipe;

import java.util.ArrayDeque;

import iter.Fetch;
import iter.Holder;
import iter.ValueLead;

public final class LeadFetch<T> extends Fetch<ValueLead<T>> {
	private final Fetch<T> fetch;
	private final int count;
	private final T defaultValue;
	private final ArrayDeque<T> queue;

	public LeadFetch(Fetch<T> fetch, int offset, T defaultValue) {
		this.fetch = fetch;
		this.count = offset + 1;
		this.defaultValue = defaultValue;
		this.queue = new ArrayDeque<T>(count);
	}

	@Override
	protected final Holder<ValueLead<T>> internalNext() {
		while (queue.size() < count) {
			var holder = fetch.next();
			if (!holder.exists()) {
				break;
			}
			queue.addLast(holder.value());
		}
		if (queue.isEmpty()) {
			return Holder.none();
		} else {
			var lead = queue.size() == count ? queue.getLast() : defaultValue;
			var value = queue.removeFirst();
			return Holder.of(new ValueLead<T>(value, lead));
		}
	}

	@Override
	protected final void internalClose() {
		try (var _fetch = fetch) {
		}
	}
}
