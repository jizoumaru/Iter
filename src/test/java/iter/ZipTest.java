package iter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

import iter.fetch.src.ArrayFetch;

public class ZipTest {
	@Test
	public void testEmpty() {
		assertFalse(Iter.empty().zip(Iter.empty()).hasNext());
		assertFalse(Iter.of("A").zip(Iter.empty()).hasNext());
		assertFalse(Iter.empty().zip(Iter.of("A")).hasNext());
	}

	@Test
	public void testSome() {
		var iter = Iter.of("A", "B", "C").zip(Iter.of(1, 2, 3));
		assertEquals(new Zip<String, Integer>("A", 1), iter.next());
		assertEquals(new Zip<String, Integer>("B", 2), iter.next());
		assertEquals(new Zip<String, Integer>("C", 3), iter.next());
		assertFalse(iter.hasNext());
	}

	@Test
	public void testCloseLeft() {
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

		iter.zip(Iter.empty()).close();
		assertEquals(1, count.get());
	}

	@Test
	public void testCloseRight() {
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

		Iter.empty().zip(iter).close();
		assertEquals(1, count.get());
	}

}
