package org.albaross.agents4j.learning.gridworld;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import org.albaross.agents4j.core.Agent;
import org.albaross.agents4j.core.BasicEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GridworldSimple extends BasicEnvironment<Location2D, Direction2D> {

	private static final Logger LOG = LoggerFactory.getLogger(GridworldSimple.class);

	protected Location2D[] currentState;

	protected int width;
	protected int height;
	protected Location2D start;
	protected Location2D goal;
	protected double[] rewards;

	public GridworldSimple(List<Agent<Location2D, Direction2D>> agents) {
		this(agents, 8, 6);
	}

	public GridworldSimple(List<Agent<Location2D, Direction2D>> agents, int width, int height) {
		super(agents);
		this.currentState = new Location2D[agents.size()];
		this.width = width;
		this.height = height;
		this.start = new Location2D(0, 0);
		this.goal = new Location2D(width - 1, 0);
		this.rewards = new double[width * height];
		Arrays.fill(this.rewards, -1);
		reward(goal.x, goal.y, 100);
	}

	protected double reward(int x, int y) {
		return this.rewards[y * width + x];
	}

	protected void reward(int x, int y, double reward) {
		this.rewards[y * width + x] = reward;
	}

	@Override
	public Location2D createPerception(int agentId) {
		return currentState[agentId];
	}

	@Override
	public void executeAction(int agentId, Direction2D action) {
		Location2D current = currentState[agentId];

		switch (action) {
		case NORTH:
			currentState[agentId] = current.y < height - 1 ? new Location2D(current.x, current.y + 1) : current;
			break;
		case SOUTH:
			currentState[agentId] = current.y > 0 ? new Location2D(current.x, current.y - 1) : current;
			break;
		case EAST:
			currentState[agentId] = current.x < width - 1 ? new Location2D(current.x + 1, current.y) : current;
			break;
		case WEST:
			currentState[agentId] = current.x > 0 ? new Location2D(current.x - 1, current.y) : current;
			break;
		default:
			LOG.warn("unknown action");
		}
	}

	@Override
	public double getReward(int agentId) {
		Location2D next = createPerception(agentId);
		return reward(next.x, next.y);
	}

	@Override
	public boolean terminationCriterion(int agentId) {
		return currentState[agentId].equals(goal);
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
			stamp(tmp, loc.x, loc.y, 1, '=', ')');

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

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (rewards[y * width + x] <= -10) {
					stamp(grid, x, y, 0, '#', '#', '#', '#');
				}
			}
		}

		stamp(grid, goal.x, goal.y, 0, '>', ' ', ' ', '<');
	}

	protected void stamp(byte[] dst, int x, int y, int offset, char... cs) {
		int p = (2 * (height - y) - 1) * row + 5 * x;

		for (int s = 0; s < cs.length; s++)
			dst[p + offset + s + 1] = (byte) cs[s];
	}

}
