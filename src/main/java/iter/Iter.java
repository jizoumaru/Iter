package iter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.SplittableRandom;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.IntUnaryOperator;
import java.util.function.Predicate;
import java.util.function.ToLongFunction;
import java.util.stream.Stream;

import iter.fetch.pipe.ConcatFetch;
import iter.fetch.pipe.EmptyFetch;
import iter.fetch.pipe.FilterFetch;
import iter.fetch.pipe.FlatMapFetch;
import iter.fetch.pipe.GroupByFetch;
import iter.fetch.pipe.GroupFetch;
import iter.fetch.pipe.JoinFetch;
import iter.fetch.pipe.LagFetch;
import iter.fetch.pipe.LeadFetch;
import iter.fetch.pipe.MapFetch;
import iter.fetch.pipe.MergeFetch;
import iter.fetch.pipe.NumberFetch;
import iter.fetch.pipe.SkipFetch;
import iter.fetch.pipe.SkipWhileFetch;
import iter.fetch.pipe.TakeFetch;
import iter.fetch.pipe.TakeWhileFetch;
import iter.fetch.pipe.UniqByFetch;
import iter.fetch.pipe.ZipFetch;
import iter.fetch.src.ArrayFetch;
import iter.fetch.src.IterableFetch;
import iter.fetch.src.IteratorFetch;
import iter.fetch.src.RangeFetch;
import iter.fetch.src.RepeatFetch;
import iter.fetch.src.StreamFetch;
import iter.fetch.tank.NthFetch;
import iter.fetch.tank.NtileFetch;
import iter.fetch.tank.ReverseFetch;
import iter.fetch.tank.ShuffleFetch;
import iter.fetch.tank.SkipLastFetch;
import iter.fetch.tank.TakeLastFetch;

public class Iter<T> implements Iterator<T>, AutoCloseable {
	public static final <T, K extends Comparable<K>> Iter<T> merge(ArrayList<? extends Iter<T>> iters, Function<T, K> toKey) {
		return new Iter<T>(merge(iters, 0, iters.size(), Comparator.comparing(toKey)));
	}

	static <T> Fetch<T> merge(ArrayList<? extends Iter<T>> a, int f, int t, Comparator<T> cmp) {
		if (t - f == 0) {
			return new EmptyFetch<T>();
		}
		if (t - f == 1) {
			return a.get(f).fetch();
		}
		var m = (t - f) / 2 + f;
		return new MergeFetch<T>(merge(a, f, m, cmp), merge(a, m, t, cmp), cmp);
	}

	public static final <T> Iter<T> empty() {
		return new Iter<T>(new EmptyFetch<T>());
	}

	public static final Iter<Integer> range(int offset, int count) {
		return new Iter<Integer>(new RangeFetch(offset, count));
	}

	public static final <T> Iter<T> repeat(T value, int count) {
		return new Iter<T>(new RepeatFetch<T>(value, count));
	}

	public static final <T> Iter<T> from(Stream<T> stream) {
		return new Iter<T>(new StreamFetch<T>(stream));
	}

	public static final <T> Iter<T> from(Iterable<T> iterable) {
		return new Iter<T>(new IterableFetch<T>(iterable));
	}

	public static final <T> Iter<T> from(Iterator<T> iterator) {
		return new Iter<T>(new IteratorFetch<T>(iterator));
	}

	@SafeVarargs
	public static final <T> Iter<T> of(T... a) {
		return new Iter<T>(new ArrayFetch<T>(a));
	}

	private final Fetch<T> fetch;

	public Iter(Fetch<T> iter) {
		this.fetch = iter;
	}

	public final Fetch<T> fetch() {
		return fetch;
	}

	public final T peek() {
		return fetch.peek().value();
	}

	@Override
	public final boolean hasNext() {
		return fetch.peek().exists();
	}

	@Override
	public final T next() {
		return fetch.next().value();
	}

	@Override
	public final void close() {
		try (var _fetch = fetch) {
		}
	}

