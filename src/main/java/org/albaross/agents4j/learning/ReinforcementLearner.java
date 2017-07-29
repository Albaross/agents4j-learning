package org.albaross.agents4j.learning;

public interface ReinforcementLearner<S, A> {

	/**
	 * Calculates and updates the weight of an executed action.
	 * 
	 * @param state
	 * @param action
	 * @param reward
	 * @param nextState
	 */
	void update(S state, A action, double reward, S nextState);

}
