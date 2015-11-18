package pacman.entries.pacman.behaviortree.helpers;

import pacman.game.Game;

/**
 * Represents a selector node: executes actions
 * of all of its children until one succeeds (returns true)
 *
 * @author romsahel
 */
public class Selector extends Composite implements java.io.Serializable
{

	public Selector(int depth)
	{
		super(depth);
	}

	public Selector()
	{
	}

	@Override
	public boolean DoAction(Game game)
	{
		for (Node child : children)
		{
			printDebug(child, true);
			final boolean result = child.DoAction(game);
			printDebug(child, false);

			if (result)
				return true;
		}
		return true;
	}

}