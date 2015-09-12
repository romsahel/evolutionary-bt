package pacman.entries.pacman.behaviortree.helpers;

import pacman.game.Game;

/**
 * Represents a sequence node: executes actions 
 * for all of the children except if one fails (returns false)
 * 
 * @author romsahel
 */
public class Sequence extends Composite
{

    public Sequence(int depth)
    {
        super(depth);
    }

    public Sequence()
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

			if (!result)
				return false;
		}
		return true;
	}
}
