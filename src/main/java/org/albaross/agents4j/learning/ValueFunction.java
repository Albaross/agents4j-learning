package org.albaross.agents4j.learning;

import org.albaross.agents4j.core.common.DataComponent;

public interface ValueFunction<S, A> extends DataComponent {

	A getBestAction(S state);

	void update(S state, A action, double reward, S nextState);

}
