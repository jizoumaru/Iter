package iter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

public class LeadOrDefaultTest {
	@Test
	public void testEmpty() {
		var iter = Iter.empty().lead(1, -1);
		assertFalse(iter.hasNext());
	}

	@Test
	public void testSome() {
		var iter = Iter.of(1, 2, 3, 4, 5, 6).lead(3, -1);
		assertEquals(new ValueLead<Integer>(1, 4), iter.next());
		assertEquals(new ValueLead<Integer>(2, 5), iter.next());
		assertEquals(new ValueLead<Integer>(3, 6), iter.next());
		assertEquals(new ValueLead<Integer>(4, -1), iter.next());
		assertEquals(new ValueLead<Integer>(5, -1), iter.next());
		assertEquals(new ValueLead<Integer>(6, -1), iter.next());
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

		iter.lead(1, -1).close();
		assertEquals(1, count.get());
	}

}
