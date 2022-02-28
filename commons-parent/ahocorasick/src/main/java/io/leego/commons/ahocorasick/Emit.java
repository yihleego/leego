package io.leego.commons.ahocorasick;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author Leego Yih
 */
public class Emit implements Serializable {
    @Serial
    private static final long serialVersionUID = -8879895979621579720L;
    /** The beginning index, inclusive. */
    private final int begin;
    /** The ending index, exclusive. */
    private final int end;
    private final String keyword;

    public Emit(int begin, int end, String keyword) {
        this.begin = begin;
        this.end = end;
        this.keyword = keyword;
    }

    public int getBegin() {
        return begin;
    }

    public int getEnd() {
        return end;
    }

    public int getLength() {
        return end - begin;
    }

    public String getKeyword() {
        return keyword;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Emit))
            return false;
        Emit emit = (Emit) o;
        return begin == emit.begin
                && end == emit.end
                && Objects.equals(keyword, emit.keyword);
    }

    @Override
    public int hashCode() {
        return Objects.hash(begin, end, keyword);
    }

    @Override
    public String toString() {
        return begin + ":" + end + "=" + keyword;
    }
}
