package org.albaross.agents4j.learning;

import java.util.List;
import java.util.Map;

import org.albaross.agents4j.core.Agent;
import org.albaross.agents4j.core.common.BasicEnvironment;

public abstract class RLEnvironment<S, A> extends BasicEnvironment<S, A> {

	protected S[] currentState;

	protected S start;
	protected S goal;

	protected Map<S, Double> rewards;

	public RLEnvironment(List<Agent<S, A>> agents, Map<S, Double> rewards, S start, S goal) {
		super(agents);
		this.currentState = ara(agents.size());
		this.start = start;
		this.goal = goal;
		this.rewards = rewards;
		reboot();
	}

	public void reboot() {
		for (int a = 0; a < currentState.length; a++)
			currentState[a] = start;
	}

	protected abstract S[] ara(int size);

	@Override
	public S createPerception(long agentId) {
		return currentState[(int) agentId];
	}

	@SuppressWarnings("unchecked")
	@Override
	public void executeAction(long agentId, A action) {
		S current = currentState[(int) agentId];
		S next = nextState(current, action);

		double reward = getReward(next);

		currentState[(int) agentId] = next;
		Agent<S, A> agent = agents.get((int) agentId);
		if (agent instanceof ReinforcementLearner<?, ?>)
			((ReinforcementLearner<S, A>) agent).update(current, action, reward, next);
	}

	protected double getReward(S next) {
		double reward = -1;

		if (!next.equals(goal)) {
			if (rewards.containsKey(next))
				reward = rewards.get(next);
		} else {
			reward = 0;
		}

		return reward;
	}

	public abstract S nextState(S current, A action);

	@Override
	public void runEnvironment() {}

	@Override
	public boolean terminationCriterion(long agentId) {
		return currentState[(int) agentId].equals(goal);
	}

}
