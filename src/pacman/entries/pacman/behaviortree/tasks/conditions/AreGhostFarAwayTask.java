package pacman.entries.pacman.behaviortree.tasks.conditions;

import pacman.entries.pacman.behaviortree.BTPacMan;
import pacman.entries.pacman.behaviortree.helpers.Leaf;
import pacman.game.Game;

/**
 * A condition task: returns true if the nearest ghost is edible
 * @author romsahel
 */
public class AreGhostFarAwayTask extends Leaf
{
    public static int FAR_DISTANCE = 40;

    public AreGhostFarAwayTask(BTPacMan parent)
    {
        super(parent);
    }

    @Override
    public boolean DoAction(Game game)
    {
    	if (state.getNearestGhost() == null)
    		return false;
    	return (state.getNearestGhost().getDistance() > FAR_DISTANCE);
    }

}
