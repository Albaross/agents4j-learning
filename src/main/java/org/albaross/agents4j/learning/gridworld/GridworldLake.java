package org.albaross.agents4j.learning.gridworld;

import java.util.List;

import org.albaross.agents4j.core.Agent;

public class GridworldLake extends GridworldSimple {

	public GridworldLake(List<Agent<Location2D, Direction2D>> agents) {
		super(agents);

		for (int y = 0; y <= 4; y++) {
			for (int x = 1; x <= 6; x++) {
				rewards.put(new Location2D(x, y), -100.0);
			}
		}
	}

}
