package org.albaross.agents4j.learning;

import static org.deeplearning4j.rl4j.network.dqn.DQNFactoryStdDense.Configuration.builder;

import java.io.IOException;
import java.util.Arrays;

import org.albaross.agents4j.learning.gridworld.Direction2D;
import org.albaross.agents4j.learning.gridworld.GridworldSimple;
import org.albaross.agents4j.learning.gridworld.GridworldRiver;
import org.albaross.agents4j.learning.gridworld.Location2D;
import org.deeplearning4j.api.storage.StatsStorage;
import org.deeplearning4j.optimize.api.IterationListener;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.deeplearning4j.rl4j.learning.ILearning;
import org.deeplearning4j.rl4j.learning.sync.qlearning.QLearning;
import org.deeplearning4j.rl4j.learning.sync.qlearning.discrete.QLearningDiscreteDense;
import org.deeplearning4j.rl4j.network.dqn.DQN;
import org.deeplearning4j.rl4j.network.dqn.DQNFactoryStdDense;
import org.deeplearning4j.rl4j.space.DiscreteSpace;
import org.deeplearning4j.rl4j.util.Constants;
import org.deeplearning4j.rl4j.util.DataManager;
import org.deeplearning4j.ui.api.UIServer;
import org.deeplearning4j.ui.stats.StatsListener;
import org.deeplearning4j.ui.storage.InMemoryStatsStorage;
import org.nd4j.linalg.factory.Nd4j;

public class RL4JExample {

	public static final int NET_IN = 3;
	public static final int NET_OUT = 4;

	public static QLearning.QLConfiguration GRID_QL = new QLearning.QLConfiguration(123,   //Random seed
			10000,//Max step By epoch
			100000, //Max step
			1000, //Max size of experience replay
			32,    //size of batches
			10,   //target update (hard)
			0,     //num step noop warmup
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

		DQNFactoryStdDense.Configuration conf = builder() //
				.l2(0.001) //
				.learningRate(0.001) //
				.numLayer(2) //
				.numHiddenNodes(16) //
				.listeners(new IterationListener[] { new StatsListener(statsStorage),
						new ScoreIterationListener(Constants.NEURAL_NET_ITERATION_LISTENER) }) //
				.build();

		DQNFactoryStdDense fac = new DQNFactoryStdDense(conf);
		@SuppressWarnings("rawtypes")
		final DQN net = fac.buildDQN(new int[] { NET_IN }, NET_OUT);

		GridworldSimple env = new GridworldRiver(Arrays.asList((p) -> {
			return Direction2D.decode(Nd4j.argMax(net.output(Nd4j.create(p.toArray())), 1).getInt(0));
		}));

		MDPWrapper<Location2D, Direction2D> mdp = new MDPWrapper<>(env, NET_IN, NET_OUT, Direction2D::decode);

		ILearning<Location2D, Integer, DiscreteSpace> dql = new QLearningDiscreteDense<>(mdp, net, GRID_QL, manager);

		dql.train();

		// test what was learned
		env.run();
		System.out.println(env.getCumulative(0));
	}

}
