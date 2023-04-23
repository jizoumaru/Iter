package iter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

import iter.fetch.src.ArrayFetch;

public class ReduceTest {
	@Test
	public void testEmpty() {
		assertThrows(NoSuchElementException.class, () -> Iter.<Integer>empty().reduce((l, r) -> l + r));
	}

	@Test
	public void testSome() {
		assertEquals(6, Iter.of(1, 2, 3).reduce((l, r) -> l + r));
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

		iter.reduce((l, r) -> l + 1);
		assertEquals(1, count.get());
	}

}
