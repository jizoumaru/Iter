package iter.fetch.pipe;

import java.util.function.Function;

import iter.Holder;
import iter.Iter;
import iter.Fetch;

public final class FlatMapFetch<T, U> extends Fetch<U> {
	private final Fetch<T> src;
	private final Function<T, Iter<U>> function;
	private Fetch<U> fetch;

	public FlatMapFetch(Fetch<T> src, Function<T, Iter<U>> function) {
		this.src = src;
		this.function = function;
		this.fetch = new Fetch<U>() {
			@Override
			public Holder<U> internalNext() {
				return Holder.none();
			}
		};
	}

	@Override
	protected final Holder<U> internalNext() {
		while (true) {
			var holder = fetch.next();

			if (holder.exists()) {
				return holder;
			}

			try (var _inner = fetch) {
			}

			var srcHolder = src.next();

			if (!srcHolder.exists()) {
				return holder;
			}

			fetch = function.apply(srcHolder.value()).fetch();
		}
	}

	@Override
	protected final void internalClose() {
		try (var _src = src;
				var _fetch = fetch) {
		}
	}
}
