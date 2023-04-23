package iter;

import java.util.NoSuchElementException;
import java.util.Objects;

public final class Holder<T> {
	public static final <T> Holder<T> of(T value) {
		return new Holder<T>(value, true);
	}

	public static final <T> Holder<T> none() {
		return new Holder<T>(null, false);
	}

	private final T value;
	private final boolean exists;

	private Holder(T value, boolean exists) {
		this.value = value;
		this.exists = exists;
	}

	public final T value() {
		if (exists) {
			return value;
		}
		throw new NoSuchElementException();
	}

	public final boolean exists() {
		return exists;
	}

	@Override
	public final int hashCode() {
		return Objects.hash(exists, value);
	}

	@Override
	public final boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof Holder)) {
			return false;
		}

		@SuppressWarnings("unchecked")
		var other = (Holder<T>) obj;
		
		return exists == other.exists
				&& Objects.equals(value, other.value);
	}

	@Override
	public final String toString() {
		return exists ? String.valueOf(value) : "NONE";
	}
}
