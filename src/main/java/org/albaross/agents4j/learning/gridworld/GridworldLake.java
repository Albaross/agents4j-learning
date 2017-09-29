package org.albaross.agents4j.learning.gridworld;

import java.util.List;

import org.albaross.agents4j.core.Agent;

public class GridworldLake extends GridworldSimple {

	public GridworldLake(List<Agent<Location2D, Direction2D>> agents) {
		this(agents, 8, 6);
	}

	public GridworldLake(List<Agent<Location2D, Direction2D>> agents, int width, int height) {
		super(agents, width, height);

		for (int y = 0; y < height - 1; y++) {
			for (int x = 1; x < width - 1; x++) {
				reward(x, y, -100.0);
			}
		}
	}

	public GridworldLake(GridworldLake env) {
		super(env);
	}

	@Override
	public GridworldLake clone() {
		return new GridworldLake(this);
	}

}
