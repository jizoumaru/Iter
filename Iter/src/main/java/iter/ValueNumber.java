package iter;

import java.util.Objects;

public final class ValueNumber<T> {
	public final T value;
	public final int number;

	public ValueNumber(T value, int number) {
		this.value = value;
		this.number = number;
	}

	@Override
	public final int hashCode() {
		return Objects.hash(number, value);
	}

	@Override
	public final boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof ValueNumber)) {
			return false;
		}

		@SuppressWarnings("unchecked")
		var other = (ValueNumber<T>) obj;
		
		return number == other.number
				&& Objects.equals(value, other.value);
	}

	@Override
	public final String toString() {
		return "(" + value + ", " + number + ")";
	}
}
