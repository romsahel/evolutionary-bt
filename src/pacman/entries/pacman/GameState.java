package pacman.entries.pacman;

import java.util.Comparator;
import java.util.TreeSet;

import pacman.game.Constants;
import pacman.game.Game;

public class GameState
{
	/*
	 * List of all the ghosts sorted by distance
	 */
	public final TreeSet<NearGhost> nearestGhosts;

	/*
	 * The indexes of the nearest pill and power pill
	 */
	private int nearestPill, nearestPowerPill;

	/*
	 * Index of current position
	 */
	private int current;

	public GameState()
	{
		nearestGhosts = new TreeSet<>(new Comparator<NearGhost>()
		{
			@Override
			public int compare(NearGhost o1, NearGhost o2)
			{
				return (int) (o1.getDistance() - o2.getDistance());
			}
		});

	}

	public void update(Game game)
	{
		update(game, game.getPacmanCurrentNodeIndex());
	}

	public void update(Game game, int current)
	{
		setCurrent(current);

		updateNearestGhosts(game);

		int[] pills = game.getActivePillsIndices();
		int[] powerPills = game.getActivePowerPillsIndices();

		// merge the pills and powerpills indexes into one array to find
		// the nearest one
		int[] availablePills = new int[pills.length + powerPills.length];
		System.arraycopy(pills, 0, availablePills, 0, pills.length);
		System.arraycopy(powerPills, 0, availablePills, pills.length, powerPills.length);

		setNearestPill(game.getClosestNodeIndexFromNodeIndex(current,
		        availablePills,
		        Constants.DM.PATH));

		setNearestPowerPill(game, game.getClosestNodeIndexFromNodeIndex(current,
		        powerPills,
		        Constants.DM.PATH));
	}

	/**
	 * Update the sorted list of enabled ghosts (excludes ghosts in the lair)
	 * 
	 * @param game
	 */
	private void updateNearestGhosts(Game game)
	{
		getNearestGhosts().clear();
		for (Constants.GHOST ghost : Constants.GHOST.values())
			if (game.getGhostLairTime(ghost) < 1)
			{
				double shortest = game.getDistance(
				        current,
				        game.getGhostCurrentNodeIndex(ghost),
				        Constants.DM.PATH);
				getNearestGhosts().add(new NearGhost(game, ghost, shortest));
			}
	}

	public int getNearestPill()
	{
		return nearestPill;
	}

	public void setNearestPill(int nearestPill)
	{
		this.nearestPill = nearestPill;
	}

	public int getNearestPowerPill()
	{
		return nearestPowerPill;
	}

	public void setNearestPowerPill(Game game, int nearestPowerPill)
	{
		this.nearestPowerPill = nearestPowerPill;
	}

	public int getCurrent()
	{
		return current;
	}

	public void setCurrent(int current)
	{
		this.current = current;
	}

	public NearGhost getNearestGhost()
	{
		return (nearestGhosts.size() > 0) ? nearestGhosts.first() : null;
	}

	public TreeSet<NearGhost> getNearestGhosts()
	{
		return nearestGhosts;
	}
}
