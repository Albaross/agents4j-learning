package org.albaross.agents4j.learning.operators;

import org.albaross.agents4j.core.Container;
import org.albaross.agents4j.core.Context;
import org.albaross.agents4j.core.Operator;
import org.albaross.agents4j.learning.components.ValueComponent;
import org.albaross.agents4j.learning.utils.ActionRandomizer;

import java.util.HashSet;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

public class EpsilonGreedyOperator<S, A> implements Operator<S, A> {

    protected static final Random RND = new Random();
    protected final ActionRandomizer<A> randomizer;
    protected final double epsilon;

    public EpsilonGreedyOperator(ActionRandomizer<A> randomizer, double epsilon) {
        this.randomizer = Objects.requireNonNull(randomizer, "action randomizer must not be null");
        this.epsilon = epsilon;
    }

    public void execute(Context<S, A> context) {
        S state = context.getState();
        double epsilon = getExploreRate(context);

        if (RND.nextDouble() <= epsilon) {
            A action = randomizer.randomAction();
            context.setAction(action);
            return;
        }

        ValueComponent<S, A> values = context.getContainer(ValueComponent.class);

        A action = values.getBestAction(state);
        action = action != null ? action : randomizer.randomAction();

        context.setAction(action);
    }

    protected double getExploreRate(Context<S, A> context) {
        return epsilon;
    }

    @Override
    public Set<Class<? extends Container>> getRequired() {
        Set<Class<? extends Container>> required = new HashSet<>();
        required.add(ValueComponent.class);
        return required;
    }

}
