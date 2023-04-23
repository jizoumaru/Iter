package iter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

import iter.fetch.src.ArrayFetch;

public class MergeTest {
	@Test
	public void testEmpty() {
		var iter = Iter.<Integer>empty().merge(Iter.<Integer>empty(), x -> x);

		assertFalse(iter.hasNext());
	}

	@Test
	public void testSome() {
		var iter = Iter.of("L1", "L3").merge(Iter.of("R2", "R4"), x -> x.substring(1));
		assertEquals("L1", iter.next());
		assertEquals("R2", iter.next());
		assertEquals("L3", iter.next());
		assertEquals("R4", iter.next());
		assertFalse(iter.hasNext());
	}

	@Test
	public void testCloseFirst() {
		var count = new AtomicInteger(0);

		var a = new Iter<Integer>(new Fetch<Integer>() {
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

		a.merge(Iter.empty(), x -> x).close();

		assertEquals(1, count.get());
	}

	@Test
	public void testCloseSecond() {
		var count = new AtomicInteger(0);

		var a = new Iter<Integer>(new Fetch<Integer>() {
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

		Iter.<Integer>empty().merge(a, x -> x).close();

		assertEquals(1, count.get());
	}

}
