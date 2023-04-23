package iter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

import iter.fetch.src.ArrayFetch;

public class ThenByTest {
	@Test
	public void testEmpty() {
		var iter = Iter.<Integer>empty().sortBy(x -> x).thenBy(x -> x);
		assertFalse(iter.hasNext());
	}

	@Test
	public void testSome() {
		var iter = Iter.of("ABC", "ACA", "AAB").sortBy(x -> x.substring(0, 1)).thenBy(x -> x.substring(1, 2));
		assertEquals("AAB", iter.next());
		assertEquals("ABC", iter.next());
		assertEquals("ACA", iter.next());
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

		iter.sortBy(x -> x).thenBy(x -> x).close();
		assertEquals(1, count.get());
	}

}
