package iter.fetch.tank;

import iter.Fetch;
import iter.Holder;
import iter.ValueTile;

public final class NtileFetch<T> extends Fetch<ValueTile<T>> {
	private final Fetch<T> fetch;
	private final int n;
	private T[] array;
	private int index;
	private Tiles tiles;

	public NtileFetch(Fetch<T> fetch, int n) {
		this.fetch = fetch;
		this.n = n;
	}

	@SuppressWarnings("unchecked")
	@Override
	public final Holder<ValueTile<T>> internalNext() {
		if (array == null) {
			this.array = (T[]) fetch.toArray();
			this.index = 0;
			this.tiles = new Tiles(array.length, n);
		}

		if (index < array.length) {
			var value = array[index];
			array[index] = null;
			index++;

			return Holder.of(new ValueTile<T>(value, tiles.next()));
		} else {
			return Holder.none();
		}
	}

	@Override
	public final void internalClose() {
		try (var _fetch = fetch) {
		}
	}

	private static final class Tiles {
		final int baseValueCount;
		final int extraValueCount;
		int tile;
		int valueCount;
		int valueIndex;

		Tiles(int totalValueCount, int tileCount) {
			this.baseValueCount = totalValueCount / tileCount;
			this.extraValueCount = totalValueCount % tileCount;
			this.tile = 1;
			this.valueCount = baseValueCount + (tile - 1 < extraValueCount ? 1 : 0);
			this.valueIndex = 0;
		}

		int next() {
			if (valueIndex < valueCount) {
				valueIndex++;
			} else {
				tile++;
				valueCount = baseValueCount + (tile - 1 < extraValueCount ? 1 : 0);
				valueIndex = 1;
			}
			return tile;
		}
	}
}
