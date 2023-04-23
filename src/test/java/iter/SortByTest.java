package iter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

import iter.fetch.src.ArrayFetch;

public class SortByTest {
	@Test
	public void testEmpty() {
		var iter = Iter.<Integer>empty().sortBy(x -> x);
		assertFalse(iter.hasNext());
	}

	@Test
	public void testSome() {
		var iter = Iter.of("ABC", "BCA", "CAB").sortBy(x -> x.substring(1, 2));
		assertEquals("CAB", iter.next());
		assertEquals("ABC", iter.next());
		assertEquals("BCA", iter.next());
		assertFalse(iter.hasNext());
	}

	@Test
	public void testSmall() {
		for (var i = 0; i < 10; i++) {
			var expected = Iter.range(1, 7).flatMap(x -> Iter.repeat(x, x)).toList();
			var actual = Iter.from(expected).shuffle().sortBy(x -> x).toList();
			assertEquals(expected, actual);
		}
	}

	@Test
	public void testMiddle() {
		for (var i = 0; i < 10; i++) {
			var expected = Iter.range(1, 40).flatMap(x -> Iter.repeat(x, x)).toList();
			var actual = Iter.from(expected).shuffle().sortBy(x -> x).toList();
			assertEquals(expected, actual);
		}
	}

	@Test
	public void testBig() {
		for (var i = 0; i < 10; i++) {
			var expected = Iter.range(1, 100).flatMap(x -> Iter.repeat(x, x)).toList();
			var actual = Iter.from(expected).shuffle().sortBy(x -> x).toList();
			assertEquals(expected, actual);
		}
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

		iter.sortBy(x -> x).close();
		assertEquals(1, count.get());
	}

}
