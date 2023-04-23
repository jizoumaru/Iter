package iter.fetch.pipe;

import iter.Fetch;
import iter.Holder;

public final class SkipFetch<T> extends Fetch<T> {
	private final Fetch<T> fetch;
	private final int count;
	private int index;

	public SkipFetch(Fetch<T> fetch, int count) {
		this.fetch = fetch;
		this.count = count;
		this.index = 0;
	}

	@Override
	protected final Holder<T> internalNext() {
		while (index < count) {
			index++;
			var holder = fetch.next();
			
			if (!holder.exists()) {
				return holder;
			}
		}
		
		return fetch.next();
	}

	@Override
	protected final void internalClose() {
		try (var _fetch = fetch) {
		}
	}

}
