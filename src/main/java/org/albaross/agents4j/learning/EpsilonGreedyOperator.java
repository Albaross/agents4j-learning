package org.albaross.agents4j.learning;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.albaross.agents4j.core.ActionOperator;
import org.albaross.agents4j.core.DataComponent;
import org.albaross.agents4j.core.OperationParam;
import org.albaross.agents4j.core.ValueComponent;

@FunctionalInterface
public interface EpsilonGreedyOperator<P, A> extends ActionOperator<P, A>, Randomizer<A> {

	static final Random RND = new Random();

	default A generate(OperationParam<P, A> param) {
		P perception = param.getPerception();

		if (RND.nextDouble() <= getExploreRate())
			return randomAction();

		@SuppressWarnings("unchecked")
		A action = (A) param.getComponent(ValueComponent.class).getBestAction(perception);

		return action != null ? action : randomAction();
	}

	default double getExploreRate() {
		return 0.1;
	}

	@Override
	default Set<Class<? extends DataComponent>> getRequired() {
		Set<Class<? extends DataComponent>> required = new HashSet<>();
		required.add(ValueComponent.class);
		return required;
	}

}
