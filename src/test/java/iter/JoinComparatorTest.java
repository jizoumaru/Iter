package iter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

public class JoinComparatorTest {
	@Test
	public void testEmpty() {
		var l = Iter.<String>empty();
		var r = Iter.<String>empty();

		var iter = l.join(r, Integer::valueOf, Integer::valueOf, Integer::compare);

		assertFalse(iter.hasNext());
	}

	@Test
	public void testLR() {
		var l = Iter.of("Ax", "Bx");
		var r = Iter.of("xb", "xc");

		var iter = l.join(r, x -> x.substring(0, 1), x -> x.substring(1), String::compareToIgnoreCase);
		assertEquals(Join.left("Ax"), iter.next());
		assertEquals(Join.both("Bx", "xb"), iter.next());
		assertEquals(Join.right("xc"), iter.next());
		assertFalse(iter.hasNext());
	}

	@Test
	public void testRL() {
		var l = Iter.of("xb", "xc");
		var r = Iter.of("Ax", "Bx");

		var iter = l.join(r, x -> x.substring(1), x -> x.substring(0, 1), String::compareToIgnoreCase);
		assertEquals(Join.right("Ax"), iter.next());
		assertEquals(Join.both("xb", "Bx"), iter.next());
		assertEquals(Join.left("xc"), iter.next());
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

		left.join(right, x -> x, x -> x, Integer::compare).close();
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

		left.join(right, x -> x, x -> x, Integer::compare).close();
		assertEquals(1, count.get());
	}

}
