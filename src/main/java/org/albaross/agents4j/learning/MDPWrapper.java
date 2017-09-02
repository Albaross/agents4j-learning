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
		return env().createPerception(getAgentId());
	}

	@Override
	default void close() {}

	@Override
	default StepReply<S> step(Integer action) {
		int agentId = getAgentId();
		env().runEnvironment();
		env().executeAction(agentId, decode(action));
		S next = env().createPerception(agentId);
		double reward = env().getReward(agentId);
		env().cumulate(agentId, reward);
		boolean done = isDone();
		JSONObject info = new JSONObject();
		return new StepReply<>(next, reward, done, info);
	}

	@Override
	default boolean isDone() {
		return env().terminationCriterion(getAgentId());
	}

	RLEnvironment<S, A> env();

	A decode(Integer action);

	int getAgentId();

}
