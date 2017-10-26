package org.albaross.agents4j.learning.operators;

import java.util.HashSet;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

import org.albaross.agents4j.core.ActionOperator;
import org.albaross.agents4j.core.DataComponent;
import org.albaross.agents4j.core.OperationParam;
import org.albaross.agents4j.core.ValueComponent;
import org.albaross.agents4j.learning.utils.ActionRandomizer;

public class EpsilonGreedyOperator<P, A> implements ActionOperator<P, A> {

	protected static final Random RND = new Random();
	protected final ActionRandomizer<A> randomizer;
	protected final double epsilon;

	public EpsilonGreedyOperator(ActionRandomizer<A> randomizer, double epsilon) {
		this.randomizer = Objects.requireNonNull(randomizer, "action randomizer must not be null");
		this.epsilon = epsilon;
	}

	@Override
	public A generate(OperationParam<P, A> param) {
		P perception = param.getPerception();
		double epsilon = getExploreRate(param);

		if (RND.nextDouble() <= epsilon)
			return randomizer.randomAction();

		@SuppressWarnings("unchecked")
		ValueComponent<P, A> values = param.getComponent(ValueComponent.class);

		A action = values.getBestAction(perception);

		return action != null ? action : randomizer.randomAction();
	}

	protected double getExploreRate(OperationParam<P, A> param) {
		return epsilon;
	}

	@Override
	public Set<Class<? extends DataComponent>> getRequired() {
		Set<Class<? extends DataComponent>> required = new HashSet<>();
		required.add(ValueComponent.class);
		return required;
	}

}
