package org.albaross.agents4j.learning.common;

import java.util.List;
import java.util.Map;

import org.albaross.agents4j.core.Agent;
import org.albaross.agents4j.learning.RLEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CubeworldEnvironment extends RLEnvironment<Location3D, Direction3D> {

	private static final Logger LOG = LoggerFactory.getLogger(CubeworldEnvironment.class);

	protected int width, height, depth;

	public CubeworldEnvironment(List<Agent<Location3D, Direction3D>> agents, Map<Location3D, Double> rewards, Location3D start, Location3D goal,
			int width, int height, int depth) {
		super(agents, rewards, start, goal);
		this.width = width;
		this.height = height;
		this.depth = depth;
	}

	@Override
	protected Location3D[] ara(int size) {
		return new Location3D[size];
	}

	@Override
	public Location3D nextState(Location3D current, Direction3D action) {
		switch (action) {
		case UP:
			return current.y < height - 1 ? new Location3D(current.x, current.y + 1, current.z) : current;
		case DOWN:
			return current.y > 0 ? new Location3D(current.x, current.y - 1, current.z) : current;
		case RIGHT:
			return current.x < width - 1 ? new Location3D(current.x + 1, current.y, current.z) : current;
		case LEFT:
			return current.x > 0 ? new Location3D(current.x - 1, current.y, current.z) : current;
		case BACK:
			return current.z < depth - 1 ? new Location3D(current.x, current.y, current.z + 1) : current;
		case FORTH:
			return current.z > 0 ? new Location3D(current.x, current.y, current.z - 1) : current;
		default:
			LOG.warn("unknown action");
			return current;
		}
	}

}
