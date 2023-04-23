package iter.fetch.pipe;

import java.util.Objects;
import java.util.function.Function;

import iter.Fetch;
import iter.Holder;

public final class UniqByFetch<T, K> extends Fetch<T> {
	private final Fetch<T> fetch;
	private final Function<T, K> toKey;
	private Holder<T> current;

	public UniqByFetch(Fetch<T> fetch, Function<T, K> toKey) {
		this.fetch = fetch;
		this.toKey = toKey;
		this.current = Holder.none();
	}

	@Override
	protected final Holder<T> internalNext() {
		if (current.exists()) {
			var key = toKey.apply(current.value());
			
			while (true) {
				var holder = fetch.next();
				
				if (!holder.exists() || !Objects.equals(key, toKey.apply(holder.value()))) {
					current = holder;
					break;
				}
			}
		} else {
			current = fetch.next();
		}
		
		return current;
	}

	@Override
	protected final void internalClose() {
		try (var _fetch = fetch) {
		}
	}

}
