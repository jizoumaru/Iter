package iter.fetch.src;

import java.util.Iterator;

import iter.Fetch;
import iter.Holder;

public final class IteratorFetch<T> extends Fetch<T> {
	private final Iterator<T> iterator;

	public IteratorFetch(Iterator<T> iterator) {
		this.iterator = iterator;
	}

	@Override
	protected final Holder<T> internalNext() {
		if (iterator.hasNext()) {
			return Holder.of(iterator.next());
		} else {
			return Holder.none();
		}
	}
}
