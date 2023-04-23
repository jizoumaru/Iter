package iter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

import iter.fetch.src.ArrayFetch;

public class ToArrayTest {
	@Test
	public void testEmpty() {
		assertTrue(Arrays.equals(new Object[0], Iter.empty().toArray()));
	}

	@Test
	public void testSome() {
		assertTrue(Arrays.equals(new Object[] { "A", "B", "C" }, Iter.of("A", "B", "C").toArray()));
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

		iter.toArray();
		assertEquals(1, count.get());
	}

}
