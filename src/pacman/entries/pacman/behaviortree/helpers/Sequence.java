package pacman.entries.pacman.behaviortree.helpers;

import pacman.entries.pacman.GameState;
import pacman.entries.pacman.behaviortree.BTPacMan;
import pacman.game.Game;

/**
 * Represents a sequence node: executes actions
 * for all of the children except if one fails (returns false)
 *
 * @author romsahel
 */
public class Sequence extends Composite implements java.io.Serializable
{

	public Sequence(int depth)
	{
		super(depth);
	}

	public Sequence()
	{
	}

	@Override
	public boolean DoAction(Game game, BTPacMan parent, GameState state)
	{
		for (Node child : children)
		{
			printDebug(child, true);
			final boolean result = child.DoAction(game, parent, state);
			printDebug(child, false);

			if (!result)
				return false;
		}
		return true;
	}
}
