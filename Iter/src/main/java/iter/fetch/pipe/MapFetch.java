package iter.fetch.pipe;

import java.util.function.Function;

import iter.Fetch;
import iter.Holder;

public final class MapFetch<T, U> extends Fetch<U> {
	private final Fetch<T> fetch;
	private final Function<T, U> function;

	public MapFetch(Fetch<T> fetch, Function<T, U> function) {
		this.fetch = fetch;
		this.function = function;
	}

	@Override
	protected final Holder<U> internalNext() {
		var holder = fetch.next();
		
		if (!holder.exists()) {
			return Holder.none();
		}
		
		return Holder.of(function.apply(holder.value()));
	}

	@Override
	protected final void internalClose() {
		try (var _fetch = fetch) {
		}
	}
}
