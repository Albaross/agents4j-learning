package org.albaross.agents4j.learning;

import java.util.HashSet;
import java.util.Set;

import org.albaross.agents4j.core.common.ActionOperator;
import org.albaross.agents4j.core.common.DataComponent;
import org.albaross.agents4j.core.common.OperationParam;

@FunctionalInterface
public interface EpsilonGreedyOperator<P, A> extends ActionOperator<P, A> {

	@Override
	default void process(OperationParam<P, A> param) {
		ActionOperator.super.process(param);
	}

	default A generate(OperationParam<P, A> param) {
		P perception = param.getPerception();

		if (Math.random() <= getExploreRate())
			return randomAction();

		@SuppressWarnings("unchecked")
		A action = (A) param.getComponent(ValueFunction.class).getBestAction(perception);

		return action != null ? action : randomAction();
	}

	default double getExploreRate() {
		return 0.1;
	}

	A randomAction();

	@Override
	default Set<Class<? extends DataComponent>> getRequired() {
		Set<Class<? extends DataComponent>> required = new HashSet<>();
		required.add(ValueFunction.class);
		return required;
	}

}
