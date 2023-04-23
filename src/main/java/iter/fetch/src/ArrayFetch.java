package iter.fetch.src;

import iter.Fetch;
import iter.Holder;

public final class ArrayFetch<T> extends Fetch<T> {
	private final T[] array;
	private int index;

	@SafeVarargs
	public ArrayFetch(T... array) {
		this.array = array;
		this.index = 0;
	}

	@Override
	protected final Holder<T> internalNext() {
		if (index < array.length) {
			return Holder.of(array[index++]);
		} else {
			return Holder.none();
		}
	}
}
