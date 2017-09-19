package org.albaross.agents4j.learning;

import java.util.Arrays;

import org.albaross.agents4j.learning.gridworld.Direction2D;
import org.albaross.agents4j.learning.gridworld.GridworldRiver;
import org.albaross.agents4j.learning.gridworld.GridworldSimple;

public class QTableExample {

	public static void main(String[] args) {
		GridworldSimple env = new GridworldRiver(Arrays.asList(new ReplayAgent<>(Direction2D::randomAction, 500)));

		for (int r = 0; r < 100000; r++) {
			env.run();
			if(env.getCumulative(0) > 91) {
				System.out.println(r);
				break;
			}
		}
	}

}
