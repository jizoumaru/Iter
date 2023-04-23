package iter;

import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

public class EmptyTest {
	@Test
	public void testEmpty() {
		var iter = Iter.empty();
		assertFalse(iter.hasNext());
	}
}
