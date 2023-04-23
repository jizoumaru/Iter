package iter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

import iter.fetch.src.ArrayFetch;

public class SortByDescTest {
	@Test
	public void testEmpty() {
		var iter = Iter.<Integer>empty().sortByDesc(x -> x);
		assertFalse(iter.hasNext());
	}

	@Test
	public void testSome() {
		var iter = Iter.of("ABC", "BCA", "CAB").sortByDesc(x -> x.substring(1, 2));
		assertEquals("BCA", iter.next());
		assertEquals("ABC", iter.next());
		assertEquals("CAB", iter.next());
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

		iter.sortByDesc(x -> x).close();
		assertEquals(1, count.get());
	}

}
