package iter.fetch.tank;

import java.util.function.IntUnaryOperator;

import iter.Fetch;
import iter.Holder;

public class ShuffleFetch<T> extends Fetch<T> {
	private final Fetch<T> fetch;
	private final IntUnaryOperator random;
	private T[] array;
	private int index;

	public ShuffleFetch(Fetch<T> fetch, IntUnaryOperator random) {
		this.fetch = fetch;
		this.random = random;
		this.array = null;
		this.index = 0;
	}

	@Override
	public Holder<T> internalNext() {
		if (array == null) {
			@SuppressWarnings("unchecked")
			var a = (T[]) fetch.toArray();
			for (var i = a.length; i > 1; i--) {
				var j = random.applyAsInt(i);
				var t = a[i - 1];
				a[i - 1] = a[j];
				a[j] = t;
			}
			this.array = a;
			this.index = 0;
		}

		if (index < array.length) {
			return Holder.of(array[index++]);
		} else {
			return Holder.none();
		}
	}

	@Override
	public final void internalClose() {
		try (var _fetch = fetch) {
		}
	}
}
