package org.albaross.agents4j.learning.common;

import java.nio.charset.StandardCharsets;
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
			return current.y < height ? new Location2D(current.x, current.y + 1) : current;
		case SOUTH:
			return current.y > 1 ? new Location2D(current.x, current.y - 1) : current;
		case EAST:
			return current.x < width ? new Location2D(current.x + 1, current.y) : current;
		case WEST:
			return current.x > 1 ? new Location2D(current.x - 1, current.y) : current;
		default:
			LOG.warn("unknown action");
			return current;
		}
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
		int p = (2 * (height - loc.y) + 1) * row + 5 * (loc.x - 1);

		for (int s = 0; s < cs.length; s++)
			dst[p + offset + s + 1] = (byte) cs[s];
	}

}
