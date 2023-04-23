package iter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.LinkedHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

import iter.fetch.src.ArrayFetch;

public class ToMapTest {
	@Test
	public void testEmpty() {
		assertEquals(new LinkedHashMap<Integer, Integer>(), Iter.<Integer>empty().toMap(x -> x));
	}

	@Test
	public void testSome() {
		var expected = new LinkedHashMap<String, String>();
		expected.put("A", "AA");
		expected.put("B", "BC");
		expected.put("C", "CF");
		assertEquals(expected, Iter.of("AA", "BB", "CD", "BC", "CE", "CF").toMap(x -> x.substring(0, 1)));
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

		iter.toMap(x -> x);
		assertEquals(1, count.get());
	}

}
