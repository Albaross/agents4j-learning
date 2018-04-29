package org.albaross.agents4j.learning;

import lombok.Data;

@Data
public class Experience<S, A> {

    private final S state;
    private final A action;
    private final double reward;
    private final S next;
    private final boolean terminal;

    @Override
    public String toString() {
        return "{" + state + ", " + action + ", " + reward + ", " + next + ", " + terminal + "}";
    }
}
