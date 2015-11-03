package pacman.entries.pacman;

/*
 * @author romsahel
 */
public class Junction
{
	private final int index;
	private final double distance;

	public Junction(int index, double distance)
	{
		this.index = index;
		this.distance = distance;
	}

	/**
	 * @return the index
	 */
	public int getIndex()
	{
		return index;
	}

	/**
	 * @return the distance
	 */
	public double getDistance()
	{
		return distance;
	}
}
