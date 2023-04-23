package iter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

public class AllTest {
	@Test
	public void testEmpty() {
		assertTrue(Iter.<Integer>of().all(x -> x == 1));
	}

	@Test
	public void testSome() {
		assertFalse(Iter.of(1, 0, 1).all(x -> x == 1));
	}

	@Test
	public void testAll() {
		assertTrue(Iter.of(1, 1, 1).all(x -> x == 1));
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

		iter.all(x -> true);
		assertEquals(1, count.get());
	}

}
