package iter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

public class FirstOrDefaultTest {
	@Test
	public void testEmpty() {
		assertEquals("X", Iter.empty().firstOrDefault("X"));
	}

	@Test
	public void testSome() {
		assertEquals("A", Iter.of("A", "B", "C").firstOrDefault("X"));
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

		iter.firstOrDefault(-1);
		assertEquals(1, count.get());
	}

}
