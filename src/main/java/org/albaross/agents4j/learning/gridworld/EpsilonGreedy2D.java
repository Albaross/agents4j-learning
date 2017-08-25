package org.albaross.agents4j.learning.gridworld;

import java.util.Random;

import org.albaross.agents4j.learning.EpsilonGreedyOperator;

/**
 * 
 * @author Manuel Barbi
 *
 */
public class EpsilonGreedy2D<P> implements EpsilonGreedyOperator<P, Direction2D> {

	protected final Random rnd = new Random();

	@Override
	public Direction2D randomAction() {
		switch (rnd.nextInt(4)) {
		case 1:
			return Direction2D.NORTH;
		case 2:
			return Direction2D.SOUTH;
		case 3:
			return Direction2D.EAST;
		default:
			return Direction2D.WEST;
		}
	}

}
