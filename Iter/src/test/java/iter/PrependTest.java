package iter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

public class PrependTest {
	@Test
	public void testOne() {
		var iter = Iter.of().prepend("A");
		assertEquals("A", iter.next());
		assertFalse(iter.hasNext());
	}

	@Test
	public void testThree() {
		var iter = Iter.of("B", "C").prepend("A");
		assertEquals("A", iter.next());
		assertEquals("B", iter.next());
		assertEquals("C", iter.next());
		assertFalse(iter.hasNext());
	}

	@Test
	public void testClose() {
		var count = new AtomicInteger(0);

		var iter = new Iter<String>(new Fetch<String>() {
			@Override
			protected Holder<String> internalNext() {
				return Holder.none();
			}

			@Override
			protected void internalClose() {
				count.incrementAndGet();
			}
		});

		iter.prepend("A").close();
		assertEquals(1, count.get());
	}

}
