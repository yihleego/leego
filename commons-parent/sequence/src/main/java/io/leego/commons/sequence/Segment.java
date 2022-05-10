package io.leego.commons.sequence;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * @author Leego Yih
 */
public class Segment implements Serializable {
    @Serial
    private static final long serialVersionUID = -5400688503980153565L;
    private long begin;
    private long end;
    private int increment;

    public Segment() {
    }

    public Segment(long begin, long end, int increment) {
        this.begin = begin;
        this.end = end;
        this.increment = increment;
    }

    public <C extends Collection<Long>> C toCollection(Supplier<C> collectionFactory) {
        return toCollection(collectionFactory.get());
    }

    public <C extends Collection<Long>> C toCollection(C collection) {
        long diff = end - begin;
        if (diff < 0 || diff % increment != 0) {
            throw new IllegalArgumentException("Illegal values: begin=" + begin + ", end=" + end + ", increment=" + increment);
        }
        for (long i = begin; i <= end; i += increment) {
            collection.add(i);
        }
        return collection;
    }

    public long getBegin() {
        return begin;
    }

    public void setBegin(long begin) {
        this.begin = begin;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public int getIncrement() {
        return increment;
    }

    public void setIncrement(int increment) {
        this.increment = increment;
    }

    @Override
    public String toString() {
        return "Segment{" + "begin=" + begin +
                ", end=" + end +
                ", increment=" + increment +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Segment segment))
            return false;
        return begin == segment.begin &&
                end == segment.end &&
                increment == segment.increment;
    }

    @Override
    public int hashCode() {
        return Objects.hash(begin, end, increment);
    }
}
