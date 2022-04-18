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
    private final Map<Character, State> success;
    private State failure;
    private Set<String> keywords;

    public State() {
        this(0);
    }

    public State(int depth) {
        this.depth = depth;
        this.success = new HashMap<>();
    }

    public State nextState(char c) {
        return nextState(c, false);
    }

    public State nextState(char c, boolean ignoreCase) {
        State next = success.get(c);
        if (next != null) {
            return next;
        }
        if (ignoreCase) {
            char cc;
            if (Character.isLowerCase(c)) {
                cc = Character.toUpperCase(c);
            } else if (Character.isUpperCase(c)) {
                cc = Character.toLowerCase(c);
            } else {
                cc = c;
            }
            if (c != cc) {
                next = success.get(cc);
            }
        }
        if (next == null && depth == 0) {
            next = this;
        }
        return next;
    }

    public State addState(CharSequence cs) {
        State state = this;
        for (int i = 0; i < cs.length(); i++) {
            state = state.addState(cs.charAt(i));
        }
        return state;
    }

    public State addState(char c) {
        State state = success.get(c);
        if (state == null) {
            state = new State(depth + 1);
            success.put(c, state);
        }
        return state;
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

    public Set<Character> getTransitions() {
        return success.keySet();
    }

    public int getDepth() {
        return depth;
    }

    public boolean isRoot() {
        return depth == 0;
    }
}