package org.albaross.agents4j.learning;

import java.util.List;
import java.util.Map;

import org.albaross.agents4j.core.common.BasicAgent;
import org.albaross.agents4j.core.common.DataComponent;
import org.albaross.agents4j.core.common.Operator;

public class BasicRLAgent<S, A> extends BasicAgent<S, A> implements RLAgent<S, A> {

	protected BasicRLAgent(Map<Class<? extends DataComponent>, DataComponent> components, List<Operator<S, A>> agentCycle) {
		super(components, agentCycle);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void update(S state, A action, double reward, S next) {
		getComponent(ValueFunction.class).update(state, action, reward, next);
	}

}
