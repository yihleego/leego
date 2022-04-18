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
        this.root = new State();
        this.init(keywords);
    }

    public Emits findAll(CharSequence text, boolean ignoreCase) {
        Emits emits = new Emits(text);
        State current = root;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            current = nextState(current, c, ignoreCase);
            Set<String> keywords = current.getKeywords();
            for (String keyword : keywords) {
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
        State current = root;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            current = nextState(current, c, ignoreCase);
            Set<String> keywords = current.getKeywords();
            for (String keyword : keywords) {
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

    private State nextState(State current, char c, boolean ignoreCase) {
        State next = current.nextState(c, ignoreCase);
        while (next == null) {
            current = current.getFailure();
            next = current.nextState(c, ignoreCase);
        }
        return next;
    }

    private void init(Set<String> keywords) {
        Queue<State> queue = new LinkedList<>();
        for (String keyword : keywords) {
            if (keyword == null || keyword.isEmpty()) {
                continue;
            }
            root.addState(keyword).addKeyword(keyword);
        }
        for (State state : root.getStates()) {
            state.setFailure(root);
            queue.add(state);
        }
        while (!queue.isEmpty()) {
            State current = queue.poll();
            for (Character transition : current.getTransitions()) {
                State target = current.nextState(transition);
                queue.add(target);
                State failure = current.getFailure();
                while (failure.nextState(transition) == null) {
                    failure = failure.getFailure();
                }
                State nextFailure = failure.nextState(transition);
                target.setFailure(nextFailure);
                target.addKeywords(nextFailure.getKeywords());
            }
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
