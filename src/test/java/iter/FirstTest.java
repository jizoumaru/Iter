package iter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

public class FirstTest {
	@Test
	public void testEmpty() {
		var called = false;

		try {
			Iter.empty().first();
		} catch (NoSuchElementException e) {
			called = true;
		}

		assertTrue(called);
	}

	@Test
	public void testSome() {
		assertEquals("A", Iter.of("A", "B", "C").first());
	}

	@Test
	public void testClose() {
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

		iter.first();
		assertEquals(1, count.get());
	}

}
