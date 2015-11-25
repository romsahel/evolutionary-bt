package pacman.entries.pacman.behaviortree.tasks.conditions;

import pacman.entries.pacman.GameState;
import pacman.entries.pacman.behaviortree.BTPacMan;
import pacman.entries.pacman.behaviortree.helpers.Task;
import pacman.game.Game;

/**
 * A condition task: returns true if a ghost is within 
 * the specified distance
 * @author romsahel
 */
public class IsGhostNearTask extends Task
{

    public static int RUN_DISTANCE = 20;

	public IsGhostNearTask()
    {
        super();
    }

    @Override
    public boolean DoAction(Game game, BTPacMan parent, GameState state)
    {
        return state.getNearestGhost() != null
                && state.getNearestGhost().getDistance() < RUN_DISTANCE;
    }
}
