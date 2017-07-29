package org.albaross.agents4j.learning.common;

/**
 * 
 * @author Manuel Barbi
 *
 */
public class Location3D implements Comparable<Location3D> {

	protected final int x;
	protected final int y;
	protected final int z;

	public Location3D(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		result = prime * result + z;
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
		Location3D other = (Location3D) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		if (z != other.z)
			return false;
		return true;
	}

	@Override
	public int compareTo(Location3D other) {
		int result = Integer.compare(this.x, other.x);
		if (result != 0)
			return result;

		result = Integer.compare(this.y, other.y);
		if (result != 0)
			return result;

		return Integer.compare(this.z, other.z);
	}

	@Override
	public String toString() {
		return "(" + x + ", " + y + ", " + z + ")";
	}

}
