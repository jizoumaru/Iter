package iter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

public class StreamTest {
	@Test
	public void testEmtpy() {
		var iter = Iter.from(Stream.of());
		assertFalse(iter.hasNext());
	}

	@Test
	public void testThree() {
		var iter = Iter.from(Stream.of("A", "B", "C"));
		assertEquals("A", iter.next());
		assertEquals("B", iter.next());
		assertEquals("C", iter.next());
		assertFalse(iter.hasNext());
	}
	
	@Test
	public void testClose() {
		var count = new AtomicInteger(0);

		Iter.from(Stream.of("A").onClose(() -> count.incrementAndGet())).close();
		assertEquals(1, count.get());
	}

}
