package pacman.entries.pacman.behaviortree.tasks.conditions;

import pacman.entries.pacman.behaviortree.MyPacMan;
import pacman.entries.pacman.behaviortree.helpers.Leaf;
import pacman.game.Game;

/**
 * A condition task: returns true if a ghost is within the specified distance
 * 
 * @author romsahel
 */
public class IsPacmanAtJunction extends Leaf
{

	public static int JUNCTIONS_TO_CHECK = 3;

	public IsPacmanAtJunction(MyPacMan parent)
	{
		super(parent);
	}

	@Override
	public boolean DoAction(Game game)
	{
		return (state.getNearestJunctions().first().getDistance() < 2);
	}
}
