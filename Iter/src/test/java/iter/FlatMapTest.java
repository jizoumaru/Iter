package iter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

public class FlatMapTest {
	@Test
	public void testEmpty() {
		var iter = Iter.empty().flatMap(x -> Iter.empty());
		assertFalse(iter.hasNext());
	}

	@Test
	public void testMap() {
		var iter = Iter.of(0, 1, 2).flatMap(x -> Iter.of(x + 1));
		assertEquals(1, iter.next());
		assertEquals(2, iter.next());
		assertEquals(3, iter.next());
		assertFalse(iter.hasNext());
	}

	@Test
	public void testExpand() {
		var iter = Iter.of(1).flatMap(x -> Iter.of(x, x, x));
		assertEquals(1, iter.next());
		assertEquals(1, iter.next());
		assertEquals(1, iter.next());
		assertFalse(iter.hasNext());
	}

	@Test
	public void testFilter() {
		var iter = Iter.of(1, 2, 3, 4, 5, 6).flatMap(x -> x % 2 == 1 ? Iter.of(x) : Iter.empty());
		assertEquals(1, iter.next());
		assertEquals(3, iter.next());
		assertEquals(5, iter.next());
		assertFalse(iter.hasNext());
	}

	@Test
	public void testCloseSrc() {
		var count = new AtomicInteger(0);

		var iter = new Iter<String>(new Fetch<String>() {
			@Override
			protected Holder<String> internalNext() {
				return Holder.of("A");
			}

			@Override
			protected void internalClose() {
				count.incrementAndGet();
			}
		});

		iter.flatMap(x -> Iter.of(x)).close();
		assertEquals(1, count.get());
	}

	@Test
	public void testCloseMap() {
		var count = new AtomicInteger(0);

		var iter = Iter.of("A", "B", "C")
				.flatMap(x -> new Iter<String>(new Fetch<String>() {
					boolean terminated = false;

					@Override
					protected Holder<String> internalNext() {
						if (!terminated) {
							terminated = true;
							return Holder.of(x);
						} else {
							return Holder.none();
						}
					}

					@Override
					protected void internalClose() {
						count.incrementAndGet();
					}
				}));

		iter.next();
		iter.next();
		assertEquals(1, count.get());
		
		iter.next();
		assertEquals(2, count.get());
		
		iter.close();
		assertEquals(3, count.get());
	}

}
