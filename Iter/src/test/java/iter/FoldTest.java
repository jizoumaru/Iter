package iter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

public class FoldTest {
	@Test
	public void testEmpty() {
		assertEquals("seed", Iter.empty().fold("seed", (l, r) -> l + 1));
	}

	@Test
	public void testSome() {
		assertEquals(6, Iter.of(1, 2, 3).fold(0, (l, r) -> l + r));
	}

	@Test
	public void testCollect() {
		assertEquals(Arrays.asList(1, 2, 3), Iter.of(1, 2, 3).fold(new ArrayList<Integer>(), (l, r) -> {
			l.add(r);
			return l;
		}));
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

		iter.fold(0, (l, r) -> l + 1);
		assertEquals(1, count.get());
	}

}
