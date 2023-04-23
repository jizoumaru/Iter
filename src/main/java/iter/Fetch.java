package iter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.ToLongFunction;

public abstract class Fetch<T> implements AutoCloseable {
	private boolean closed = false;
	private Holder<T> peek;

	public final Holder<T> peek() {
		if (peek == null) {
			peek = internalNext();
		}
		return peek;
	}

	public final Holder<T> next() {
		if (peek != null) {
			var ret = peek;
			peek = null;
			return ret;
		} else {
			return internalNext();
		}
	}

	protected abstract Holder<T> internalNext();

	@Override
	public final void close() {
		if (!closed) {
			closed = true;
			internalClose();
		}
	}

	protected void internalClose() {
	}

	@SuppressWarnings("unchecked")
	public final Object[] toArray() {
		return toArray(x -> (T[]) new Object[x]);
	}

	public final T[] toArray(IntFunction<T[]> factory) {
		try (var _this = this) {
			var a = factory.apply(33);
			var i = 0;

			while (true) {
				var holder = next();

				if (!holder.exists()) {
					break;
				}

				if (i >= a.length) {
					var n = factory.apply(a.length * 2 - 1);
					System.arraycopy(a, 0, n, 0, a.length);
					a = n;
				}

				a[i++] = holder.value();
			}

			if (i < a.length) {
				var n = factory.apply(i);
				System.arraycopy(a, 0, n, 0, i);
				a = n;
			}

			return a;
		}
	}

	public final void forEach(Consumer<T> cons) {
		try (var _this = this) {
			while (true) {
				var holder = next();

				if (!holder.exists()) {
					break;
				}

				cons.accept(holder.value());
			}
		}
	}

	public final ArrayList<T> toList() {
		try (var _this = this) {
			var list = new ArrayList<T>();

			while (true) {
				var holder = next();

				if (!holder.exists()) {
					break;
				}

				list.add(holder.value());
			}

			return list;
		}
	}

	public final LinkedHashSet<T> toSet() {
		try (var _this = this) {
			var set = new LinkedHashSet<T>();

			while (true) {
				var holder = next();

				if (!holder.exists()) {
					break;
				}

				set.add(holder.value());
			}

			return set;
		}
	}

	public final <K> LinkedHashMap<K, T> toMap(Function<T, K> toKey) {
		return toMap(toKey, x -> x);
	}

	public final <K, V> LinkedHashMap<K, V> toMap(Function<T, K> toKey, Function<T, V> toValue) {
		try (var _this = this) {
			var map = new LinkedHashMap<K, V>();

			while (true) {
				var holder = next();

				if (!holder.exists()) {
					break;
				}

				var key = toKey.apply(holder.value());
				var value = toValue.apply(holder.value());
				map.put(key, value);
			}

			return map;
		}
	}

	public final <K> LinkedHashMap<K, ArrayList<T>> toListMap(Function<T, K> toKey) {
		return toListMap(toKey, x -> x);
	}

	public final <K, V> LinkedHashMap<K, ArrayList<V>> toListMap(Function<T, K> toKey, Function<T, V> toValue) {
		try (var _this = this) {
			var map = new LinkedHashMap<K, ArrayList<V>>();

			while (true) {
				var holder = next();

				if (!holder.exists()) {
					break;
				}

				var key = toKey.apply(holder.value());
				var value = toValue.apply(holder.value());
				var list = map.get(key);

				if (list == null) {
					list = new ArrayList<V>();
					map.put(key, list);
				}

				list.add(value);
			}

			return map;
		}
	}

	public final <U> U fold(U seed, BiFunction<U, T, U> function) {
		try (var _this = this) {
			var left = seed;

			while (true) {
				var holder = next();

				if (!holder.exists()) {
					break;
				}

				left = function.apply(left, holder.value());
			}

			return left;
		}
	}

	public final T reduce(BinaryOperator<T> operator) {
		try (var _this = this) {
			var left = next().value();

			while (true) {
				var holder = next();

				if (!holder.exists()) {
					break;
				}

				left = operator.apply(left, holder.value());
			}

			return left;
		}
	}

	public final long count() {
		try (var _this = this) {
			var count = 0L;

			while (true) {
				var holder = next();

				if (!holder.exists()) {
					break;
				}

				count++;
			}

			return count;
		}
	}

	public final long sumLong(ToLongFunction<T> toLong) {
		try (var _this = this) {
			var sum = 0L;

			while (true) {
				var holder = next();

				if (!holder.exists()) {
					break;
				}

				sum += toLong.applyAsLong(holder.value());
			}

			return sum;
		}
	}

	public final BigDecimal sumBigDecimal(Function<T, BigDecimal> toBigDecimal) {
		try (var _this = this) {
			var sum = BigDecimal.ZERO;

			while (true) {
				var holder = next();

				if (!holder.exists()) {
					break;
				}

				sum = sum.add(toBigDecimal.apply(holder.value()));
			}

			return sum;
		}
	}

	public final T first() {
		try (var _this = this) {
			return next().value();
		}
	}

	public final T first(T defaultValue) {
		try (var _this = this) {
			var holder = next();
			return holder.exists() ? holder.value() : defaultValue;
		}
	}

	public final T last() {
		try (var _this = this) {
			var last = next().value();

			while (true) {
				var holder = next();

				if (!holder.exists()) {
					break;
				}

				last = holder.value();
			}

			return last;
		}
	}

	public final T last(T defaultValue) {
		try (var _this = this) {
			var last = defaultValue;

			while (true) {
				var holder = next();

				if (!holder.exists()) {
					break;
				}

				last = holder.value();
			}

			return last;
		}
	}

	public final <K extends Comparable<K>> T max(Function<T, K> toKey) {
		try (var _this = this) {
			var max = next().value();

			while (true) {
				var holder = next();

				if (!holder.exists()) {
					break;
				}

				if (toKey.apply(max).compareTo(toKey.apply(holder.value())) < 0) {
					max = holder.value();
				}
			}

			return max;
		}
	}

	public final <K extends Comparable<K>> T min(Function<T, K> toKey) {
		try (var _this = this) {
			var min = next().value();

			while (true) {
				var holder = next();

				if (!holder.exists()) {
					break;
				}

				if (toKey.apply(min).compareTo(toKey.apply(holder.value())) > 0) {
					min = holder.value();
				}
			}

			return min;
		}
	}

	public final boolean contains(T value) {
		try (var _this = this) {
			while (true) {
				var holder = next();

				if (!holder.exists()) {
					break;
				}

				if (Objects.equals(value, holder.value())) {
					return true;
				}
			}

			return false;
		}
	}

	public final boolean exists() {
		try (var _this = this) {
			return next().exists();
		}
	}

	public final boolean all(Predicate<T> predicate) {
		try (var _this = this) {
			while (true) {
				var holder = next();

				if (!holder.exists()) {
					break;
				}

				if (!predicate.test(holder.value())) {
					return false;
				}
			}

			return true;
		}
	}

	public final boolean any(Predicate<T> predicate) {
		try (var _this = this) {
			while (true) {
				var holder = next();

				if (!holder.exists()) {
					break;
				}

				if (predicate.test(holder.value())) {
					return true;
				}
			}

			return false;
		}
	}
}
