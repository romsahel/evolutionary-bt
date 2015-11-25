package pacman.entries.pacman.behaviortree.tasks.conditions;

import pacman.entries.pacman.GameState;
import pacman.entries.pacman.behaviortree.BTPacMan;
import pacman.entries.pacman.behaviortree.helpers.Task;
import pacman.game.Game;

/**
 * A condition task: returns true if the nearest ghost is edible
 * @author romsahel
 */
public class AreGhostFarAwayTask extends Task
{
    public static int FAR_DISTANCE = 40;

    public AreGhostFarAwayTask()
    {
        super();
    }

    @Override
    public boolean DoAction(Game game, BTPacMan parent, GameState state)
    {
    	if (state.getNearestGhost() == null)
    		return false;
    	return (state.getNearestGhost().getDistance() > FAR_DISTANCE);
    }

}
