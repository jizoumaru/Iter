package iter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

import iter.fetch.src.ArrayFetch;

public class UniqByTest {
	@Test
	public void testEmpty() {
		assertFalse(Iter.empty().uniqBy(x -> x).hasNext());
	}

	@Test
	public void testSome() {
		var iter = Iter.of("AA", "BB", "CB", "DC", "EC", "FC").uniqBy(x -> x.substring(1, 2));
		assertEquals("AA", iter.next());
		assertEquals("BB", iter.next());
		assertEquals("DC", iter.next());
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

		iter.uniqBy(x -> x).close();
		assertEquals(1, count.get());
	}

}
