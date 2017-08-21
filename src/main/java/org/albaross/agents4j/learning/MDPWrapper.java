package org.albaross.agents4j.learning;

import org.deeplearning4j.gym.StepReply;
import org.deeplearning4j.rl4j.mdp.MDP;
import org.deeplearning4j.rl4j.space.DiscreteSpace;
import org.deeplearning4j.rl4j.space.Encodable;
import org.json.JSONObject;

public interface MDPWrapper<S extends Encodable, A> extends MDP<S, Integer, DiscreteSpace> {

	@Override
	default S reset() {
		env().reboot();
		return env().createPerception(0);
	}

	@Override
	default void close() {}

	@Override
	default StepReply<S> step(Integer action) {
		env().runEnvironment();
		env().executeAction(0, decode(action));
		S next = env().createPerception(0);
		double reward = env().getReward(0);
		boolean done = isDone();
		JSONObject info = new JSONObject();
		return new StepReply<>(next, reward, done, info);
	}

	@Override
	default boolean isDone() {
		return env().terminationCriterion(0);
	}

	RLEnvironment<S, A> env();

	A decode(Integer action);

}
