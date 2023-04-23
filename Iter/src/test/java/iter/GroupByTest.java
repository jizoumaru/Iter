package iter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

public class GroupByTest {
	@Test
	public void testEmpty() {
		var iter = Iter.empty().groupBy(x -> x);
		assertFalse(iter.hasNext());
	}

	@Test
	public void testSome() {
		var iter = Iter.of(1, 2, 2, 3, 3, 3).groupBy(x -> x);
		assertEquals(Arrays.asList(1), iter.next().toList());
		assertEquals(Arrays.asList(2, 2), iter.next().toList());
		assertEquals(Arrays.asList(3, 3, 3), iter.next().toList());
		assertFalse(iter.hasNext());
	}

	@Test
	public void testSkip() {
		var iter = Iter.range(1, 6).flatMap(x -> Iter.repeat(x, 3)).groupBy(x -> x).skip(3).map(x -> x.toList());
		assertEquals(Arrays.asList(4, 4, 4), iter.next());
		assertEquals(Arrays.asList(5, 5, 5), iter.next());
		assertEquals(Arrays.asList(6, 6, 6), iter.next());
		assertFalse(iter.hasNext());
	}

	@Test
	public void testKey() {
		var iter = Iter.of("a1", "b2", "c2", "d3", "e3", "f3").groupBy(x -> x.substring(1));
		assertEquals(Arrays.asList("a1"), iter.next().toList());
		assertEquals(Arrays.asList("b2", "c2"), iter.next().toList());
		assertEquals(Arrays.asList("d3", "e3", "f3"), iter.next().toList());
		assertFalse(iter.hasNext());
	}

	@Test
	public void testClose() {
		var count = new AtomicInteger(0);

		var iter = new Iter<Integer>(new Fetch<Integer>() {
			@Override
			protected Holder<Integer> internalNext() {
				return Holder.none();
			}

			@Override
			protected void internalClose() {
				count.incrementAndGet();
			}
		});

		iter.groupBy(x -> x).close();
		assertEquals(1, count.get());
	}

}
