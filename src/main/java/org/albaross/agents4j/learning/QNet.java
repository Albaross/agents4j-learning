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
import org.nd4j.linalg.api.ops.impl.indexaccum.IMax;
import org.nd4j.linalg.factory.Nd4j;

public class QNet<S, A> {

	protected static final Random RND = new Random();
	protected MultiLayerNetwork net;

	protected StateEncoder<S> stateEncoder;
	protected ActionEncoder<A> actionEncoder;
	protected ActionDecoder<A> actionDecoder;

	public QNet(MultiLayerNetwork net, StateEncoder<S> stateEncoder, ActionEncoder<A> actionEncoder, ActionDecoder<A> actionDecoder) {
		this.net = Objects.requireNonNull(net, "net must not be null");
		this.stateEncoder = Objects.requireNonNull(stateEncoder, "state encoder must not be null");
		this.actionEncoder = Objects.requireNonNull(actionEncoder, "action encoder must not be null");
		this.actionDecoder = Objects.requireNonNull(actionDecoder, "action decoder must not be null");
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
		double max = !terminal ? maxVal(net.output(stateEncoder.encodeState(next))).getDouble(0) : 0;
		put(state, action, calcWeight(reward, gamma, max));
	}

	public void updateBatch(Collection<Experience<S, A>> batch, double gamma) {
		if (batch.isEmpty())
			return;

		INDArray in = stateEncoder.encodeStates(batch);
		INDArray out = net.output(in);
		INDArray nextIn = stateEncoder.encodeNext(batch);
		INDArray nextOut = net.output(nextIn);
		INDArray max = maxVal(nextOut);

		int row = 0;
		for (Experience<S, A> exp : batch) {
			double weight = calcWeight(exp.getReward(), gamma, (!exp.terminal ? max.getDouble(row) : 0));
			out.putScalar(row, actionEncoder.encode(exp.getAction()), weight);
			row++;
		}

		net.fit(in, out);
	}

	public double get(S state, A action) {
		return net.output(stateEncoder.encodeState(state)).getDouble(actionEncoder.encode(action));
	}

	public void put(S state, A action, double weight) {
		INDArray in = stateEncoder.encodeState(state);
		INDArray out = net.output(in);
		int row = actionEncoder.encode(action);
		out.putScalar(row, weight);
		net.fit(in, out);
	}

	public A getBestAction(S state) {
		INDArray in = net.output(stateEncoder.encodeState(state));
		return actionDecoder.decode(maxIdx(in).getInt(0));
	}

	protected INDArray maxVal(INDArray in) {
		return Nd4j.getExecutioner().exec(new Max(in), 1);
	}

	protected INDArray maxIdx(INDArray in) {
		return Nd4j.getExecutioner().exec(new IMax(in), 1);
	}

	protected double calcWeight(double reward, double gamma, double max) {
		return 0.01 * reward + gamma * max;
	}

}
