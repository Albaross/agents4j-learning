package org.albaross.agents4j.learning;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

import org.albaross.agents4j.core.common.BasicBuilder;
import org.albaross.agents4j.learning.common.Direction2D;
import org.albaross.agents4j.learning.common.GridworldEnvironment;
import org.albaross.agents4j.learning.common.Location2D;

public class Example {

	public static void main(String[] args) {
		QTable<Location2D, Direction2D> qTable = new QTable<>();

		EpsilonGreedyOperator<Location2D, Direction2D> policy = () -> {
			final Random rnd = new Random(System.currentTimeMillis());

			switch (rnd.nextInt(4)) {
			case 1:
				return Direction2D.NORTH;
			case 2:
				return Direction2D.SOUTH;
			case 3:
				return Direction2D.EAST;
			default:
				return Direction2D.WEST;
			}
		};

		GridworldEnvironment env = new GridworldEnvironment(
				Arrays.asList(new RLWrapper<>(new BasicBuilder<Location2D, Direction2D>().add(policy).add(qTable).getAgent())), new HashMap<>(),
				Location2D.ORIGIN_2D, new Location2D(7, 0), 8, 6);

		for (int r = 0; r < 10000; r++) {
			env.run();
			System.out.println(qTable);
		}
	}

}
