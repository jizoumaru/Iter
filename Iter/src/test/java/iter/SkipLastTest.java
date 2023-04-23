package iter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

import iter.fetch.src.ArrayFetch;

public class SkipLastTest {
	@Test
	public void testEmpty() {
		var iter = Iter.empty().skipLast(1);
		assertFalse(iter.hasNext());
	}

	@Test
	public void testSome() {
		var iter = Iter.of("A", "B", "C", "D", "E", "F").skipLast(3);
		assertEquals("A", iter.next());
		assertEquals("B", iter.next());
		assertEquals("C", iter.next());
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

		iter.skipLast(1).close();
		assertEquals(1, count.get());
	}

}
