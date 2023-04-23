package iter;

import java.util.Objects;

public final class ValueLead<T> {
	public final T value;
	public final T lead;

	public ValueLead(T value, T lead) {
		this.value = value;
		this.lead = lead;
	}

	@Override
	public final int hashCode() {
		return Objects.hash(lead, value);
	}

	@Override
	public final boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof ValueLead)) {
			return false;
		}

		@SuppressWarnings("unchecked")
		var other = (ValueLead<T>) obj;

		return Objects.equals(lead, other.lead)
				&& Objects.equals(value, other.value);
	}

	@Override
	public final String toString() {
		return "(" + value + ", " + lead + ")";
	}
}
