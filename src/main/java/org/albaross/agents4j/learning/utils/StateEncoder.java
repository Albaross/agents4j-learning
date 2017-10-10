package org.albaross.agents4j.learning.utils;

import java.util.Collection;

import org.albaross.agents4j.learning.Experience;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

/**
 * 
 * @author Manuel Barbi
 *
 * @param <S> state
 */
@FunctionalInterface
public interface StateEncoder<S> {

	double[] encode(S state);

	default INDArray encodeState(S state) {
		return Nd4j.create(encode(state));
	}

	default <A> INDArray encodeStates(Collection<Experience<S, A>> batch) {
		double[][] encoded = new double[batch.size()][];
		int row = 0;

		for (Experience<S, A> exp : batch)
			encoded[row++] = encode(exp.getState());

		return Nd4j.create(encoded);
	}

	default <A> INDArray encodeNext(Collection<Experience<S, A>> batch) {
		double[][] encoded = new double[batch.size()][];
		int row = 0;

		for (Experience<S, A> exp : batch)
			encoded[row++] = encode(exp.getNext());

		return Nd4j.create(encoded);
	}

}
