package iter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

import iter.fetch.src.ArrayFetch;

public class LastTest {
	@Test
	public void testEmpty() {
		var called = false;

		try {
			Iter.empty().last();
		} catch (NoSuchElementException e) {
			called = true;
		}

		assertTrue(called);
	}

	@Test
	public void testSome() {
		assertEquals("C", Iter.of("A", "B", "C").last());
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

		iter.last();
		assertEquals(1, count.get());
	}

}
