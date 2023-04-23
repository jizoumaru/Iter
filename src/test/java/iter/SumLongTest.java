package iter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

import iter.fetch.src.ArrayFetch;

public class SumLongTest {
	@Test
	public void testEmpty() {
		assertEquals(0L, Iter.<Long>empty().sumLong(x -> x));
	}

	@Test
	public void testSome() {
		assertEquals(6L, Iter.of("1", "2", "3").sumLong(x -> Long.valueOf(x)));
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

		iter.sumLong(x -> Long.valueOf(x));
		assertEquals(1, count.get());
	}

}
