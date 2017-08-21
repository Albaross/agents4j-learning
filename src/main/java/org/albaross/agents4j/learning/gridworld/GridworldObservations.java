package org.albaross.agents4j.learning.gridworld;

import org.deeplearning4j.rl4j.space.ObservationSpace;
import org.nd4j.linalg.api.ndarray.INDArray;

/**
 * 
 * @author Manuel Barbi
 *
 */
public class GridworldObservations implements ObservationSpace<Location2D> {

	@Override
	public String getName() {
		return "gridworld";
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

}
