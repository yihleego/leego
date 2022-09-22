package io.leego.commons.seq.provider;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.Supplier;

/**
 * @author Leego Yih
 */
public final class Segment implements Iterable<Long>, Serializable {
    @Serial
    private static final long serialVersionUID = -5400688503980153565L;
    /** The beginning index, inclusive. */
    private final long value;
    /** The increment. */
    private final int increment;
    /** The size. */
    private final int size;

    public Segment(long value, int increment, int size) {
        this.value = value;
        this.increment = increment;
        this.size = size;
    }

    public long getValue() {
        return value;
    }

    public int getIncrement() {
        return increment;
    }

    public int getSize() {
        return size;
    }

    public long[] toArray() {
        long[] array = new long[size];
        for (int i = 0; i < size; i++) {
            array[i] = value + (long) increment * i;
        }
        return array;
    }

    public <C extends Collection<Long>> C toCollection(Supplier<C> collectionFactory) {
        C c = collectionFactory.get();
        for (int i = 0; i < size; i++) {
            c.add(value + (long) increment * i);
        }
        return c;
    }

    @Override
    public Iterator<Long> iterator() {
        return new Itr();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Segment segment)) return false;
        if (value != segment.value) return false;
        if (increment != segment.increment) return false;
        return size == segment.size;
    }

    @Override
    public int hashCode() {
        int result = (int) (value ^ (value >>> 32));
        result = 31 * result + increment;
        result = 31 * result + size;
        return result;
    }

    @Override
    public String toString() {
        return "Segment{" + "value=" + value +
                ", increment=" + increment +
                ", size=" + size +
                '}';
    }

    private class Itr implements Iterator<Long> {
        int cursor;

        Itr() {}

        @Override
        public boolean hasNext() {
            return cursor != size;
        }

        @Override
        public Long next() {
            return value + (long) increment * (cursor++);
        }
    }
}
