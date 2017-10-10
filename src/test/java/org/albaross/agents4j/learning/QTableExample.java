package org.albaross.agents4j.learning;

import java.util.Arrays;

import org.albaross.agents4j.learning.gridworld.Direction2D;
import org.albaross.agents4j.learning.gridworld.GridworldRiver;
import org.albaross.agents4j.learning.gridworld.GridworldSimple;
import org.albaross.agents4j.learning.gridworld.Location2D;

public class QTableExample {

	public static void main(String[] args) {
		ReplayAgent<Location2D, Direction2D> agent = new ReplayAgent<>(Direction2D::randomAction, 500);
		GridworldSimple env = new GridworldRiver(Arrays.asList(agent));

		int sum = 0;
		for (int r = 0; r < 2000000; r++) {
			env.run();
			sum = (int) env.getCumulative(0);
			System.out.println("Round " + r + ", Rewards " + sum + ", Ticks " + env.getCurrentTick());
		}
	}

}
