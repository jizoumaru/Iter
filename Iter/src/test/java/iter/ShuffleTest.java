package iter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

import iter.fetch.src.ArrayFetch;

public class ShuffleTest {
	@Test
	public void testEmpty() {
		var iter = Iter.empty().shuffle();
		assertFalse(iter.hasNext());
	}

	@Test
	public void testSome() {
		var iter = Iter.of("A").shuffle();
		assertEquals("A", iter.next());
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

		iter.shuffle().close();
		assertEquals(1, count.get());
	}

}
