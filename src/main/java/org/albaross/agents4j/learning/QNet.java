package org.albaross.agents4j.learning;

import java.util.Objects;

import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.Updater;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.deeplearning4j.rl4j.network.dqn.DQN;
import org.deeplearning4j.rl4j.space.Encodable;
import org.deeplearning4j.rl4j.util.Constants;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.lossfunctions.LossFunctions;

public class QNet<S extends Encodable, A> extends DQN implements ValueFunction<S, A> {

	protected final Decoder<A> decoder;

	public QNet(int in, int hidden, int out, int layers, double rate, Decoder<A> decoder) {
		super(createNet(in, hidden, out, layers, rate));
		this.decoder = Objects.requireNonNull(decoder, "decoder must not be null");
	}

	protected static MultiLayerNetwork createNet(int in, int hidden, int out, int layers, double rate) {
		if (layers < 2)
			throw new IllegalArgumentException("network should have at least two layers");

		NeuralNetConfiguration.ListBuilder conf = new NeuralNetConfiguration.Builder() //
				.seed(Constants.NEURAL_NET_SEED) //
				.iterations(1) //
				.optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT) //
				.learningRate(rate) //
				.updater(Updater.ADAM) //
				.weightInit(WeightInit.XAVIER) //
				.list();

		conf.layer(0, new DenseLayer.Builder() //
				.nIn(in) //
				.nOut(hidden) //
				.activation(Activation.RELU) //
				.build());

		for (int i = 1; i < layers - 1; i++) {
			conf.layer(i, new DenseLayer.Builder() //
					.nIn(hidden) //
					.nOut(hidden) //
					.activation(Activation.RELU) //
					.build());
		}

		conf.layer(layers - 1, new OutputLayer.Builder(LossFunctions.LossFunction.MSE) //
				.nIn(hidden) //
				.nOut(out) //
				.activation(Activation.IDENTITY) //
				.build());

		MultiLayerConfiguration mlnconf = conf.pretrain(false).backprop(true).build();
		MultiLayerNetwork model = new MultiLayerNetwork(mlnconf);
		model.init();
		model.setListeners(new ScoreIterationListener(Constants.NEURAL_NET_ITERATION_LISTENER));
		return model;
	}

	@Override
	public A getBestAction(S state) {
		INDArray in = Nd4j.create(state.toArray());
		INDArray out = this.mln.output(in);
		int code = Nd4j.argMax(out, 1).getInt(0);
		return this.decoder.decode(code);
	}

	@Override
	public void update(S state, A action, double reward, S next) {}

}
