package org.albaross.agents4j.learning.gridworld;

import org.albaross.agents4j.learning.utils.StateEncoder;

public class EncoderLocation2D implements StateEncoder<Location2D> {

	private final int width;
	private final int height;

	public EncoderLocation2D(int width, int height) {
		this.width = width;
		this.height = height;
	}

	@Override
	public double[] encode(Location2D state) {
		double[] img = new double[width + height];
		img[state.x] = 1.0;
		img[width + state.y] = 1.0;
		return img;
	}

}
