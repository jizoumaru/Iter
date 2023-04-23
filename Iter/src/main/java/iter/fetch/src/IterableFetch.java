package iter.fetch.src;

import java.util.Iterator;

import iter.Fetch;
import iter.Holder;

public final class IterableFetch<T> extends Fetch<T> {
	private final Iterable<T> src;
	private Iterator<T> iterator;

	public IterableFetch(Iterable<T> src) {
		this.src = src;
		this.iterator = null;
	}

	@Override
	protected final Holder<T> internalNext() {
		if (iterator == null) {
			iterator = src.iterator();
		}
		if (iterator.hasNext()) {
			return Holder.of(iterator.next());
		} else {
			return Holder.none();
		}
	}
}
