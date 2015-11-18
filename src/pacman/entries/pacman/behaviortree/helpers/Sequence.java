package pacman.entries.pacman.behaviortree.helpers;

import java.io.Serializable;
import pacman.game.Game;

/**
 * Represents a sequence node: executes actions
 * for all of the children except if one fails (returns false)
 *
 * @author romsahel
 */
public class Sequence extends Composite implements Serializable
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
		for (Node child : children)
		{
				printDebug(child, true);
				final boolean result = child.DoAction(game);
				printDebug(child, false);

			if (!result)
				return false;
		}
		return true;
	}
}
