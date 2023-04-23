package iter;

import java.util.Objects;

public final class Zip<A, B> {
	public final A a;
	public final B b;

	public Zip(A a, B b) {
		this.a = a;
		this.b = b;
	}

	@Override
	public final int hashCode() {
		return Objects.hash(a, b);
	}

	@Override
	public final boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof Zip)) {
			return false;
		}

		@SuppressWarnings("unchecked")
		var other = (Zip<A, B>) obj;

		return Objects.equals(a, other.a)
				&& Objects.equals(b, other.b);
	}

	@Override
	public final String toString() {
		return "(" + a + ", " + b + ")";
	}
}
