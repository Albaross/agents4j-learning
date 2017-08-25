package org.albaross.agents4j.learning.gridworld;

import java.util.List;

import org.albaross.agents4j.core.Agent;

public class GridworldRiver extends GridworldSimple {

	public GridworldRiver(List<Agent<Location2D, Direction2D>> agents) {
		super(agents);

		for (int x = 1; x <= 6; x++) {
			rewards.put(new Location2D(x, 0), -100.0);
		}
	}

}
