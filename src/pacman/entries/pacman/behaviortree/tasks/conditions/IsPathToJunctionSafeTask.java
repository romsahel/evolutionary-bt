package pacman.entries.pacman.behaviortree.tasks.conditions;

import pacman.entries.pacman.GameState;
import pacman.entries.pacman.Junction;
import pacman.entries.pacman.behaviortree.BTPacMan;
import pacman.entries.pacman.behaviortree.helpers.Task;
import pacman.game.Constants;
import pacman.game.Game;

/**
 * A condition task: returns true if a ghost is within the specified distance
 *
 * @author romsahel
 */
public class IsPathToJunctionSafeTask extends Task
{

	public static int JUNCTIONS_TO_CHECK = 3;

	public IsPathToJunctionSafeTask()
	{
		super();
	}

	@Override
	public boolean DoAction(Game game, BTPacMan parent, GameState state)
	{
		int i = 0;

		if (state.getNearestGhost() == null)
			return true;

		for (Junction junction : state.getNearestJunctions())
		{
			boolean safe = true;
			for (int node : game.getShortestPath(state.getCurrent(), junction.getIndex()))
			{
				if (game.getDistance(node, state.getNearestGhost().getIndex(), Constants.DM.PATH) < 2)
				{
					safe = false;
					break;
				}
			}

			if (safe)
			{
				state.setNearestSafeJunction(junction);
				return true;
			}
//			GameView.addPoints(game, Color.GREEN, game.getShortestPath(state.getCurrent(), junction.getIndex()));

			if (i++ == JUNCTIONS_TO_CHECK)
				break;
		}

		return false;
	}
}
