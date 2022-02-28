package io.leego.commons.ahocorasick;

import java.io.Serial;
import java.io.Serializable;

public class Token implements Serializable {
    @Serial
    private static final long serialVersionUID = -7918430275428907853L;
    private final String fragment;
    private final Emit emit;

    public Token(String fragment, Emit emit) {
        this.fragment = fragment;
        this.emit = emit;
    }

    public String getFragment() {
        return this.fragment;
    }

    public Emit getEmit() {
        return emit;
    }

    public boolean isMatched() {
        return emit != null;
    }
}
