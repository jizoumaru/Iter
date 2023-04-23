package iter.fetch.pipe;

import java.util.function.Predicate;

import iter.Fetch;
import iter.Holder;

public final class TakeWhileFetch<T> extends Fetch<T> {
	private final Fetch<T> fetch;
	private final Predicate<T> predicate;
	private boolean terminated;

	public TakeWhileFetch(Fetch<T> fetch, Predicate<T> predicate) {
		this.fetch = fetch;
		this.predicate = predicate;
		this.terminated = false;
	}

	@Override
	protected final Holder<T> internalNext() {
		if (terminated) {
			return Holder.none();
		}

		var holder = fetch.next();

		if (!holder.exists()) {
			return holder;
		}

		if (!predicate.test(holder.value())) {
			terminated = true;
			return Holder.none();
		}

		return holder;
	}

	@Override
	protected final void internalClose() {
		try (var _fetch = fetch) {
		}
	}

}
