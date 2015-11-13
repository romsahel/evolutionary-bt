package pacman.entries.pacman.behaviortree.tasks.conditions;

import java.awt.Color;

import pacman.entries.pacman.Junction;
import pacman.entries.pacman.behaviortree.BTPacMan;
import pacman.entries.pacman.behaviortree.helpers.Leaf;
import pacman.game.Constants;
import pacman.game.Game;
import pacman.game.GameView;

/**
 * A condition task: returns true if a ghost is within the specified distance
 * 
 * @author romsahel
 */
public class IsPathToJunctionSafeTask extends Leaf
{

	public static int JUNCTIONS_TO_CHECK = 3;

	public IsPathToJunctionSafeTask(BTPacMan parent)
	{
		super(parent);
	}

	@Override
	public boolean DoAction(Game game)
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
			GameView.addPoints(game, Color.GREEN, game.getShortestPath(state.getCurrent(), junction.getIndex()));
			
			if (i++ == JUNCTIONS_TO_CHECK)
				break;
		}

		return false;
	}
}
