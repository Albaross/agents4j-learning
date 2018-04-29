package org.albaross.agents4j.learning.components;

import org.albaross.agents4j.core.Container;

public interface ValueComponent<S, A> extends Container {

    void update(S state, A action, double reward, S next, boolean terminal);

    A getBestAction(S state);

}
