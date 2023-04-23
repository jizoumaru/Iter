package iter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

import iter.fetch.src.ArrayFetch;

public class MergeListTest {
	@Test
	public void testEmtpy() {
		var iter = Iter.merge(new ArrayList<Iter<Integer>>(), x -> x);
		assertFalse(iter.hasNext());
	}

	@Test
	public void testThree() {
		var iters = Iter.from(Iter.of(Iter.of(0, 3), Iter.of(2, 5), Iter.of(1, 4))).toList();
		var iter = Iter.merge(iters, x -> x);
		assertEquals(0, iter.next());
		assertEquals(1, iter.next());
		assertEquals(2, iter.next());
		assertEquals(3, iter.next());
		assertEquals(4, iter.next());
		assertEquals(5, iter.next());
		assertFalse(iter.hasNext());
	}

	@Test
	public void testKey() {
		var iters = Iter.from(Iter.of(Iter.of("a0", "b3"), Iter.of("c2", "d5"), Iter.of("e1", "f4"))).toList();
		var iter = Iter.merge(iters, x -> x.substring(1));
		assertEquals("a0", iter.next());
		assertEquals("e1", iter.next());
		assertEquals("c2", iter.next());
		assertEquals("b3", iter.next());
		assertEquals("f4", iter.next());
		assertEquals("d5", iter.next());
		assertFalse(iter.hasNext());
	}
	
	@Test
	public void testCloseFirst() {
		var count = new AtomicInteger(0);

		var a = new Iter<Integer>(new Fetch<Integer>() {
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

		var iters = new ArrayList<Iter<Integer>>();
		iters.add(a);
		
		Iter.merge(iters, x -> x).close();
		
		assertEquals(1, count.get());
	}

	@Test
	public void testCloseSecond() {
		var count = new AtomicInteger(0);

		var a = new Iter<Integer>(new Fetch<Integer>() {
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

		var iters = new ArrayList<Iter<Integer>>();
		iters.add(Iter.empty());
		iters.add(a);
		
		Iter.merge(iters, x -> x).close();
		
		assertEquals(1, count.get());
	}

}
