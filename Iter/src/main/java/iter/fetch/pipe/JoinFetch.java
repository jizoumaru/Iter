package iter.fetch.pipe;

import java.util.Comparator;
import java.util.function.Function;

import iter.Fetch;
import iter.Holder;
import iter.Join;

public final class JoinFetch<L, R, K> extends Fetch<Join<L, R>> {
	private final Fetch<L> leftFetch;
	private final Function<L, K> leftToKey;
	private final Fetch<R> rightFetch;
	private final Function<R, K> rightToKey;
	private final Comparator<K> comparator;

	public JoinFetch(Fetch<L> leftFetch, Fetch<R> rightFetch, Function<L, K> leftToKey, Function<R, K> rightToKey, Comparator<K> comparator) {
		this.leftFetch = leftFetch;
		this.leftToKey = leftToKey;
		this.rightFetch = rightFetch;
		this.rightToKey = rightToKey;
		this.comparator = comparator;
	}

	@Override
	protected final Holder<Join<L, R>> internalNext() {
		var left = leftFetch.peek();
		var right = rightFetch.peek();

		if (left.exists() && right.exists()) {
			var c = comparator.compare(leftToKey.apply(left.value()), rightToKey.apply(right.value()));
			if (c < 0) {
				leftFetch.next();
				return Holder.of(Join.left(left.value()));
			} else if (c > 0) {
				rightFetch.next();
				return Holder.of(Join.right(right.value()));
			} else {
				leftFetch.next();
				rightFetch.next();
				return Holder.of(Join.both(left.value(), right.value()));
			}
		} else if (left.exists()) {
			leftFetch.next();
			return Holder.of(Join.left(left.value()));
		} else if (right.exists()) {
			rightFetch.next();
			return Holder.of(Join.right(right.value()));
		} else {
			return Holder.none();
		}
	}

	@Override
	protected final void internalClose() {
		try (var _leftFetch = leftFetch;
				var _rightFetch = rightFetch) {
		}
	}
}
