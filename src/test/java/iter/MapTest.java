package iter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

public class MapTest {
	@Test
	public void testEmpty() {
		assertFalse(Iter.empty().map(x -> x).exists());
	}

	@Test
	public void testSame() {
		var iter = Iter.of("A", "B", "C").map(x -> "x" + x);
		assertEquals("xA", iter.next());
		assertEquals("xB", iter.next());
		assertEquals("xC", iter.next());
		assertFalse(iter.hasNext());
	}

	@Test
	public void testClose() {
		var count = new AtomicInteger(0);

		var iter = new Iter<Integer>(new Fetch<Integer>() {
			@Override
			protected Holder<Integer> internalNext() {
				return Holder.none();
			}

			@Override
			protected void internalClose() {
				count.incrementAndGet();
			}
		});

		iter.map(x -> x).close();
		assertEquals(1, count.get());
	}

}
