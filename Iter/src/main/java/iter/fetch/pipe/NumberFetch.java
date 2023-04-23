package iter.fetch.pipe;

import iter.Fetch;
import iter.Holder;
import iter.ValueNumber;

public final class NumberFetch<T> extends Fetch<ValueNumber<T>> {
	private final Fetch<T> fetch;
	private int number;

	public NumberFetch(Fetch<T> fetch) {
		this.fetch = fetch;
		this.number = 0;
	}

	@Override
	protected final Holder<ValueNumber<T>> internalNext() {
		var holder = fetch.next();
		if (holder.exists()) {
			number++;
			return Holder.of(new ValueNumber<T>(holder.value(), number));
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
