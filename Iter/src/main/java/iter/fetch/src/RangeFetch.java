package iter.fetch.src;

import iter.Holder;
import iter.Fetch;

public final class RangeFetch extends Fetch<Integer> {
	private final int offset;
	private final int count;
	private int index = 0;

	public RangeFetch(int offset, int count) {
		this.offset = offset;
		this.count = count;
		this.index = 0;
	}

	@Override
	protected final Holder<Integer> internalNext() {
		if (index < count) {
			return Holder.of(offset + (index++));
		} else {
			return Holder.none();
		}
	}
}
