package org.albaross.agents4j.learning.gridworld;

import java.util.List;

import org.albaross.agents4j.core.Agent;

public class GridworldRiver extends GridworldSimple {

	public GridworldRiver(List<Agent<Location2D, Direction2D>> agents) {
		this(agents, 8, 6);
	}

	public GridworldRiver(List<Agent<Location2D, Direction2D>> agents, int width, int height) {
		super(agents, width, height);

		for (int x = 1; x < width - 1; x++) {
			reward(x, 0, -100.0);
		}
	}

}
