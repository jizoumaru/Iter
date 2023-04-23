package iter;

import java.util.Comparator;
import java.util.function.Function;

import iter.fetch.tank.SortByFetch;

public final class SortIter<T> extends Iter<T> {
	private final Fetch<T> iter;
	private final Comparator<T> cmp;

	public SortIter(Fetch<T> iter, Comparator<T> cmp) {
		super(new SortByFetch<T>(iter, cmp));
		this.iter = iter;
		this.cmp = cmp;
	}

	public final <K extends Comparable<K>> SortIter<T> thenBy(Function<T, K> toKey) {
		return new SortIter<T>(iter, cmp.thenComparing(toKey));
	}

	public final <K extends Comparable<K>> SortIter<T> thenByDesc(Function<T, K> toKey) {
		return new SortIter<T>(iter, cmp.thenComparing(toKey, Comparator.reverseOrder()));
	}

}
