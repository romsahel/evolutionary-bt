package pacman.entries.pacman.behaviortree.helpers;

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
			if (DEBUG)
			{
				isComposite = child instanceof Composite;
				if (isComposite)
					System.out.println(prefix + child.getClass().getSimpleName());
			}
			final boolean result = child.DoAction(game);

			if (DEBUG)
			{
				if (!isComposite)
					System.out.println(prefix + child.getClass().getSimpleName() + ": " + result);
			}

			if (result)
				return true;
		}
		return true;
	}

}