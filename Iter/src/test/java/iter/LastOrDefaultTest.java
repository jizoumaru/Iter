package iter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

public class LastOrDefaultTest {
	@Test
	public void testEmpty() {
		assertEquals("X", Iter.empty().lastOrDefault("X"));
	}

	@Test
	public void testSome() {
		assertEquals("C", Iter.of("A", "B", "C").lastOrDefault("X"));
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

		iter.lastOrDefault(-1);
		assertEquals(1, count.get());
	}

}
