package iter;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class HasNextTest {
	@Test
	public void testEmpty() {
		var iter = Iter.empty();

		assertFalse(iter.hasNext());
		assertFalse(iter.hasNext());
		assertFalse(iter.hasNext());
	}

	@Test
	public void testSome() {
		var iter = Iter.of("A", "B");

		assertTrue(iter.hasNext());
		assertTrue(iter.hasNext());
		assertTrue(iter.hasNext());
		
		iter.next();

		assertTrue(iter.hasNext());
		assertTrue(iter.hasNext());
		assertTrue(iter.hasNext());

		iter.next();

		assertFalse(iter.hasNext());
		assertFalse(iter.hasNext());
		assertFalse(iter.hasNext());
	}

}
