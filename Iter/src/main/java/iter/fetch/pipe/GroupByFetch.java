package iter.fetch.pipe;

import java.util.Objects;
import java.util.function.Function;

import iter.Fetch;
import iter.Holder;
import iter.Iter;

public final class GroupByFetch<T, K> extends Fetch<Iter<T>> {
	private final Fetch<T> fetch;
	private final Function<T, K> toKey;
	private Holder<K> key;

	public GroupByFetch(Fetch<T> fetch, Function<T, K> toKey) {
		this.fetch = fetch;
		this.toKey = toKey;
		this.key = null;
	}

	@SuppressWarnings("resource")
	@Override
	protected final Holder<Iter<T>> internalNext() {
		if (key != null) {
			while (valueNext().exists()) {
			}
		}

		if (fetch.peek().exists()) {
			key = Holder.of(toKey.apply(fetch.peek().value()));

			return Holder.of(new Iter<T>(new Fetch<T>() {
				@Override
				public Holder<T> internalNext() {
					return valueNext();
				}
			}));
		} else {
			return Holder.<Iter<T>>none();
		}
	}

	final Holder<T> valueNext() {
		var peek = fetch.peek();

		if (peek.exists() && Objects.equals(key.value(), toKey.apply(peek.value()))) {
			return fetch.next();
		} else {
			key = null;
			return Holder.none();
		}
	}

	@Override
	protected final void internalClose() {
		try (var _fetch = fetch) {
		}
	}
}
