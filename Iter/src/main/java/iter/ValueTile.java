package iter;

import java.util.Objects;

public final class ValueTile<T> {
	public final T value;
	public final int tile;

	public ValueTile(T val, int tile) {
		this.value = val;
		this.tile = tile;
	}

	@Override
	public final int hashCode() {
		return Objects.hash(tile, value);
	}

	@Override
	public final boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof ValueTile)) {
			return false;
		}

		@SuppressWarnings("unchecked")
		var other = (ValueTile<T>) obj;
		
		return tile == other.tile
				&& Objects.equals(value, other.value);
	}

	@Override
	public final String toString() {
		return "(" + value + ", " + tile + ")";
	}
}
