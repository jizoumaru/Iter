package iter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

public class CountTest {
	@Test
	public void testEmpty() {
		assertEquals(0L, Iter.empty().count());
	}

	@Test
	public void testThree() {
		assertEquals(3L, Iter.of("A", "B", "C").count());
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

		iter.count();
		assertEquals(1, count.get());
	}

}
