package org.albaross.agents4j.learning;

import java.util.Objects;

import org.albaross.agents4j.core.BasicEnvironment;
import org.deeplearning4j.gym.StepReply;
import org.deeplearning4j.rl4j.mdp.MDP;
import org.deeplearning4j.rl4j.space.ArrayObservationSpace;
import org.deeplearning4j.rl4j.space.DiscreteSpace;
import org.deeplearning4j.rl4j.space.ObservationSpace;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MDPWrapper<S, A> implements MDP<S, Integer, DiscreteSpace> {

	private final Logger LOG = LoggerFactory.getLogger(MDPWrapper.class);

	private final BasicEnvironment<S, A> env;
	private final ObservationSpace<S> observations;
	private final DiscreteSpace actions;
	private final ActionDecoder<A> decoder;

	public MDPWrapper(BasicEnvironment<S, A> env, int netIn, int actions, ActionDecoder<A> decoder) {
		this(env, new ArrayObservationSpace<>(new int[] { netIn }), new DiscreteSpace(actions), decoder);
	}

	public MDPWrapper(BasicEnvironment<S, A> env, ObservationSpace<S> observations, DiscreteSpace actions, ActionDecoder<A> decoder) {
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
		S state = this.env.createPerception(0);
		LOG.info("<tick {}> agent perceives {}", env.getCurrentTick(), state);
		return state;
	}

	@Override
	public StepReply<S> step(Integer actionNr) {
		A action = decoder.decode(actionNr);
		env.executeAction(0, action);
		double reward = env.getReward(0);
		LOG.info("<tick {}> agent executes {} and receives {}", env.getCurrentTick(), action, reward);

		env.cumulate(0, reward);
		boolean done = isDone();
		env.tick();
		env.runEnvironment();

		S next = env.createPerception(0);
		LOG.info("<tick {}> agent perceives {}", env.getCurrentTick(), next);
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
		return String.valueOf(env.getCumulative(0));
	}

}
