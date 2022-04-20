package io.leego.commons.ahocorasick;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

/**
 * @author Leego Yih
 */
public class Trie implements Serializable {
    @Serial
    private static final long serialVersionUID = 7464998650081881647L;
    private final State root;

    public Trie(Set<String> keywords) {
        this.root = new State(0);
        this.init(keywords);
    }

    public Emits findAll(CharSequence text, boolean ignoreCase) {
        Emits emits = new Emits(text);
        State state = root;
        for (int i = 0, len = text.length(); i < len; i++) {
            state = nextState(state, text.charAt(i), ignoreCase);
            for (String keyword : state.getKeywords()) {
                emits.add(new Emit(i - keyword.length() + 1, i + 1, keyword));
            }
        }
        return emits;
    }

    public Emits findAll(CharSequence text) {
        return findAll(text, false);
    }

    public Emits findAllIgnoreCase(CharSequence text) {
        return findAll(text, true);
    }

    public Emit findFirst(CharSequence text, boolean ignoreCase) {
        State state = root;
        for (int i = 0, len = text.length(); i < len; i++) {
            state = nextState(state, text.charAt(i), ignoreCase);
            for (String keyword : state.getKeywords()) {
                return new Emit(i - keyword.length() + 1, i + 1, keyword);
            }
        }
        return null;
    }

    public Emit findFirst(CharSequence text) {
        return findFirst(text, false);
    }

    public Emit findFirstIgnoreCase(CharSequence text) {
        return findFirst(text, true);
    }

    private State nextState(State state, char c, boolean ignoreCase) {
        State next = state.nextState(c, ignoreCase);
        while (next == null) {
            state = state.getFailure();
            next = state.nextState(c, ignoreCase);
        }
        return next;
    }

    private void init(Set<String> keywords) {
        for (String keyword : keywords) {
            if (keyword != null && !keyword.isEmpty()) {
                root.addState(keyword).addKeyword(keyword);
            }
        }
        Queue<State> states = new LinkedList<>();
        root.getSuccess().forEach((ignored, state) -> {
            state.setFailure(root);
            states.add(state);
        });
        while (!states.isEmpty()) {
            State state = states.poll();
            state.getSuccess().forEach((c, next) -> {
                State f = state.getFailure();
                State fn = f.nextState(c);
                while (fn == null) {
                    f = f.getFailure();
                    fn = f.nextState(c);
                }
                next.setFailure(fn);
                next.addKeywords(fn.getKeywords());
                states.add(next);
            });
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final Set<String> keywords = new HashSet<>();

        public Builder addKeyword(String keyword) {
            if (keyword != null) {
                this.keywords.add(keyword);
            }
            return this;
        }

        public Builder addKeywords(String... keywords) {
            if (keywords != null) {
                Collections.addAll(this.keywords, keywords);
            }
            return this;
        }

        public Builder addKeywords(Collection<String> keywords) {
            if (keywords != null) {
                this.keywords.addAll(keywords);
            }
            return this;
        }

        public Trie build() {
            return new Trie(keywords);
        }
    }
}
