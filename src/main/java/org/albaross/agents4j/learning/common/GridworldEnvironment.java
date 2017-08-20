package org.albaross.agents4j.learning.common;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import org.albaross.agents4j.core.Agent;
import org.albaross.agents4j.learning.MDPWrapper;
import org.albaross.agents4j.learning.RLEnvironment;
import org.deeplearning4j.rl4j.mdp.MDP;
import org.deeplearning4j.rl4j.space.DiscreteSpace;
import org.deeplearning4j.rl4j.space.ObservationSpace;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GridworldEnvironment extends RLEnvironment<Location2D, Direction2D> implements MDPWrapper<Location2D, Direction2D> {

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

	protected final DiscreteSpace actions = new DiscreteSpace(4);
	protected final ObservationSpace<Location2D> observations = new ObservationSpace<Location2D>() {

		@Override
		public String getName() {
			return "Observations 2D";
		}

		@Override
		public int[] getShape() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public INDArray getLow() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public INDArray getHigh() {
			// TODO Auto-generated method stub
			return null;
		}

	};

	@Override
	public ObservationSpace<Location2D> getObservationSpace() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DiscreteSpace getActionSpace() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MDP<Location2D, Integer, DiscreteSpace> newInstance() {
		return new GridworldEnvironment(agents, rewards, start, goal, width, height);
	}

	@Override
	public RLEnvironment<Location2D, Direction2D> env() {
		return this;
	}

	@Override
	public Direction2D decode(Integer action) {
		return Direction2D.values()[action];
	}

	@Override
	public String toString() {
		if (grid == null)
			initGrid();

		byte[] tmp = new byte[grid.length];
		System.arraycopy(grid, 0, tmp, 0, grid.length);
		stamp(tmp, currentState[0], ' ', '=', ')', ' ');
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
				stamp(grid, loc, '#', '#', '#', '#');
			}
		});

		stamp(grid, goal, '>', ' ', ' ', '<');
	}

	protected void stamp(byte[] dst, Location2D loc, char c1, char c2, char c3, char c4) {
		int p = (2 * (height - loc.y) + 1) * row + 5 * (loc.x - 1);
		dst[p + 1] = (byte) c1;
		dst[p + 2] = (byte) c2;
		dst[p + 3] = (byte) c3;
		dst[p + 4] = (byte) c4;
	}

}
