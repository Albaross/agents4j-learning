package org.albaross.agents4j.learning;

import java.util.Objects;

import org.albaross.agents4j.core.common.BasicAgent;

public class RLWrapper<S, A> implements RLAgent<S, A> {

	protected BasicAgent<S, A> agent;

	public RLWrapper(BasicAgent<S, A> agent) {
		if (agent.getComponent(ValueFunction.class) == null)
			throw new IllegalArgumentException("agent does not have a suitable value-function");

		this.agent = Objects.requireNonNull(agent, "agent must not be null");
	}

	@SuppressWarnings("unchecked")
	@Override
	public void update(S state, A action, double reward, S next) {
		this.agent.getComponent(ValueFunction.class).update(state, action, reward, next);
	}

	@Override
	public A generateAction(S perception) {
		return agent.generateAction(perception);
	}

}
