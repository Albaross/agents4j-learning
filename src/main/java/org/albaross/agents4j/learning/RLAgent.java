package org.albaross.agents4j.learning;

import org.albaross.agents4j.core.Agent;

public interface RLAgent<S, A> extends Agent<S, A> {

	/**
	 * Calculates and updates the weight of an executed action.
	 * 
	 * @param state
	 * @param action
	 * @param reward
	 * @param next
	 */
	void update(S state, A action, double reward, S next);

}
