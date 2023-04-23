package iter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

public class CloseTest {
	@Test
	public void testClose() {
		var count = new AtomicInteger(0);
		
		var iter = new Iter<Void>(new Fetch<Void>() {
			@Override
			protected Holder<Void> internalNext() {
				return Holder.none();
			}
			
			@Override
			protected void internalClose() {
				count.incrementAndGet();
			}
		});
		
		iter.close();
		assertEquals(1, count.get());
	}
}
