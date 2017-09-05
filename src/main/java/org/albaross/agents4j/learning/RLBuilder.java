package org.albaross.agents4j.learning;

import java.util.List;
import java.util.Map;

import org.albaross.agents4j.core.common.BasicBuilder;
import org.albaross.agents4j.core.common.DataComponent;
import org.albaross.agents4j.core.common.Operator;

public class RLBuilder<S, A> extends BasicBuilder<S, A> {

	@Override
	public BasicRLAgent<S, A> getAgent() {
		return (BasicRLAgent<S, A>) super.getAgent();
	}

	@Override
	protected BasicRLAgent<S, A> create(Map<Class<? extends DataComponent>, DataComponent> comps, List<Operator<S, A>> ops) {
		if (!comps.containsKey(ValueFunction.class))
			throw new IllegalStateException("components do not contain a suitable value-function");

		return new BasicRLAgent<>(comps, ops);
	}

}
