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
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.lossfunctions.LossFunctions;

public class NetBuilder {

	protected final static OptimizationAlgorithm OPTIMIZATION = OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT;

	protected int seed = 123;
	protected double learningRate = 1e-3;
	protected Updater updater = Updater.ADAM;
	protected WeightInit weightInit = WeightInit.XAVIER;
	protected double l2 = 1e-3;
	protected Activation activation = Activation.RELU;

	public MultiLayerNetwork createNet(int... numNodes) {

		int numLayers = numNodes.length - 2;
		int len = numNodes.length - 1;

		if (numLayers < 1)
			throw new IllegalArgumentException("illegal number of layers: " + numLayers);

		NeuralNetConfiguration.ListBuilder confB = new NeuralNetConfiguration.Builder().seed(seed).iterations(1).optimizationAlgo(OPTIMIZATION)
				.learningRate(learningRate).updater(updater).weightInit(weightInit).regularization(l2 > 0).l2(l2).list();

		for (int i = 0; i < numLayers; i++)
			confB.layer(i, new DenseLayer.Builder().nIn(numNodes[i]).nOut(numNodes[i + 1]).activation(activation).build());

		confB.layer(numLayers, new OutputLayer.Builder(LossFunctions.LossFunction.MSE).nIn(numNodes[numLayers]).nOut(numNodes[len])
				.activation(Activation.IDENTITY).build());

		MultiLayerConfiguration mlnconf = confB.pretrain(false).backprop(true).build();
		MultiLayerNetwork model = new MultiLayerNetwork(mlnconf);
		model.init();
		return model;
	}

	public NetBuilder seed(int seed) {
		this.seed = seed;
		return this;
	}

	public NetBuilder learningRate(double learningRate) {
		this.learningRate = learningRate;
		return this;
	}

	public NetBuilder updater(Updater updater) {
		this.updater = Objects.requireNonNull(updater, "updater must not be null");
		return this;
	}

	public NetBuilder weightInit(WeightInit weightInit) {
		this.weightInit = Objects.requireNonNull(weightInit, "weight-init must not be null");
		return this;
	}

	public NetBuilder l2(double l2) {
		this.l2 = l2;
		return this;
	}

	public NetBuilder activation(Activation activation) {
		this.activation = Objects.requireNonNull(activation, "activation must not be null");
		return this;
	}

}
