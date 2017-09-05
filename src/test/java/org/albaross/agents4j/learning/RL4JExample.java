package org.albaross.agents4j.learning;

import static org.deeplearning4j.rl4j.network.dqn.DQNFactoryStdDense.Configuration.builder;

import java.io.IOException;
import java.util.Arrays;

import org.albaross.agents4j.learning.gridworld.Direction2D;
import org.albaross.agents4j.learning.gridworld.GridworldEnvironment;
import org.albaross.agents4j.learning.gridworld.GridworldLake;
import org.albaross.agents4j.learning.gridworld.Location2D;
import org.deeplearning4j.rl4j.learning.ILearning;
import org.deeplearning4j.rl4j.learning.sync.qlearning.QLearning;
import org.deeplearning4j.rl4j.learning.sync.qlearning.discrete.QLearningDiscreteDense;
import org.deeplearning4j.rl4j.network.dqn.DQN;
import org.deeplearning4j.rl4j.network.dqn.DQNFactoryStdDense;
import org.deeplearning4j.rl4j.space.DiscreteSpace;
import org.deeplearning4j.rl4j.util.DataManager;
import org.nd4j.linalg.factory.Nd4j;

public class RL4JExample {

	public static final int NET_IN = 3;
	public static final int NET_OUT = 4;

	public static QLearning.QLConfiguration GRID_QL = new QLearning.QLConfiguration(123,   //Random seed
			100000,//Max step By epoch
			80000, //Max step
			10000, //Max size of experience replay
			32,    //size of batches
			100,   //target update (hard)
			0,     //num step noop warmup
			0.05,  //reward scaling
			0.99,  //gamma
			10.0,  //td-error clipping
			0.1f,  //min epsilon
			2000,  //num step for eps greedy anneal
			true   //double DQN
	);

	@SuppressWarnings("rawtypes")
	public static DQN GRID_NET;

	static {
		DQNFactoryStdDense.Configuration conf = builder().l2(0.01).learningRate(1e-2).numLayer(3).numHiddenNodes(16).build();
		DQNFactoryStdDense fac = new DQNFactoryStdDense(conf);
		GRID_NET = fac.buildDQN(new int[] { NET_IN }, NET_OUT);
	}

	public static void main(String[] args) throws IOException {

		DataManager manager = new DataManager();

		GridworldEnvironment env = new GridworldLake(Arrays.asList((p) -> {
			return Direction2D.decode(Nd4j.argMax(GRID_NET.output(Nd4j.create(p.toArray())), 1).getInt(0));
		}));

		MDPWrapper<Location2D, Direction2D> mdp = new MDPWrapper<>(env, NET_IN, NET_OUT, Direction2D::decode);

		ILearning<Location2D, Integer, DiscreteSpace> dql = new QLearningDiscreteDense<>(mdp, GRID_NET, GRID_QL, manager);

		dql.train();
		
		// test what was learned
		env.run();
		System.out.println(env.getCumulative(0));
	}

}
