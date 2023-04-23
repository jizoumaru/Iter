package iter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

import iter.fetch.src.ArrayFetch;

public class TakeWhileTest {
	@Test
	public void testEmpty() {
		var iter = Iter.empty().takeWhile(x -> false);
		assertFalse(iter.hasNext());
	}

	@Test
	public void testSome() {
		var iter = Iter.of(1, 2, 3, 1, 2, 3).takeWhile(x -> x < 3);
		assertEquals(1, iter.next());
		assertEquals(2, iter.next());
		assertFalse(iter.hasNext());
	}
	
	@Test
	public void testTerminate() {
		var iter = Iter.of(1, 2, 3, 1, 2, 3).takeWhile(x -> x < 3);
		assertEquals(1, iter.next());
		assertEquals(2, iter.next());

		assertThrows(NoSuchElementException.class, () -> iter.next());
		assertThrows(NoSuchElementException.class, () -> iter.next());
		assertThrows(NoSuchElementException.class, () -> iter.next());
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

		iter.takeWhile(x -> true).close();
		assertEquals(1, count.get());
	}

}
