package org.albaross.agents4j.learning.common;

/**
 * 
 * @author Manuel Barbi
 *
 */
public class Location2D implements Comparable<Location2D> {

	public static final Location2D ORIGIN = new Location2D(0, 0);

	protected final int x;
	protected final int y;

	public Location2D(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Location2D other = (Location2D) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

	@Override
	public int compareTo(Location2D other) {
		int result = Integer.compare(this.x, other.x);
		if (result != 0)
			return result;

		return Integer.compare(this.y, other.y);
	}

	@Override
	public String toString() {
		return "(" + x + "," + y + ")";
	}

}