	public final Iter<T> filter(Predicate<T> pred) {
		return new Iter<T>(new FilterFetch<T>(fetch, pred));
	}

	public final <U> Iter<U> map(Function<T, U> func) {
		return new Iter<U>(new MapFetch<T, U>(fetch, func));
	}

	public final <U> Iter<U> flatMap(Function<T, Iter<U>> func) {
		return new Iter<U>(new FlatMapFetch<T, U>(fetch, func));
	}

	public final Iter<T> take(int count) {
		return new Iter<T>(new TakeFetch<T>(fetch, count));
	}

	public final Iter<T> takeLast(int count) {
		return new Iter<T>(new TakeLastFetch<T>(fetch, count));
	}

	public final Iter<T> takeWhile(Predicate<T> pred) {
		return new Iter<T>(new TakeWhileFetch<T>(fetch, pred));
	}

	public final Iter<T> skip(int count) {
		return new Iter<T>(new SkipFetch<T>(fetch, count));
	}

	public final Iter<T> skipLast(int count) {
		return new Iter<T>(new SkipLastFetch<T>(fetch, count));
	}

	public final Iter<T> skipWhile(Predicate<T> pred) {
		return new Iter<T>(new SkipWhileFetch<T>(fetch, pred));
	}

	public final <K> Iter<Iter<T>> groupBy(Function<T, K> toKey) {
		return new Iter<Iter<T>>(new GroupByFetch<T, K>(fetch, toKey));
	}

	public final Iter<Iter<T>> group(int count) {
		return new Iter<Iter<T>>(new GroupFetch<T>(fetch, count));
	}

	public final Iter<T> concat(Iter<T> second) {
		return new Iter<T>(new ConcatFetch<T>(fetch, second.fetch()));
	}

	public final Iter<T> append(T value) {
		return concat(Iter.of(value));
	}

	public final Iter<T> prepend(T value) {
		return Iter.of(value).concat(this);
	}

	public final Iter<T> uniq() {
		return new Iter<T>(new UniqByFetch<T, T>(fetch, x -> x));
	}

	public final <K> Iter<T> uniqBy(Function<T, K> toKey) {
		return new Iter<T>(new UniqByFetch<T, K>(fetch, toKey));
	}

	public final Iter<T> reverse() {
		return new Iter<T>(new ReverseFetch<T>(fetch));
	}

	public final Iter<T> shuffle() {
		return new Iter<T>(new ShuffleFetch<T>(fetch, new SplittableRandom()::nextInt));
	}

	public final Iter<T> shuffle(IntUnaryOperator random) {
		return new Iter<T>(new ShuffleFetch<T>(fetch, random));
	}

	public final Iter<ValueTile<T>> ntile(int n) {
		return new Iter<ValueTile<T>>(new NtileFetch<T>(fetch, n));
	}

	public final Iter<ValueNth<T>> nth(int n) {
		return new Iter<ValueNth<T>>(new NthFetch<T>(fetch, n));
	}

	public final Iter<ValueNumber<T>> number() {
		return new Iter<ValueNumber<T>>(new NumberFetch<T>(fetch));
	}

	public final Iter<ValueLag<T>> lag(int offset) {
		return new Iter<ValueLag<T>>(new LagFetch<T>(fetch, offset, null));
	}

	public final Iter<ValueLag<T>> lag(int offset, T defVal) {
		return new Iter<ValueLag<T>>(new LagFetch<T>(fetch, offset, defVal));
	}

	public final Iter<ValueLead<T>> lead(int offset) {
		return new Iter<ValueLead<T>>(new LeadFetch<T>(fetch, offset, null));
	}

	public final Iter<ValueLead<T>> lead(int offset, T defVal) {
		return new Iter<ValueLead<T>>(new LeadFetch<T>(fetch, offset, defVal));
	}

	public final <K extends Comparable<K>> SortIter<T> sortBy(Function<T, K> toKey) {
		return new SortIter<T>(fetch, Comparator.comparing(toKey));
	}

