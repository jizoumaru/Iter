package iter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

public class IteratorTest {
	@Test
	public void testEmtpy() {
		var iter = Iter.from(Arrays.asList().iterator());
		assertFalse(iter.hasNext());
	}

	@Test
	public void testThree() {
		var iter = Iter.from(Arrays.asList(1, 2, 3).iterator());
		assertEquals(1, iter.next());
		assertEquals(2, iter.next());
		assertEquals(3, iter.next());
		assertFalse(iter.hasNext());
	}
}
