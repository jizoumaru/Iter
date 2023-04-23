package iter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

import iter.fetch.src.ArrayFetch;

public class NtileTest {
	@Test
	public void testEmpty() {
		var iter = Iter.empty().ntile(1);
		assertFalse(iter.hasNext());
	}

	@Test
	public void testSame() {
		var iter = Iter.of(1, 2, 3, 4, 5, 6, 7).ntile(3);
		assertEquals(new ValueTile<Integer>(1, 1), iter.next());
		assertEquals(new ValueTile<Integer>(2, 1), iter.next());
		assertEquals(new ValueTile<Integer>(3, 1), iter.next());
		assertEquals(new ValueTile<Integer>(4, 2), iter.next());
		assertEquals(new ValueTile<Integer>(5, 2), iter.next());
		assertEquals(new ValueTile<Integer>(6, 3), iter.next());
		assertEquals(new ValueTile<Integer>(7, 3), iter.next());
		assertFalse(iter.hasNext());
	}
	
	@Test
	public void testClose() {
		var count = new AtomicInteger(0);

		var iter = new Iter<Integer>(new Fetch<Integer>() {
			Fetch<Integer> delegate = new ArrayFetch<Integer>(1);

			@Override
			protected Holder<Integer> internalNext() {
				return delegate.next();
			}

			@Override
			protected void internalClose() {
				count.incrementAndGet();
			}
		});

		iter.ntile(1).close();
		assertEquals(1, count.get());
	}

}
