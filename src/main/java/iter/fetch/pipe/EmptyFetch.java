package iter.fetch.pipe;

import iter.Fetch;
import iter.Holder;

public final class EmptyFetch<T> extends Fetch<T> {
	@Override
	protected final Holder<T> internalNext() {
		return Holder.none();
	}
}
