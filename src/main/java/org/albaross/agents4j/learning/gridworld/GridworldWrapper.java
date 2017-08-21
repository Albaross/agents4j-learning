package org.albaross.agents4j.learning.gridworld;

import java.util.Objects;

import org.albaross.agents4j.learning.MDPWrapper;
import org.albaross.agents4j.learning.RLEnvironment;
import org.deeplearning4j.rl4j.mdp.MDP;
import org.deeplearning4j.rl4j.space.DiscreteSpace;
import org.deeplearning4j.rl4j.space.ObservationSpace;

public class GridworldWrapper implements MDPWrapper<Location2D, Direction2D> {

	protected final GridworldEnvironment gridworld;
	protected static final DiscreteSpace ACTIONS = new DiscreteSpace(4);
	protected static final ObservationSpace<Location2D> OBSERVATIONS = new GridworldObservations();

	public GridworldWrapper(GridworldEnvironment gridworld) {
		this.gridworld = Objects.requireNonNull(gridworld, "gridworld must not be null");
	}

	@Override
	public DiscreteSpace getActionSpace() {
		return ACTIONS;
	}

	@Override
	public ObservationSpace<Location2D> getObservationSpace() {
		return OBSERVATIONS;
	}

	@Override
	public MDP<Location2D, Integer, DiscreteSpace> newInstance() {
		return new GridworldWrapper(gridworld);
	}

	@Override
	public RLEnvironment<Location2D, Direction2D> env() {
		return gridworld;
	}

	@Override
	public Direction2D decode(Integer action) {
		return Direction2D.values()[action];
	}

}
