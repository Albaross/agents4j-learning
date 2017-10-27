package org.albaross.agents4j.learning;

import java.util.Arrays;

import org.albaross.agents4j.core.Agent;
import org.albaross.agents4j.core.BasicBuilder;
import org.albaross.agents4j.learning.components.QTableReplayComponent;
import org.albaross.agents4j.learning.gridworld.Direction2D;
import org.albaross.agents4j.learning.gridworld.GridworldRiver;
import org.albaross.agents4j.learning.gridworld.GridworldSimple;
import org.albaross.agents4j.learning.gridworld.Location2D;
import org.albaross.agents4j.learning.operators.EpsilonGreedyOperator;

public class QTableExampleGridworld {

	public static void main(String[] args) {
		BasicBuilder<Location2D, Direction2D> builder = new BasicBuilder<>();
		builder.add(new EpsilonGreedyOperator<>(Direction2D::randomAction, 0.1));
		builder.add(new QTableReplayComponent<>(1000000));
		Agent<Location2D, Direction2D> agent = builder.getAgent();
		GridworldSimple env = new GridworldRiver(Arrays.asList(agent));

		int sum = 0;
		for (int r = 0; r < 2000000; r++) {
			env.run();
			sum = (int) env.getCumulative(0);
			System.out.println("Round " + r + ", Rewards " + sum + ", Ticks " + env.getCurrentTick());
		}
	}

}
