package org.albaross.agents4j.learning;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class ReplayAgent<S, A> implements RLAgent<S, A> {

	private final Random RND = new Random();
	private final QTable<S, A> table = new QTable<>();
	private List<Experience<S, A>> lesson = new LinkedList<>();

	private Randomizer<A> randomizer;
	private double epsilon = 0.1;

	public ReplayAgent(Randomizer<A> randomizer) {
		this.randomizer = Objects.requireNonNull(randomizer, "randomizer must not be null");
	}

	@Override
	public A generateAction(S perception) {
		if (RND.nextDouble() <= epsilon)
			return randomizer.randomAction();

		A action = table.getBestAction(perception);

		return action != null ? action : randomizer.randomAction();
	}

	@Override
	public void update(S state, A action, double reward, S next) {
		lesson.add(0, new Experience<>(state, action, reward, next));

		if (lesson.size() > 100000)
			learn();
	}

	public void learn() {
		for (Experience<S, A> exp : lesson)
			table.update(exp.getState(), exp.getAction(), exp.getReward(), exp.getNext());

		lesson = new LinkedList<>();
	}

	@Override
	public void notifySuccess() {
		learn();
	}

}
