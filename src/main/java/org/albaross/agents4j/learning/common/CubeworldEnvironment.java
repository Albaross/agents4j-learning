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

public class CubeworldEnvironment extends RLEnvironment<Location3D, Direction3D> implements MDPWrapper<Location3D, Direction3D> {

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

	protected final DiscreteSpace actions = new DiscreteSpace(6);
	protected final ObservationSpace<Location3D> observations = new Obs

	@Override
	public DiscreteSpace getActionSpace() {
		return actions;
	}
	
	@Override
	public ObservationSpace<Location3D> getObservationSpace() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public MDP<Location3D, Integer, DiscreteSpace> newInstance() {
		return new CubeworldEnvironment(agents, rewards, start, goal, width, height, depth);
	}

	@Override
	public RLEnvironment<Location3D, Direction3D> env() {
		return this;
	}

	@Override
	public Direction3D decode(Integer action) {
		return Direction3D.values()[action];
	}

}
