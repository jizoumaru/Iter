package iter;

import java.util.Objects;

public final class ValueLag<T> {
	public final T value;
	public final T lag;

	public ValueLag(T value, T lag) {
		this.value = value;
		this.lag = lag;
	}

	@Override
	public final int hashCode() {
		return Objects.hash(lag, value);
	}

	@Override
	public final boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof ValueLag)) {
			return false;
		}

		@SuppressWarnings("unchecked")
		var other = (ValueLag<T>) obj;

		return Objects.equals(lag, other.lag)
				&& Objects.equals(value, other.value);
	}

	@Override
	public final String toString() {
		return "(" + value + ", " + lag + ")";
	}
}
