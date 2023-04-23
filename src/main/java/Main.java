
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import iter.Fetch;
import iter.Holder;
import iter.Iter;

public class Main {
	public static void main(String[] args) {
		var c = Iter.range(1, 100)
			.flatMap(x -> Iter.repeat(x, x))
			.count();
		
		System.out.println(c);
	}

	static Iter<String> lines(Path file) {
		return new Iter<String>(new Fetch<String>() {
			BufferedReader reader;

			@Override
			protected Holder<String> internalNext() {
				try {
					if (reader == null) {
						reader = Files.newBufferedReader(file);
					}
					var line = reader.readLine();
					return line != null ? Holder.of(line) : Holder.none();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}

			@Override
			protected void internalClose() {
				try (var _reader = reader) {
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		});
	}

	static Iter<Path> rec(Path file) {
		return list(file).flatMap(x -> rec(x)).append(file);
	}

	static Iter<Path> list(Path file) {
		try {
			return Files.isDirectory(file) ? Iter.from(Files.list(file)) : Iter.empty();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	static long times(Runnable task) {
		var a = new long[5];
		for (var i = 0; i < a.length; i++) {
			var start = System.currentTimeMillis();
			task.run();
			var end = System.currentTimeMillis();
			a[i] = end - start;
		}
		Arrays.sort(a);
		return a[a.length / 2];
	}

	static long time(Runnable task) {
		var start = System.currentTimeMillis();
		task.run();
		var end = System.currentTimeMillis();
		return end - start;
	}

}
