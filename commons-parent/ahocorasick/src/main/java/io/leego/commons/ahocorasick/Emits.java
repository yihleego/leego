package io.leego.commons.ahocorasick;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiPredicate;

/**
 * @author Leego Yih
 */
public class Emits extends ArrayList<Emit> {
    @Serial
    private static final long serialVersionUID = -9117361135147927914L;
    private final CharSequence source;

    public Emits(CharSequence source) {
        this.source = source;
    }

    public CharSequence getSource() {
        return source;
    }

    public List<Token> tokenize() {
        this.removeContains();
        String source = this.source.toString();
        List<Token> tokens = new ArrayList<>(this.size() * 2 + 1);
        if (this.isEmpty()) {
            tokens.add(new Token(source, null));
            return tokens;
        }
        int index = 0;
        for (Emit emit : this) {
            if (index < emit.getBegin()) {
                tokens.add(new Token(source.substring(index, emit.getBegin()), null));
            }
            tokens.add(new Token(source.substring(emit.getBegin(), emit.getEnd()), emit));
            index = emit.getEnd();
        }
        Emit last = this.get(this.size() - 1);
        if (last.getEnd() < source.length()) {
            tokens.add(new Token(source.substring(last.getEnd()), null));
        }
        return tokens;
    }

    public String replaceWith(String replacement) {
        this.removeContains();
        String source = this.source.toString();
        if (this.isEmpty()) {
            return source;
        }
        int index = 0;
        StringBuilder sb = new StringBuilder();
        for (Emit emit : this) {
            if (index < emit.getBegin()) {
                sb.append(source, index, emit.getBegin());
                index = emit.getBegin();
            }
            sb.append(mask(replacement, index, emit.getEnd()));
            index = emit.getEnd();
        }
        Emit last = this.get(this.size() - 1);
        if (last.getEnd() < source.length()) {
            sb.append(source, last.getEnd(), source.length());
        }
        return sb.toString();
    }

    public void removeOverlaps() {
        removeIf(Emit::overlaps);
    }

    public void removeContains() {
        removeIf(Emit::contains);
    }

    private void removeIf(BiPredicate<Emit, Emit> predicate) {
        if (this.size() <= 1) {
            return;
        }
        this.sort();
        Iterator<Emit> iterator = this.iterator();
        Emit emit = iterator.next();
        while (iterator.hasNext()) {
            Emit next = iterator.next();
            if (predicate.test(emit, next)) {
                iterator.remove();
            } else {
                emit = next;
            }
        }
    }

    private void sort() {
        this.sort((a, b) -> {
            if (a.getBegin() != b.getBegin()) {
                return Integer.compare(a.getBegin(), b.getBegin());
            } else {
                return Integer.compare(b.getEnd(), a.getEnd());
            }
        });
    }

    private String mask(String replacement, int begin, int end) {
        int count = end - begin;
        int len = replacement != null ? replacement.length() : 0;
        if (len == 0) {
            return "*".repeat(count);
        } else if (len == 1) {
            return replacement.repeat(count);
        } else {
            char[] chars = new char[count];
            for (int i = 0; i < count; i++) {
                chars[i] = replacement.charAt((i + begin) % len);
            }
            return new String(chars);
        }
    }
}
