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
    private final State root = new State();
    private boolean caseInsensitive = false;

    private Trie() {
    }

    public Emits find(String text) {
        Emits emits = new Emits(text);
        State current = root;
        for (int i = 0; i < text.length(); i++) {
            char c = caseInsensitive ? Character.toLowerCase(text.charAt(i)) : text.charAt(i);
            current = nextState(current, c);
            Set<String> keywords = current.getKeywords();
            for (String keyword : keywords) {
                emits.add(new Emit(i - keyword.length() + 1, i + 1, keyword));
            }
        }
        return emits;
    }

    public Emit findFirst(String text) {
        State current = root;
        for (int i = 0; i < text.length(); i++) {
            char c = caseInsensitive ? Character.toLowerCase(text.charAt(i)) : text.charAt(i);
            current = nextState(current, c);
            Set<String> keywords = current.getKeywords();
            for (String keyword : keywords) {
                return new Emit(i - keyword.length() + 1, i + 1, keyword);
            }
        }
        return null;
    }

    public boolean contains(String text) {
        return findFirst(text) != null;
    }

    private State nextState(State current, Character character) {
        State next = current.nextState(character);
        while (next == null) {
            current = current.getFailure();
            next = current.nextState(character);
        }
        return next;
    }

    private void init(Set<String> keywords) {
        keywords.stream()
                .filter(keyword -> keyword != null && !keyword.isEmpty())
                .forEach(keyword -> root.addState(caseInsensitive ? keyword.toLowerCase() : keyword).addKeyword(keyword));
        Queue<State> queue = new LinkedList<>();
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

    private void setCaseInsensitive(boolean caseInsensitive) {
        this.caseInsensitive = caseInsensitive;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private boolean caseInsensitive = false;
        private final Set<String> keywords = new HashSet<>();

        public Builder caseInsensitive() {
            this.caseInsensitive = true;
            return this;
        }

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
            Trie trie = new Trie();
            trie.setCaseInsensitive(caseInsensitive);
            trie.init(keywords);
            return trie;
        }
    }
}
