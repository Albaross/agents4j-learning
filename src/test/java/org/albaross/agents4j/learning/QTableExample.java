package org.albaross.agents4j.learning;

import java.util.Arrays;

import org.albaross.agents4j.learning.gridworld.Direction2D;
import org.albaross.agents4j.learning.gridworld.GridworldLake;
import org.albaross.agents4j.learning.gridworld.GridworldSimple;
import org.albaross.agents4j.learning.gridworld.GridworldRiver;

public class QTableExample {

	public static void main(String[] args) {
		GridworldSimple env = new GridworldRiver(Arrays.asList(new ReplayAgent<>(Direction2D::randomAction)));

		for (int r = 0; r < 100000; r++) {
			env.run();
			System.out.println(env.getCumulative(0));
		}
	}

}
