package iter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

public class JoinOtherTest {
	@Test
	public void testEmpty() {
		var l = Iter.<Integer>empty();
		var r = Iter.<String>empty();

		var iter = l.join(r, x -> x, Integer::valueOf);
		assertFalse(iter.hasNext());
	}

	@Test
	public void testLR() {
		var l = Iter.of(1, 2);
		var r = Iter.of("2", "3");

		var iter = l.join(r, x -> x, Integer::valueOf);
		assertEquals(Join.left(1), iter.next());
		assertEquals(Join.both(2, "2"), iter.next());
		assertEquals(Join.right("3"), iter.next());
		assertFalse(iter.hasNext());
	}

	@Test
	public void testRL() {
		var l = Iter.of("2", "3");
		var r = Iter.of(1, 2);

		var iter = l.join(r, Integer::valueOf, x -> x);
		assertEquals(Join.right(1), iter.next());
		assertEquals(Join.both("2", 2), iter.next());
		assertEquals(Join.left("3"), iter.next());
		assertFalse(iter.hasNext());
	}

	@Test
	public void testCloseLeft() {
		var count = new AtomicInteger(0);

		var left = new Iter<Integer>(new Fetch<Integer>() {
			@Override
			protected Holder<Integer> internalNext() {
				return Holder.none();
			}

			@Override
			protected void internalClose() {
				count.incrementAndGet();
			}
		});

		var right = Iter.<Integer>empty();

		left.join(right, x -> x, x -> x).close();
		assertEquals(1, count.get());
	}

	@Test
	public void testCloseRight() {
		var count = new AtomicInteger(0);

		var left = Iter.<Integer>empty();

		var right = new Iter<Integer>(new Fetch<Integer>() {
			@Override
			protected Holder<Integer> internalNext() {
				return Holder.none();
			}

			@Override
			protected void internalClose() {
				count.incrementAndGet();
			}
		});

		left.join(right, x -> x, x -> x).close();
		assertEquals(1, count.get());
	}

}
