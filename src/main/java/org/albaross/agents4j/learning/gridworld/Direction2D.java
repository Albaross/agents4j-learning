package org.albaross.agents4j.learning.gridworld;

import java.util.Random;

/**
 * 
 * @author Manuel Barbi
 *
 */
public enum Direction2D {

	NORTH, SOUTH, EAST, WEST;

	private static final Random RND = new Random();

	public static Direction2D randomAction() {
		return values()[RND.nextInt(values().length)];
	}

	public static Direction2D decode(int code) {
		return values()[code];
	}

}
