package iter;

import java.util.Objects;

public final class Join<L, R> {
	public enum State {
		LEFT, BOTH, RIGHT
	}

	public static final <L, R> Join<L, R> left(L left) {
		return new Join<L, R>(State.LEFT, left, null);
	}

	public static final <L, R> Join<L, R> right(R right) {
		return new Join<L, R>(State.RIGHT, null, right);
	}

	public static final <L, R> Join<L, R> both(L left, R right) {
		return new Join<L, R>(State.BOTH, left, right);
	}

	private final State state;
	private final L left;
	private final R right;

	public Join(State state, L left, R right) {
		this.state = state;
		this.left = left;
		this.right = right;
	}

	public final State state() {
		return state;
	}

	public final L left() {
		if (state == State.RIGHT) {
			throw new IllegalStateException();
		}
		return left;
	}

	public final R right() {
		if (state == State.LEFT) {
			throw new IllegalStateException();
		}
		return right;
	}

	@Override
	public final int hashCode() {
		return Objects.hash(left, right, state);
	}

	@Override
	public final boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		
		if (!(obj instanceof Join)) {
			return false;
		}
		
		@SuppressWarnings("unchecked")
		var other = (Join<L, R>) obj;
		
		return Objects.equals(left, other.left)
				&& Objects.equals(right, other.right)
				&& state == other.state;
	}

	@Override
	public final String toString() {
		if (state == State.BOTH) {
			return "both(" + left + ", " + right + ")";
		} else if (state == State.LEFT) {
			return "left(" + left + ")";
		} else {
			return "right(" + right + ")";
		}
	}
}
