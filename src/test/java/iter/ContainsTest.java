package iter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

public class ContainsTest {
	@Test
	public void testEmpty() {
		assertFalse(Iter.empty().contains(1));
	}

	@Test
	public void testNotExists() {
		assertFalse(Iter.of(0, 0, 0).contains(1));
	}

	@Test
	public void testExists() {
		assertTrue(Iter.of(0, 1, 0).contains(1));
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

		iter.contains(0);
		assertEquals(1, count.get());
	}

}
