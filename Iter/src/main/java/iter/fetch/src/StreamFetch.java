package iter.fetch.src;

import java.util.Iterator;
import java.util.stream.Stream;

import iter.Fetch;
import iter.Holder;

public final class StreamFetch<T> extends Fetch<T> {
	private final Stream<T> stream;
	private Iterator<T> iterator;

	public StreamFetch(Stream<T> stream) {
		this.stream = stream;
		this.iterator = null;
	}

	@Override
	protected final Holder<T> internalNext() {
		if (iterator == null) {
			iterator = stream.iterator();
		}
		if (iterator.hasNext()) {
			return Holder.of(iterator.next());
		} else {
			return Holder.none();
		}
	}

	@Override
	protected final void internalClose() {
		try (var _stream = stream) {
		}
	}
}
