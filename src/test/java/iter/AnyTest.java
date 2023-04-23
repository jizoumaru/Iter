package iter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

public class AnyTest {
	@Test
	public void testEmpty() {
		assertFalse(Iter.<Integer>of().any(x -> x == 0));
	}

	@Test
	public void testSome() {
		assertTrue(Iter.of(1, 0, 1).any(x -> x == 0));
	}

	@Test
	public void testAll() {
		assertFalse(Iter.of(1, 1, 1).all(x -> x == 0));
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

		iter.any(x -> true);
		assertEquals(1, count.get());
	}

}
