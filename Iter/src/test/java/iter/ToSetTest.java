package iter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.LinkedHashSet;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

import iter.fetch.src.ArrayFetch;

public class ToSetTest {
	@Test
	public void testEmpty() {
		assertEquals(new LinkedHashSet<Integer>(), Iter.<Integer>empty().toSet());
	}

	@Test
	public void testSome() {
		var expected = new LinkedHashSet<String>();
		expected.add("A");
		expected.add("B");
		expected.add("C");
		assertEquals(expected, Iter.of("A", "B", "C", "B", "C", "C").toSet());
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

		iter.toSet();
		assertEquals(1, count.get());
	}

}
