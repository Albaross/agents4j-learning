package org.albaross.agents4j.learning.gridworld;

import static org.albaross.agents4j.learning.gridworld.Location2D.ORIGIN_2D;

import java.util.List;
import java.util.TreeMap;

import org.albaross.agents4j.core.Agent;

public class GridworldSimple extends GridworldEnvironment {

	protected static final Location2D GOAL = new Location2D(7, 0);

	public GridworldSimple(List<Agent<Location2D, Direction2D>> agents) {
		super(agents, new TreeMap<>(), ORIGIN_2D, GOAL, 8, 6);
	}

}
