package iter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

public class LagDefaultTest {
	@Test
	public void testEmpty() {
		var iter = Iter.empty().lag(1, -1);
		assertFalse(iter.hasNext());
	}

	@Test
	public void testSome() {
		var iter = Iter.of(1, 2, 3, 4, 5, 6).lag(3, -1);
		assertEquals(new ValueLag<Integer>(1, -1), iter.next());
		assertEquals(new ValueLag<Integer>(2, -1), iter.next());
		assertEquals(new ValueLag<Integer>(3, -1), iter.next());
		assertEquals(new ValueLag<Integer>(4, 1), iter.next());
		assertEquals(new ValueLag<Integer>(5, 2), iter.next());
		assertEquals(new ValueLag<Integer>(6, 3), iter.next());
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

		iter.lag(1, -1).close();
		assertEquals(1, count.get());
	}

}
