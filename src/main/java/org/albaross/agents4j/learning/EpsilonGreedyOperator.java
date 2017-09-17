package org.albaross.agents4j.learning;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.albaross.agents4j.core.common.ActionOperator;
import org.albaross.agents4j.core.common.DataComponent;
import org.albaross.agents4j.core.common.OperationParam;

@FunctionalInterface
public interface EpsilonGreedyOperator<P, A> extends ActionOperator<P, A>, Randomizer<A> {

	static final Random RND = new Random();

	default A generate(OperationParam<P, A> param) {
		P perception = param.getPerception();

		if (RND.nextDouble() <= getExploreRate())
			return randomAction();

		@SuppressWarnings("unchecked")
		A action = (A) param.getComponent(ValueFunction.class).getBestAction(perception);

		return action != null ? action : randomAction();
	}

	default double getExploreRate() {
		return 0.1;
	}

	@Override
	default Set<Class<? extends DataComponent>> getRequired() {
		Set<Class<? extends DataComponent>> required = new HashSet<>();
		required.add(ValueFunction.class);
		return required;
	}

}
