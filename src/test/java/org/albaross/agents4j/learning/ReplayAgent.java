package org.albaross.agents4j.learning;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import org.albaross.agents4j.core.Agent;

public class ReplayAgent<S, A> implements Agent<S, A> {

	private double alpha = 0.1;
	private double gamma = 0.9;

	private S keepState;
	private A keepAction;
	private double keepReward;

	private final Random RND = new Random();
	private final Randomizer<A> randomizer;
	private final QTable<S, A> table = new QTable<>();
	private final int replayCapacity;
	private final int epsilonAnnealing;

	private List<Experience<S, A>> replay;
	private int steps = 0;

	private List<Experience<S, A>> lastSequence = Collections.emptyList();

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
		if (keepState != null)
			replay.add(0, new Experience<>(keepState, keepAction, keepReward, perception, false));

		double epsilon = getEpsilon();
		steps++;

		if (RND.nextDouble() <= epsilon)
			return randomizer.randomAction();

		A action = table.getBestAction(perception);
		if (action == null)
			action = randomizer.randomAction();

		this.keepState = perception;
		this.keepAction = action;

		return action;
	}

	private double getEpsilon() {
		return Math.min(Math.max(1.0 - (double) steps / epsilonAnnealing, 0.1), 1.0);
	}

	@Override
	public void reward(double reward, boolean terminal) {
		this.keepReward = reward;

		if (terminal) {
			replay.add(0, new Experience<>(keepState, keepAction, reward, null, terminal));
			learn();
			this.keepState = null;
			this.keepAction = null;
			this.keepReward = 0;
		}
	}

	public void learn() {
		for (Experience<S, A> exp : replay)
			table.update(exp.getState(), exp.getAction(), exp.getReward(), exp.getNext(), exp.isTerminal(), alpha, gamma);

		this.lastSequence = replay;

		this.replay = new ArrayList<>(replayCapacity);
	}

	public List<Experience<S, A>> getLastSequence() {
		return lastSequence;
	}

	public QTable<S, A> getTable() {
		return table;
	}

}
