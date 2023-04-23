package iter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

public class RangeTest {
	@Test
	public void testEmtpy() {
		var iter = Iter.range(0, 0);
		assertFalse(iter.hasNext());
	}

	@Test
	public void testThree() {
		var iter = Iter.range(3, 3);
		assertEquals(3, iter.next());
		assertEquals(4, iter.next());
		assertEquals(5, iter.next());
		assertFalse(iter.hasNext());
	}
}
