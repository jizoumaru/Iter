package iter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

public class FilterTest {
	@Test
	public void testEmpty() {
		assertFalse(Iter.empty().filter(x -> true).exists());
	}

	@Test
	public void testOdd() {
		var iter = Iter.of(0, 1, 2, 3, 4, 5).filter(x -> x % 2 == 1);
		assertEquals(1, iter.next());
		assertEquals(3, iter.next());
		assertEquals(5, iter.next());
		assertFalse(iter.hasNext());
	}

	@Test
	public void testEven() {
		var iter = Iter.of(0, 1, 2, 3, 4, 5).filter(x -> x % 2 == 0);
		assertEquals(0, iter.next());
		assertEquals(2, iter.next());
		assertEquals(4, iter.next());
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

		iter.filter(x -> true).close();
		assertEquals(1, count.get());
	}

}
