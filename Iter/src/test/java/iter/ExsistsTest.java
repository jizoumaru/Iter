package iter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

public class ExsistsTest {
	@Test
	public void testEmpty() {
		assertFalse(Iter.empty().exists());
	}

	@Test
	public void testOne() {
		assertTrue(Iter.of(0).exists());
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

		iter.exists();
		assertEquals(1, count.get());
	}

}
