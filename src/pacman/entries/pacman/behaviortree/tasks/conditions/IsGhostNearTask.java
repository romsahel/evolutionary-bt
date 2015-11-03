package pacman.entries.pacman.behaviortree.tasks.conditions;

import pacman.entries.pacman.behaviortree.MyPacMan;
import pacman.entries.pacman.behaviortree.helpers.Leaf;
import pacman.game.Game;

/**
 * A condition task: returns true if a ghost is within 
 * the specified distance
 * @author romsahel
 */
public class IsGhostNearTask extends Leaf
{

    public static int RUN_DISTANCE = 20;

	public IsGhostNearTask(MyPacMan parent)
    {
        super(parent);
    }

    @Override
    public boolean DoAction(Game game)
    {
        return state.getNearestGhost() != null
                && state.getNearestGhost().getDistance() < RUN_DISTANCE;
    }
}
