package iter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

import iter.fetch.src.ArrayFetch;

public class SumBigDecimalTest {
	@Test
	public void testEmpty() {
		assertEquals(BigDecimal.ZERO, Iter.<BigDecimal>empty().sumBigDecimal(x -> x));
	}

	@Test
	public void testSome() {
		assertEquals(BigDecimal.valueOf(6), Iter.of("1", "2", "3").sumBigDecimal(x -> new BigDecimal(x)));
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

		iter.sumBigDecimal(x -> BigDecimal.valueOf(x));
		assertEquals(1, count.get());
	}

}
