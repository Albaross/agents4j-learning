package org.albaross.agents4j.learning.common;

import java.util.List;
import java.util.Map;

import org.albaross.agents4j.core.Agent;
import org.albaross.agents4j.learning.RLEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GridworldEnvironment extends RLEnvironment<Location2D, Direction2D> {

	private static final Logger LOG = LoggerFactory.getLogger(GridworldEnvironment.class);

	protected int width, height;

	public GridworldEnvironment(List<Agent<Location2D, Direction2D>> agents, Map<Location2D, Double> rewards, Location2D start, Location2D goal,
			int width, int height) {
		super(agents, rewards, start, goal);
		this.width = width;
		this.height = height;
	}

	@Override
	protected Location2D[] ara(int size) {
		return new Location2D[size];
	}

	@Override
	public Location2D nextState(Location2D current, Direction2D action) {
		switch (action) {
		case NORTH:
			return current.y < height - 1 ? new Location2D(current.x, current.y + 1) : current;
		case SOUTH:
			return current.y > 0 ? new Location2D(current.x, current.y - 1) : current;
		case EAST:
			return current.x < width - 1 ? new Location2D(current.x + 1, current.y) : current;
		case WEST:
			return current.x > 0 ? new Location2D(current.x - 1, current.y) : current;
		default:
			LOG.warn("unknown action");
			return current;
		}
	}

}
