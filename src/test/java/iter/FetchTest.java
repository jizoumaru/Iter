package iter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class FetchTest {
	@Test
	public void testEmpty() {
		assertFalse(Iter.empty().fetch().next().exists());
	}

	@Test
	public void testSome() {
		assertTrue(Iter.of(1).fetch().next().exists());
		assertEquals(1, Iter.of(1).fetch().next().value());
	}
}
