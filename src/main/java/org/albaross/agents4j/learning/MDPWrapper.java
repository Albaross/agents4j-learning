package org.albaross.agents4j.learning;

import java.util.Arrays;
import java.util.Objects;

import org.deeplearning4j.gym.StepReply;
import org.deeplearning4j.rl4j.mdp.MDP;
import org.deeplearning4j.rl4j.space.ArrayObservationSpace;
import org.deeplearning4j.rl4j.space.DiscreteSpace;
import org.deeplearning4j.rl4j.space.ObservationSpace;
import org.json.JSONObject;

public class MDPWrapper<S, A> implements MDP<S, Integer, DiscreteSpace> {

	private final RLEnvironment<S, A> env;
	private final ObservationSpace<S> observations;
	private final DiscreteSpace actions;
	private final Decoder<A> decoder;

	public MDPWrapper(RLEnvironment<S, A> env, int netIn, int actions, Decoder<A> decoder) {
		this(env, new ArrayObservationSpace<>(new int[] { netIn }), new DiscreteSpace(actions), decoder);
	}

	public MDPWrapper(RLEnvironment<S, A> env, ObservationSpace<S> observations, DiscreteSpace actions, Decoder<A> decoder) {
		this.env = Objects.requireNonNull(env, "environment must not be null");
		this.observations = Objects.requireNonNull(observations, "observations must not be null");
		this.actions = Objects.requireNonNull(actions, "actions must not be null");
		this.decoder = Objects.requireNonNull(decoder, "decoder must not be null");
	}

	@Override
	public S reset() {
		this.env.reboot();
		env.tick();
		env.runEnvironment();
		return this.env.createPerception(0);
	}

	@Override
	public StepReply<S> step(Integer actionNr) {
		A action = decoder.decode(actionNr);
		env.executeAction(0, action);
		double reward = env.getReward(0);
		env.cumulate(0, reward);
		boolean done = isDone();
		env.tick();
		env.runEnvironment();
		S next = env.createPerception(0);
		return new StepReply<>(next, reward, done, new JSONObject("{}"));
	}

	@Override
	public boolean isDone() {
		return this.env.terminationCriterion(0);
	}

	@Override
	public ObservationSpace<S> getObservationSpace() {
		return observations;
	}

	@Override
	public DiscreteSpace getActionSpace() {
		return actions;
	}

	@Override
	public MDP<S, Integer, DiscreteSpace> newInstance() {
		return new MDPWrapper<>(env, observations, actions, decoder);
	}

	@Override
	public void close() {}

	@Override
	public String toString() {
		return Arrays.toString(env.cumulative);
	}

}
