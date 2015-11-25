package pacman.entries.pacman.behaviortree.tasks.conditions;

import pacman.entries.pacman.GameState;
import pacman.entries.pacman.behaviortree.BTPacMan;
import pacman.entries.pacman.behaviortree.helpers.Task;
import pacman.game.Game;

/**
 * A condition task: returns true if a ghost is within the specified distance
 * 
 * @author romsahel
 */
public class IsPacmanAtJunction extends Task
{

	public static int JUNCTIONS_TO_CHECK = 3;

	public IsPacmanAtJunction()
	{
		super();
	}

	@Override
	public boolean DoAction(Game game, BTPacMan parent, GameState state)
	{
		return (state.getNearestJunctions().first().getDistance() < 2);
	}
}
