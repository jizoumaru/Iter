package iter.fetch.src;

import iter.Holder;
import iter.Fetch;

public final class RepeatFetch<T> extends Fetch<T> {
	private final T value;
	private final int count;
	private int index;

	public RepeatFetch(T value, int count) {
		this.value = value;
		this.count = count;
		this.index = 0;
	}

	@Override
	protected final Holder<T> internalNext() {
		if (index < count) {
			index++;
			return Holder.of(value);
		} else {
			return Holder.none();
		}
	}
}
