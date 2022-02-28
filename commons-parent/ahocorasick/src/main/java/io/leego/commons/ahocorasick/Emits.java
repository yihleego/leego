package io.leego.commons.ahocorasick;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Leego Yih
 */
public class Emits extends ArrayList<Emit> {
    @Serial
    private static final long serialVersionUID = -9117361135147927914L;
    private final String source;

    public Emits(String source) {
        this.source = source;
    }

    public String getSource() {
        return source;
    }

    public List<Token> tokenize() {
        List<Token> tokens = new ArrayList<>();
        if (this.isEmpty()) {
            tokens.add(new Token(source, null));
            return tokens;
        }
        int index = 0;
        Emit last = this.get(this.size() - 1);
        for (Emit emit : this) {
            if (index < emit.getBegin()) {
                tokens.add(new Token(source.substring(index, emit.getBegin()), null));
            }
            tokens.add(new Token(source.substring(emit.getBegin(), emit.getEnd()), emit));
            index = emit.getEnd();
        }
        if (last.getEnd() < source.length()) {
            tokens.add(new Token(source.substring(last.getEnd()), null));
        }
        return tokens;
    }
}
