package org.albaross.agents4j.learning;

import java.util.Arrays;

import org.albaross.agents4j.core.common.BasicBuilder;
import org.albaross.agents4j.learning.gridworld.Direction2D;
import org.albaross.agents4j.learning.gridworld.EpsilonGreedy2D;
import org.albaross.agents4j.learning.gridworld.GridworldEnvironment;
import org.albaross.agents4j.learning.gridworld.GridworldRiver;
import org.albaross.agents4j.learning.gridworld.Location2D;

public class QTableExample {

	public static void main(String[] args) {
		GridworldEnvironment env = new GridworldRiver(Arrays.asList(
				new RLWrapper<>(new BasicBuilder<Location2D, Direction2D>().add(new EpsilonGreedy2D<>()).add(new QTable<>()).getAgent())));

		for (int r = 0; r < 10000; r++) {
			env.run();
		}
	}

}
