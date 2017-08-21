package org.albaross.agents4j.learning;

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

import org.albaross.agents4j.core.common.BasicBuilder;
import org.albaross.agents4j.learning.gridworld.Direction2D;
import org.albaross.agents4j.learning.gridworld.Greedy2D;
import org.albaross.agents4j.learning.gridworld.GridworldEnvironment;
import org.albaross.agents4j.learning.gridworld.Location2D;

public class Example {

	public static void main(String[] args) {
		Map<Location2D, Double> rewards = new TreeMap<>();
		rewards.put(new Location2D(1, 0), -100.0);
		rewards.put(new Location2D(2, 0), -100.0);
		rewards.put(new Location2D(3, 0), -100.0);
		rewards.put(new Location2D(4, 0), -100.0);
		rewards.put(new Location2D(5, 0), -100.0);
		rewards.put(new Location2D(6, 0), -100.0);

		GridworldEnvironment env = new GridworldEnvironment(
				Arrays.asList(new RLWrapper<>(new BasicBuilder<Location2D, Direction2D>().add(new Greedy2D<>()).add(new QTable<>()).getAgent())),
				rewards, Location2D.ORIGIN_2D, new Location2D(7, 0), 8, 6);

		for (int r = 0; r < 10000; r++) {
			env.run();
		}
	}

}
