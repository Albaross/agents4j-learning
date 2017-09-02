package org.albaross.agents4j.learning;

import java.io.IOException;
import java.util.Arrays;

import org.albaross.agents4j.core.common.ActionOperator;
import org.albaross.agents4j.core.common.BasicBuilder;
import org.albaross.agents4j.core.common.DataComponent;
import org.albaross.agents4j.learning.gridworld.Direction2D;
import org.albaross.agents4j.learning.gridworld.GridworldRiver;
import org.albaross.agents4j.learning.gridworld.GridworldWrapper;
import org.albaross.agents4j.learning.gridworld.Location2D;
import org.deeplearning4j.rl4j.learning.sync.qlearning.QLearning;
import org.deeplearning4j.rl4j.learning.sync.qlearning.discrete.QLearningDiscreteDense;
import org.deeplearning4j.rl4j.network.dqn.IDQN;
import org.deeplearning4j.rl4j.policy.DQNPolicy;
import org.deeplearning4j.rl4j.util.DataManager;

public class RL4JExample {

	public static QLearning.QLConfiguration GRIDWORLD_QL = new QLearning.QLConfiguration(123,   //Random seed
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
			true //double DQN
	);

	public static IDQN GRIDWORLD_NET = new QNet<Location2D, Direction2D>(3, 16, 4, 4, 0.001, (code) -> {
		return Direction2D.values()[code];
	});

	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws IOException {

		//record the training data in rl4j-data in a new folder (save)
		DataManager manager = new DataManager(true);

		//define the mdp from gym (name, render)
		BasicBuilder<Location2D, Direction2D> builder = new BasicBuilder<>();
		builder.add((DataComponent) GRIDWORLD_NET).add((ActionOperator<Location2D, Direction2D>) (param) -> {
			return (Direction2D) param.getComponent(ValueFunction.class).getBestAction(param.getPerception());
		});

		GridworldRiver env = new GridworldRiver(Arrays.asList(builder.getAgent()));

		//define the training
		QLearningDiscreteDense<Location2D> dql = new QLearningDiscreteDense<>(new GridworldWrapper(env), GRIDWORLD_NET, GRIDWORLD_QL, manager);

		//train
		dql.train();

		System.out.println("rewards: " + env.getCumulative(0));

		//get the final policy
		DQNPolicy<Location2D> pol = dql.getPolicy();

		//serialize and save (serialization showcase, but not required)
		pol.save("/tmp/pol1");

	}

}
