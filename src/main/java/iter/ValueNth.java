package iter;

import java.util.Objects;

public final class ValueNth<T> {
	public final T value;
	public final T nth;

	public ValueNth(T value, T nth) {
		this.value = value;
		this.nth = nth;
	}

	@Override
	public final int hashCode() {
		return Objects.hash(nth, value);
	}

	@Override
	public final boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof ValueNth)) {
			return false;
		}

		@SuppressWarnings("unchecked")
		var other = (ValueNth<T>) obj;

		return Objects.equals(nth, other.nth)
				&& Objects.equals(value, other.value);
	}

	@Override
	public final String toString() {
		return "(" + value + ", " + nth + ")";
	}
}
