package org.albaross.agents4j.learning.gridworld;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.albaross.agents4j.core.Agent;
import org.albaross.agents4j.learning.RLEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GridworldEnvironment extends RLEnvironment<Location2D, Direction2D> {

	private static final Logger LOG = LoggerFactory.getLogger(GridworldEnvironment.class);

	protected Location2D[] currentState;

	protected int width, height;
	protected Location2D start;
	protected Location2D goal;
	protected Map<Location2D, Double> rewards;

	public GridworldEnvironment(List<Agent<Location2D, Direction2D>> agents, Map<Location2D, Double> rewards, Location2D start, Location2D goal,
			int width, int height) {
		super(agents);
		this.width = width;
		this.height = height;

		this.rewards = Objects.requireNonNull(rewards, "rewards must not be null");
		for (Location2D loc : rewards.keySet())
			checkWithin(loc);

		this.start = checkWithin(start);
		this.goal = checkWithin(goal);

		this.currentState = new Location2D[agents.size()];
	}

	protected Location2D checkWithin(Location2D loc) {
		Objects.requireNonNull(loc, "location must not be null");

		if (loc.x < 0 || loc.x > width - 1)
			throw new IllegalArgumentException("x is out of range: " + loc.x);

		if (loc.y < 0 || loc.y > height - 1)
			throw new IllegalArgumentException("y is out of range: " + loc.y);

		return loc;
	}

	@Override
	public Location2D createPerception(long agentId) {
		return currentState[(int) agentId];
	}

	@Override
	public void executeAction(long agentId, Direction2D action) {
		int idx = (int) agentId;
		Location2D current = currentState[idx];

		switch (action) {
		case NORTH:
			currentState[idx] = current.y < height - 1 ? new Location2D(current.x, current.y + 1) : current;
			break;
		case SOUTH:
			currentState[idx] = current.y > 0 ? new Location2D(current.x, current.y - 1) : current;
			break;
		case EAST:
			currentState[idx] = current.x < width - 1 ? new Location2D(current.x + 1, current.y) : current;
			break;
		case WEST:
			currentState[idx] = current.x > 0 ? new Location2D(current.x - 1, current.y) : current;
			break;
		default:
			LOG.warn("unknown action");
		}
	}

	@Override
	protected double getReward(long agentId) {
		double reward = -1;
		Location2D next = createPerception(agentId);

		if (!next.equals(goal)) {
			if (rewards.containsKey(next))
				reward = rewards.get(next);
		} else {
			reward = 0;
		}

		return reward;
	}

	@Override
	public void runEnvironment() {}

	@Override
	public boolean terminationCriterion(long agentId) {
		return currentState[(int) agentId].equals(goal);
	}

	@Override
	public void reboot() {
		super.reboot();
		Arrays.fill(currentState, start);
	}

	@Override
	public String toString() {
		if (grid == null)
			initGrid();

		byte[] tmp = new byte[grid.length];
		System.arraycopy(grid, 0, tmp, 0, grid.length);

		for (Location2D loc : currentState)
			stamp(tmp, 1, loc, '=', ')');

		return new String(tmp, StandardCharsets.UTF_8);
	}

	protected byte[] grid;
	protected int row;
	protected int col;

	protected void initGrid() {
		row = 5 * this.width + 2;
		col = 2 * this.height + 1;

		grid = new byte[row * col];
		int n = 0;

		for (int y = 0; y < col; y++) {
			for (int x = 0; x < row; x++) {
				if (x == row - 1) {
					grid[n++] = '\n';
					continue;
				}

				if (x % 5 == 0) {
					grid[n++] = '|';
					continue;
				}

				if (y % 2 == 0) {
					grid[n++] = '-';
					continue;
				}

				grid[n++] = ' ';

			}
		}

		this.rewards.forEach((loc, r) -> {
			if (r <= -10) {
				stamp(grid, 0, loc, '#', '#', '#', '#');
			}
		});

		stamp(grid, 0, goal, '>', ' ', ' ', '<');
	}

	protected void stamp(byte[] dst, int offset, Location2D loc, char... cs) {
		int p = (2 * (height - loc.y) - 1) * row + 5 * loc.x;

		for (int s = 0; s < cs.length; s++)
			dst[p + offset + s + 1] = (byte) cs[s];
	}

}
