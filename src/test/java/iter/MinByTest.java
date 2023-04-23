package iter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

import iter.fetch.src.ArrayFetch;

public class MinByTest {
	@Test
	public void testEmpty() {
		var thrown = false;

		try {
			Iter.<Integer>empty().minBy(x -> x);
		} catch (NoSuchElementException e) {
			thrown = true;
		}

		assertTrue(thrown);
	}

	@Test
	public void testSame() {
		assertEquals("31", Iter.of("13", "22", "31").minBy(x -> x.substring(1)));
		assertEquals("13", Iter.of("13", "22", "31").minBy(x -> x.substring(0, 1)));
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

		iter.minBy(x -> x);
		assertEquals(1, count.get());
	}

}
