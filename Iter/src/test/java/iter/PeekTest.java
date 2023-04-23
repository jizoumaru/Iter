package iter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;

public class PeekTest {
	@Test
	public void testEmpty() {
		assertThrows(NoSuchElementException.class, () -> Iter.empty().peek());
	}

	@Test
	public void testSame() {
		var iter = Iter.of("A", "B");
		assertEquals("A", iter.peek());
		assertEquals("A", iter.peek());
		assertEquals("A", iter.peek());
		assertEquals("A", iter.next());
		
		assertEquals("B", iter.peek());
		assertEquals("B", iter.peek());
		assertEquals("B", iter.peek());
		assertEquals("B", iter.next());

		assertFalse(iter.hasNext());
	}
}
