package iter.fetch.pipe;

import iter.Fetch;
import iter.Holder;
import iter.Zip;

public final class ZipFetch<T, U> extends Fetch<Zip<T, U>> {
	private final Fetch<T> aFetch;
	private final Fetch<U> bFetch;

	public ZipFetch(Fetch<T> aFetch, Fetch<U> bFetch) {
		this.aFetch = aFetch;
		this.bFetch = bFetch;
	}

	@Override
	protected final Holder<Zip<T, U>> internalNext() {
		var a = aFetch.next();
		var b = bFetch.next();

		if (a.exists() && b.exists()) {
			return Holder.of(new Zip<T, U>(a.value(), b.value()));
		} else {
			return Holder.none();
		}
	}

	@Override
	protected final void internalClose() {
		try (var _aFetch = aFetch;
				var _bFetch = bFetch) {
		}
	}
}
