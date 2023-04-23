package iter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

public class AppendTest {
	@Test
	public void testOne() {
		var iter = Iter.of().append(1);
		assertEquals(1, iter.next());
		assertFalse(iter.hasNext());
	}

	@Test
	public void testThree() {
		var iter = Iter.of(1, 2).append(3);
		assertEquals(1, iter.next());
		assertEquals(2, iter.next());
		assertEquals(3, iter.next());
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

		iter.append("A").close();
		assertEquals(1, count.get());
	}
}
