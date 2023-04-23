package iter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

public class GroupTest {
	@Test
	public void testEmpty() {
		var iter = Iter.empty().group(1);
		assertFalse(iter.hasNext());
	}

	@Test
	public void testSome() {
		var iter = Iter.of(0, 1, 2, 3, 4, 5, 6).group(3);
		assertEquals(Arrays.asList(0, 1, 2), iter.next().toList());
		assertEquals(Arrays.asList(3, 4, 5), iter.next().toList());
		assertEquals(Arrays.asList(6), iter.next().toList());
		assertFalse(iter.hasNext());
	}

	@Test
	public void testSkip() {
		var iter = Iter.range(1, 18).group(3).skip(3).map(x -> x.toList());
		assertEquals(Arrays.asList(10, 11, 12), iter.next());
		assertEquals(Arrays.asList(13, 14, 15), iter.next());
		assertEquals(Arrays.asList(16, 17, 18), iter.next());
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

		iter.group(1).close();
		assertEquals(1, count.get());
	}

}
