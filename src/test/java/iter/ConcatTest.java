package iter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

public class ConcatTest {
	@Test
	public void testEmpty() {
		var iter = Iter.empty().concat(Iter.empty());
		assertFalse(iter.hasNext());
	}

	@Test
	public void testLeft() {
		var iter = Iter.of(1).concat(Iter.empty());
		assertEquals(1, iter.next());
		assertFalse(iter.hasNext());
	}

	@Test
	public void testRight() {
		var iter = Iter.empty().concat(Iter.of(1));
		assertEquals(1, iter.next());
		assertFalse(iter.hasNext());
	}

	@Test
	public void testBoth() {
		var iter = Iter.of(1, 2).concat(Iter.of(3, 4));
		assertEquals(1, iter.next());
		assertEquals(2, iter.next());
		assertEquals(3, iter.next());
		assertEquals(4, iter.next());
		assertFalse(iter.hasNext());
	}
	
	@Test
	public void testCloseFirst() {
		var count = new AtomicInteger(0);

		var first = new Iter<String>(new Fetch<String>() {
			@Override
			protected Holder<String> internalNext() {
				return Holder.none();
			}

			@Override
			protected void internalClose() {
				count.incrementAndGet();
			}
		});

		var second = Iter.<String>of();
		
		first.concat(second).close();
		assertEquals(1, count.get());
	}

	@Test
	public void testCloseSecond() {
		var count = new AtomicInteger(0);

		var first = Iter.<String>of();
		
		var second = new Iter<String>(new Fetch<String>() {
			@Override
			protected Holder<String> internalNext() {
				return Holder.none();
			}

			@Override
			protected void internalClose() {
				count.incrementAndGet();
			}
		});

		first.concat(second).close();
		assertEquals(1, count.get());
	}

}