	public final <K extends Comparable<K>> SortIter<T> sortByDesc(Function<T, K> toKey) {
		return new SortIter<T>(fetch, Comparator.comparing(toKey, Comparator.reverseOrder()));
	}

	public final <U> Iter<Zip<T, U>> zip(Iter<U> other) {
		return new Iter<Zip<T, U>>(new ZipFetch<T, U>(fetch, other.fetch()));
	}

	public final <K extends Comparable<K>> Iter<Join<T, T>> join(Iter<T> right, Function<T, K> toKey) {
		return join(right, toKey, toKey);
	}

	public final <U, K extends Comparable<K>> Iter<Join<T, U>> join(Iter<U> right, Function<T, K> leftToKey, Function<U, K> rightToKey) {
		return new Iter<Join<T, U>>(new JoinFetch<T, U, K>(fetch, right.fetch(), leftToKey, rightToKey, (l, r) -> l.compareTo(r)));
	}

	public final <U, K> Iter<Join<T, U>> join(Iter<U> right, Function<T, K> leftToKey, Function<U, K> rightToKey, Comparator<K> comparator) {
		return new Iter<Join<T, U>>(new JoinFetch<T, U, K>(fetch, right.fetch(), leftToKey, rightToKey, comparator));
	}

	public final <K extends Comparable<K>> Iter<T> merge(Iter<T> right, Function<T, K> toKey) {
		return new Iter<T>(new MergeFetch<T>(fetch, right.fetch(), Comparator.comparing(toKey)));
	}

	public final Object[] toArray() {
		return fetch.toArray();
	}

	public final T[] toArray(IntFunction<T[]> factory) {
		return fetch.toArray(factory);
	}

	public final void forEach(Consumer<T> cons) {
		fetch.forEach(cons);
	}

	public final ArrayList<T> toList() {
		return fetch.toList();
	}

	public final LinkedHashSet<T> toSet() {
		return fetch.toSet();
	}

	public final <K> LinkedHashMap<K, T> toMap(Function<T, K> toKey) {
		return fetch.toMap(toKey);
	}

	public final <K, V> LinkedHashMap<K, V> toMap(Function<T, K> toKey, Function<T, V> toValue) {
		return fetch.toMap(toKey, toValue);
	}

	public final <K> LinkedHashMap<K, ArrayList<T>> toListMap(Function<T, K> toKey) {
		return fetch.toListMap(toKey);
	}

	public final <K, V> LinkedHashMap<K, ArrayList<V>> toListMap(Function<T, K> toKey, Function<T, V> toValue) {
		return fetch.toListMap(toKey, toValue);
	}

	public final <U> U fold(U seed, BiFunction<U, T, U> function) {
		return fetch.fold(seed, function);
	}

	public final T reduce(BinaryOperator<T> operator) {
		return fetch.reduce(operator);
	}

	public final long count() {
		return fetch.count();
	}

	public final long sumLong(ToLongFunction<T> toLong) {
		return fetch.sumLong(toLong);
	}

	public final BigDecimal sumBigDecimal(Function<T, BigDecimal> toBigDecimal) {
		return fetch.sumBigDecimal(toBigDecimal);
	}

	public final T first() {
		return fetch.first();
	}

	public final T firstOrDefault(T defaultValue) {
		return fetch.first(defaultValue);
	}

	public final T last() {
		return fetch.last();
	}

	public final T lastOrDefault(T defaultValue) {
		return fetch.last(defaultValue);
	}

	public final <K extends Comparable<K>> T maxBy(Function<T, K> toKey) {
		return fetch.max(toKey);
	}

	public final <K extends Comparable<K>> T minBy(Function<T, K> toKey) {
		return fetch.min(toKey);
	}

	public final boolean contains(T value) {
		return fetch.contains(value);
	}

	public final boolean exists() {
		return fetch.exists();
	}

	public final boolean all(Predicate<T> predicate) {
		return fetch.all(predicate);
	}

	public final boolean any(Predicate<T> predicate) {
		return fetch.any(predicate);
	}

}
