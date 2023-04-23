package iter.fetch.pipe;

import java.util.function.Predicate;

import iter.Fetch;
import iter.Holder;

public final class FilterFetch<T> extends Fetch<T> {
	private final Fetch<T> fetch;
	private final Predicate<T> predicate;

	public FilterFetch(Fetch<T> fetch, Predicate<T> predicate) {
		this.fetch = fetch;
		this.predicate = predicate;
	}

	@Override
	protected final Holder<T> internalNext() {
		while (true) {
			var holder = fetch.next();
			
			if (!holder.exists() || predicate.test(holder.value())) {
				return holder;
			}
		}
	}

	@Override
	protected final void internalClose() {
		try (var _fetch = fetch) {
		}
	}

}
