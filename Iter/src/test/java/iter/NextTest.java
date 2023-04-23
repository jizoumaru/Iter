package iter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;

public class NextTest {
	@Test
	public void testEmpty() {
		var thrown = false;

		try {
			Iter.empty().next();
		} catch (NoSuchElementException e) {
			thrown = true;
		}

		assertTrue(thrown);
	}

	@Test
	public void testSame() {
		var iter = Iter.of("A", "B", "C");
		
		assertEquals("A", iter.next());
		assertEquals("B", iter.next());
		assertEquals("C", iter.next());

		var thrown = false;

		try {
			iter.next();
		} catch (NoSuchElementException e) {
			thrown = true;
		}

		assertTrue(thrown);

	}
}
