package iter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

public class ArrayTest {
	@Test
	public void testEmtpy() {
		var iter = Iter.of();
		assertFalse(iter.hasNext());
	}

	@Test
	public void testThree() {
		var iter = Iter.of(1, 2, 3);
		assertEquals(1, iter.next());
		assertEquals(2, iter.next());
		assertEquals(3, iter.next());
		assertFalse(iter.hasNext());
	}
}
