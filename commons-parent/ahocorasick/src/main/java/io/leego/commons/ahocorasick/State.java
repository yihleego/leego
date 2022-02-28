package io.leego.commons.ahocorasick;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author Leego Yih
 */
public class State implements Serializable {
    @Serial
    private static final long serialVersionUID = -6350361756888572415L;
    private final int depth;
    private final State root;
    private final Map<Character, State> success;
    private State failure;
    private Set<String> keywords;

    public State() {
        this(0);
    }

    public State(int depth) {
        this.depth = depth;
        this.root = depth == 0 ? this : null;
        this.success = new HashMap<>();
    }

    public State nextState(char c) {
        State next = success.get(c);
        if (next == null && root != null) {
            next = root;
        }
        return next;
    }

    public State nextStateIgnoreRoot(char c) {
        return success.get(c);
    }

    public State addState(String keyword) {
        State state = this;
        for (char c : keyword.toCharArray()) {
            state = state.addState(c);
        }
        return state;
    }

    public State addState(char c) {
        State state = nextStateIgnoreRoot(c);
        if (state == null) {
            state = new State(depth + 1);
            success.put(c, state);
        }
        return state;
    }

    public int getDepth() {
        return depth;
    }

    public void addKeyword(String keyword) {
        if (this.keywords == null) {
            this.keywords = new TreeSet<>();
        }
        this.keywords.add(keyword);
    }

    public void addKeywords(Collection<String> keywords) {
        if (this.keywords == null) {
            this.keywords = new TreeSet<>();
        }
        this.keywords.addAll(keywords);
    }

    public Set<String> getKeywords() {
        return keywords != null ? keywords : Collections.emptySet();
    }

    public State getFailure() {
        return failure;
    }

    public void setFailure(State failure) {
        this.failure = failure;
    }

    public Collection<State> getStates() {
        return success.values();
    }

    public Collection<Character> getTransitions() {
        return success.keySet();
    }
}