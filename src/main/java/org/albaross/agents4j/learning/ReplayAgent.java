package org.albaross.agents4j.learning;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class ReplayAgent<S, A> implements RLAgent<S, A> {

	private final Random RND = new Random();
	private final Randomizer<A> randomizer;
	private final QTable<S, A> table = new QTable<>();
	private final int replayCapacity;
	private final int epsilonAnnealing;

	private List<Experience<S, A>> replay;
	private int steps = 0;

	public ReplayAgent(Randomizer<A> randomizer, int epsilonAnnealing) {
		this(randomizer, 100000, epsilonAnnealing);
	}

	public ReplayAgent(Randomizer<A> randomizer, int replayCapacity, int epsilonAnnealing) {
		this.randomizer = Objects.requireNonNull(randomizer, "randomizer must not be null");
		this.replayCapacity = replayCapacity;
		this.epsilonAnnealing = epsilonAnnealing;
		this.replay = new ArrayList<>(replayCapacity);
	}

	@Override
	public A generateAction(S perception) {
		double epsilon = getEpsilon();
		steps++;

		if (RND.nextDouble() <= epsilon)
			return randomizer.randomAction();

		A action = table.getBestAction(perception);

		return action != null ? action : randomizer.randomAction();
	}

	private double getEpsilon() {
		return Math.min(Math.max(1.0 - (double) steps / epsilonAnnealing, 0.1), 1.0);
	}

	@Override
	public void update(S state, A action, double reward, S next, boolean terminal) {
		replay.add(0, new Experience<>(state, action, reward, next, terminal));

		if (replay.size() >= replayCapacity)
			learn();
	}

	public void learn() {
		for (Experience<S, A> exp : replay)
			table.update(exp.getState(), exp.getAction(), exp.getReward(), exp.getNext(), exp.isTerminal());

		replay = new ArrayList<>(replayCapacity);
	}

	@Override
	public void notifySuccess() {
		learn();
	}

}
