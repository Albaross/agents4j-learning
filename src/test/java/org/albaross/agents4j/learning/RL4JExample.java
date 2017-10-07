package org.albaross.agents4j.learning;

import java.io.IOException;
import java.util.Arrays;

import org.albaross.agents4j.learning.gridworld.Direction2D;
import org.albaross.agents4j.learning.gridworld.GridworldRiver;
import org.albaross.agents4j.learning.gridworld.GridworldSimple;
import org.albaross.agents4j.learning.gridworld.Location2D;
import org.deeplearning4j.api.storage.StatsStorage;
import org.deeplearning4j.nn.conf.Updater;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.deeplearning4j.rl4j.learning.ILearning;
import org.deeplearning4j.rl4j.learning.sync.qlearning.QLearning;
import org.deeplearning4j.rl4j.learning.sync.qlearning.discrete.QLearningDiscreteDense;
import org.deeplearning4j.rl4j.network.dqn.DQN;
import org.deeplearning4j.rl4j.space.DiscreteSpace;
import org.deeplearning4j.rl4j.util.Constants;
import org.deeplearning4j.rl4j.util.DataManager;
import org.deeplearning4j.ui.api.UIServer;
import org.deeplearning4j.ui.stats.StatsListener;
import org.deeplearning4j.ui.storage.InMemoryStatsStorage;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.factory.Nd4j;

public class RL4JExample {

	public static final int NET_IN = 2;
	public static final int NET_OUT = 4;

	public static QLearning.QLConfiguration GRID_QL = new QLearning.QLConfiguration(12345,   //Random seed
			5000,//Max step By epoch
			1000000, //Max step
			10000, //Max size of experience replay
			32,    //size of batches
			10,   //target update (hard)
			5000,     //num step noop warmup
			0.01,  //reward scaling
			0.99,  //gamma
			0.5,  //td-error clipping
			0.1f,  //min epsilon
			1000,  //num step for eps greedy anneal
			false   //double DQN
	);

	public static void main(String[] args) throws IOException {

		DataManager manager = new DataManager();

		//Initialize the user interface backend
		UIServer uiServer = UIServer.getInstance();

		//Configure where the network information (gradients, score vs. time etc) is to be stored. Here: store in memory.
		StatsStorage statsStorage = new InMemoryStatsStorage();

		//Attach the StatsStorage instance to the UI: this allows the contents of the StatsStorage to be visualized
		uiServer.attach(statsStorage);

		MultiLayerNetwork mln = new NetBuilder() //
				.seed(123) //
				.learningRate(1e-3)//
				.l2(1e-3)//
				.updater(Updater.ADAM)//
				.weightInit(WeightInit.XAVIER)//
				.activation(Activation.RELU)//
				.createNet(NET_IN, 16, NET_OUT);

		mln.setListeners(new StatsListener(statsStorage), new ScoreIterationListener(Constants.NEURAL_NET_ITERATION_LISTENER));

		GridworldSimple env = new GridworldRiver(Arrays.asList((p) -> {
			return Direction2D.decode(Nd4j.argMax(mln.output(Nd4j.create(p.toArray())), 1).getInt(0));
		}));

		MDPWrapper<Location2D, Direction2D> mdp = new MDPWrapper<>(env, NET_IN, NET_OUT, Direction2D::decode);
		ILearning<Location2D, Integer, DiscreteSpace> dql = new QLearningDiscreteDense<>(mdp, new DQN<>(mln), GRID_QL, manager);

		dql.train();

		// test what was learned
		env.run();
		System.out.println(env.getCumulative(0));
	}

}
