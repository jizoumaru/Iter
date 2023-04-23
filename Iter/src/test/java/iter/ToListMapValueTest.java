package iter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

import iter.fetch.src.ArrayFetch;

public class ToListMapValueTest {
	@Test
	public void testEmpty() {
		assertEquals(new LinkedHashMap<Integer, ArrayList<Integer>>(), Iter.<Integer>empty().toListMap(x -> x, x -> x));
	}

	@Test
	public void testSome() {
		var expected = new LinkedHashMap<String, ArrayList<String>>();

		var list1 = new ArrayList<String>();
		expected.put("A", list1);
		list1.add("A");

		var list2 = new ArrayList<String>();
		expected.put("B", list2);
		list2.add("B");
		list2.add("C");

		var list3 = new ArrayList<String>();
		expected.put("C", list3);
		list3.add("D");
		list3.add("E");
		list3.add("F");

		assertEquals(expected, Iter.of("AA", "BB", "CD", "BC", "CE", "CF").toListMap(x -> x.substring(0, 1), x -> x.substring(1, 2)));
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

		iter.toListMap(x -> x, x -> x);
		assertEquals(1, count.get());
	}

}
