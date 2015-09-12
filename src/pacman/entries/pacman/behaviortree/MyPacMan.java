package pacman.entries.pacman.behaviortree;

import java.util.Comparator;
import java.util.TreeSet;

import pacman.controllers.Controller;
import pacman.entries.pacman.behaviortree.helpers.Composite;
import pacman.entries.pacman.behaviortree.helpers.Selector;
import pacman.entries.pacman.behaviortree.helpers.Sequence;
import pacman.entries.pacman.behaviortree.tasks.AnalyzeGameTask;
import pacman.entries.pacman.behaviortree.tasks.ChasePowerPillTask;
import pacman.entries.pacman.behaviortree.tasks.EatPillTask;
import pacman.entries.pacman.behaviortree.tasks.IsGhostEdibleTask;
import pacman.entries.pacman.behaviortree.tasks.IsGhostNearTask;
import pacman.entries.pacman.behaviortree.tasks.IsPathSafeTask;
import pacman.entries.pacman.behaviortree.tasks.RunAwayTask;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class MyPacMan extends Controller<MOVE>
{
	/*
	 * Root of the behavior tree
	 */
	private final Composite rootNode;

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

	/*
	 * Type of MOVE that will be returned at each game step
	 */
	private MOVE move;

	// instead of running away from the nearest, look at all the nearest
	// and find an exit that would work for all of them
	public MyPacMan()
	{
		nearestGhosts = new TreeSet<>(new Comparator<NearGhost>()
		{

			@Override
			public int compare(NearGhost o1, NearGhost o2)
			{
				return (int) (o1.getDistance() - o2.getDistance());
			}
		});

		rootNode = new Sequence();
		rootNode.addChildren(
		        new AnalyzeGameTask(this),
		        new Selector(1).addChildren(
		                new Sequence(2).addChildren(
		                        new IsGhostNearTask(this, IsGhostNearTask.RUN_DISTANCE),
		                        new Selector(3).addChildren(
		                                new Sequence(4).addChildren(
		                                        new IsGhostEdibleTask(this),
		                                        new EatPillTask(this)),
		                                new Selector(4).addChildren(
		                                        new Sequence(5).addChildren(
		                                                new IsPathSafeTask(this),
		                                                new ChasePowerPillTask(this)),
		                                        new RunAwayTask(this)))),
		                new EatPillTask(this)));
	}

	@Override
	public MOVE getMove(Game game, long timeDue)
	{
		rootNode.DoAction(game);
		if (Composite.DEBUG)
			System.out.println("===================");
		return move;
	}

	/**
	 * @param move
	 *            the move to set
	 */
	public void setMove(MOVE move)
	{
		this.move = move;
	}

	/**
	 * @return the nearestPill
	 */
	public int getNearestPill()
	{
		return nearestPill;
	}

	/**
	 * @param nearestPill
	 *            the nearestPill to set
	 */
	public void setNearestPill(int nearestPill)
	{
		this.nearestPill = nearestPill;
	}

	/**
	 * @return the nearestPowerPill
	 */
	public int getNearestPowerPill()
	{
		return nearestPowerPill;
	}

	/**
	 * @param game
	 * @param nearestPowerPill
	 *            the nearestPowerPill to set
	 */
	public void setNearestPowerPill(Game game, int nearestPowerPill)
	{
		this.nearestPowerPill = nearestPowerPill;
	}

	/**
	 * @return the current
	 */
	public int getCurrent()
	{
		return current;
	}

	/**
	 * @param current
	 *            the current to set
	 */
	public void setCurrent(int current)
	{
		this.current = current;
	}

	/**
	 * @return the nearestGhosts
	 */
	public NearGhost getNearestGhost()
	{
		return (nearestGhosts.size() > 0) ? nearestGhosts.first() : null;
	}

	/**
	 * @return the nearestGhosts
	 */
	public TreeSet<NearGhost> getNearestGhosts()
	{
		return nearestGhosts;
	}

}
