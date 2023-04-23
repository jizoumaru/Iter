package iter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

import iter.fetch.src.ArrayFetch;

public class ToArrayAsTest {
	@Test
	public void testEmpty() {
		var a = Iter.<Integer>empty().toArray(Integer[]::new);
		assertTrue(a instanceof Integer[]);
		assertTrue(Arrays.equals(new Integer[] {}, a));
	}

	@Test
	public void testSome() {
		var a = Iter.of("A", "B", "C").toArray(String[]::new);
		assertTrue(a instanceof String[]);
		assertTrue(Arrays.equals(new String[] { "A", "B", "C" }, a));
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

		iter.toArray(Integer[]::new);
		assertEquals(1, count.get());
	}

}
