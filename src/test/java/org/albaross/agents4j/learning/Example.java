package org.albaross.agents4j.learning;

import java.util.Arrays;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import org.albaross.agents4j.core.common.BasicBuilder;
import org.albaross.agents4j.learning.common.Direction2D;
import org.albaross.agents4j.learning.common.GridworldEnvironment;
import org.albaross.agents4j.learning.common.Location2D;

public class Example {

	public static void main(String[] args) {
		QTable<Location2D, Direction2D> qTable = new QTable<>();

		EpsilonGreedyOperator<Location2D, Direction2D> policy = () -> {
			final Random rnd = new Random();

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

		Map<Location2D, Double> rewards = new TreeMap<>();
		rewards.put(new Location2D(2, 1), -100.0);
		rewards.put(new Location2D(3, 1), -100.0);
		rewards.put(new Location2D(4, 1), -100.0);
		rewards.put(new Location2D(5, 1), -100.0);
		rewards.put(new Location2D(6, 1), -100.0);
		rewards.put(new Location2D(7, 1), -100.0);

		GridworldEnvironment env = new GridworldEnvironment(
				Arrays.asList(new RLWrapper<>(new BasicBuilder<Location2D, Direction2D>().add(policy).add(qTable).getAgent())), rewards,
				Location2D.ORIGIN_2D, new Location2D(8, 1), 8, 6);

		for (int r = 0; r < 10000; r++) {
			env.run();
		}
	}

}
