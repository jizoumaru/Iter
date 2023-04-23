package iter.fetch.pipe;

import java.util.Comparator;

import iter.Fetch;
import iter.Holder;

public final class MergeFetch<T> extends Fetch<T> {
	private final Fetch<T> leftFetch;
	private final Fetch<T> rightFetch;
	private final Comparator<T> comparator;

	public MergeFetch(Fetch<T> leftFetch, Fetch<T> rightFetch, Comparator<T> comparator) {
		this.leftFetch = leftFetch;
		this.rightFetch = rightFetch;
		this.comparator = comparator;
	}

	@Override
	protected final Holder<T> internalNext() {
		var left = leftFetch.peek();
		var right = rightFetch.peek();
		
		if (left.exists() && right.exists()) {
			if (comparator.compare(left.value(), right.value()) <= 0) {
				return leftFetch.next();
			} else {
				return rightFetch.next();
			}
		} else if (left.exists()) {
			return leftFetch.next();
		} else if (right.exists()) {
			return rightFetch.next();
		} else {
			return left;
		}
	}

	@Override
	protected final void internalClose() {
		try (var _left = leftFetch;
				var _right = rightFetch) {
		}
	}
}
