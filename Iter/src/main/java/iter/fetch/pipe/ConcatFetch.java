package iter.fetch.pipe;

import iter.Fetch;
import iter.Holder;

public final class ConcatFetch<T> extends Fetch<T> {
	private final Fetch<T> first;
	private final Fetch<T> second;
	private boolean isFirst;
	
	public ConcatFetch(Fetch<T> first, Fetch<T> second) {
		this.first = first;
		this.second = second;
		this.isFirst = true;
	}

	@Override
	protected final Holder<T> internalNext() {
		if (isFirst) {
			var holder = first.next();
			
			if (holder.exists()) {
				return holder;
			}
			
			try (var _first = first) {
			}
			
			isFirst = false;
		}
		
		return second.next();
	}

	@Override
	protected final void internalClose() {
		try (var _second = second;
				var _first = first) {
		}
	}
}
