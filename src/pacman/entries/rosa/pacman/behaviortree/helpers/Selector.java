package pacman.entries.rosa.pacman.behaviortree.helpers;

import pacman.game.Game;

/**
 * Represents a selector node: executes actions 
 * of all of its children until one succeeds (returns true)
 * 
 * @author romsahel
 */
public class Selector extends Composite
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
		boolean isComposite = false;
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