package iter.fetch.pipe;

import java.util.function.Predicate;

import iter.Fetch;
import iter.Holder;

public final class SkipWhileFetch<T> extends Fetch<T> {
	private final Fetch<T> fetch;
	private final Predicate<T> predicate;
	private boolean skipped;

	public SkipWhileFetch(Fetch<T> fetch, Predicate<T> predicate) {
		this.fetch = fetch;
		this.predicate = predicate;
		this.skipped = false;
	}

	@Override
	protected final Holder<T> internalNext() {
		if (!skipped) {
			skipped = true;

			while (true) {
				var holder = fetch.next();
			
				if (!holder.exists() || !predicate.test(holder.value())) {
					return holder;
				}
			}
		} else {
			return fetch.next();
		}
	}

	@Override
	protected final void internalClose() {
		try (var _fetch = fetch) {
		}
	}

}
