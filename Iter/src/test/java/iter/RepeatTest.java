package iter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

public class RepeatTest {
	@Test
	public void testEmtpy() {
		var iter = Iter.repeat("A", 0);
		assertFalse(iter.hasNext());
	}

	@Test
	public void testThree() {
		var iter = Iter.repeat("A", 3);
		assertEquals("A", iter.next());
		assertEquals("A", iter.next());
		assertEquals("A", iter.next());
		assertFalse(iter.hasNext());
	}
}
