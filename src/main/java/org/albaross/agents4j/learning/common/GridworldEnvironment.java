package org.albaross.agents4j.learning.common;

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

}
