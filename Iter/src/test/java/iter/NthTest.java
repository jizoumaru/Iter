package iter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

import iter.fetch.src.ArrayFetch;

public class NthTest {
	@Test
	public void testEmpty() {
		var iter = Iter.empty().nth(1);
		assertFalse(iter.hasNext());
	}

	@Test
	public void testSame() {
		var iter = Iter.of("A", "B", "C").nth(2);
		assertEquals(new ValueNth<String>("A", "B"), iter.next());
		assertEquals(new ValueNth<String>("B", "B"), iter.next());
		assertEquals(new ValueNth<String>("C", "B"), iter.next());
		assertFalse(iter.hasNext());
	}
	
	@Test
	public void testClose() {
		var count = new AtomicInteger(0);

		var iter = new Iter<Integer>(new Fetch<Integer>() {
			Fetch<Integer> delegate = new ArrayFetch<Integer>(1);

			@Override
			protected Holder<Integer> internalNext() {
				return delegate.next();
			}

			@Override
			protected void internalClose() {
				count.incrementAndGet();
			}
		});

		iter.nth(1).close();
		assertEquals(1, count.get());
	}

}
