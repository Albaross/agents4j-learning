package org.albaross.agents4j.learning;

import java.util.Collection;
import java.util.Objects;
import java.util.Random;

import org.albaross.agents4j.learning.utils.ActionDecoder;
import org.albaross.agents4j.learning.utils.ActionEncoder;
import org.albaross.agents4j.learning.utils.StateEncoder;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.api.ops.impl.accum.Max;
import org.nd4j.linalg.api.ops.impl.accum.Min;
import org.nd4j.linalg.api.ops.impl.indexaccum.IMax;
import org.nd4j.linalg.api.ops.impl.indexaccum.IMin;
import org.nd4j.linalg.factory.Nd4j;

public class QNet<S, A> {

	protected static final Random RND = new Random();
	protected MultiLayerNetwork net;

	protected StateEncoder<S> stateEncoder;
	protected ActionEncoder<A> actionEncoder;
	protected ActionDecoder<A> actionDecoder;

	protected final boolean negMode;

	public QNet(MultiLayerNetwork net, StateEncoder<S> sEncoder, ActionEncoder<A> aEncoder, ActionDecoder<A> aDecoder, boolean negMode) {
		this.net = Objects.requireNonNull(net, "net must not be null");
		this.stateEncoder = Objects.requireNonNull(sEncoder, "state encoder must not be null");
		this.actionEncoder = Objects.requireNonNull(aEncoder, "action encoder must not be null");
		this.actionDecoder = Objects.requireNonNull(aDecoder, "action decoder must not be null");
		this.negMode = negMode;
	}

	/**
	 * Calculates and updates the weight of an executed action.
	 * 
	 * @param state
	 * @param action
	 * @param reward
	 * @param next
	 * @param gamma the discount rate
	 */
	public void update(S state, A action, double reward, S next, boolean terminal, double gamma) {
		double best = 0;

		if (!terminal) {
			INDArray out = net.output(stateEncoder.encodeState(next));
			best = terminal ? 0 : Nd4j.getExecutioner().exec(negMode ? new Min(out) : new Max(out), 1).getDouble(0);
		}

		double r = negMode ? -reward : reward;
		double weight = r + gamma * best;
		put(state, action, weight);
	}

	public void updateBatch(Collection<Experience<S, A>> batch, double gamma) {
		if (batch.isEmpty())
			return;

		INDArray states = stateEncoder.encodeStates(batch);
		INDArray next = net.output(stateEncoder.encodeNext(batch));

		INDArray targets = net.output(states);
		INDArray best = Nd4j.getExecutioner().exec(negMode ? new Min(next) : new Max(next), 1);

		int row = 0;
		for (Experience<S, A> exp : batch) {
			double reward = negMode ? -exp.getReward() : exp.getReward();
			double target = reward + (exp.terminal ? 0 : gamma * best.getDouble(row));
			targets.putScalar(row++, actionEncoder.encode(exp.getAction()), target);
		}

		net.fit(states, targets);
	}

	public double get(S state, A action) {
		return net.output(stateEncoder.encodeState(state)).getDouble(actionEncoder.encode(action));
	}

	public void put(S state, A action, double weight) {
		INDArray in = stateEncoder.encodeState(state);
		INDArray out = net.output(in);
		out.putScalar(actionEncoder.encode(action), weight);
		net.fit(in, out);
	}

	public A getBestAction(S state) {
		INDArray in = net.output(stateEncoder.encodeState(state));
		INDArray code = Nd4j.getExecutioner().exec(negMode ? new IMin(in) : new IMax(in), 1);
		return actionDecoder.decode(code.getInt(0));
	}

}
