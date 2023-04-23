package iter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

public class ForEachTest {
	@Test
	public void testEmpty() {
		var count = new AtomicInteger(0);
		Iter.empty().forEach(x -> count.incrementAndGet());
		assertEquals(0, count.get());
	}

	@Test
	public void testSome() {
		var list = new ArrayList<Integer>();
		Iter.of(1, 2, 3).forEach(list::add);
		assertEquals(Arrays.asList(1, 2, 3), list);
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

		iter.forEach(x -> {});
		assertEquals(1, count.get());
	}

}
