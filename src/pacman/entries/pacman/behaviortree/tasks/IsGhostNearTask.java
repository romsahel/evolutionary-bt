package pacman.entries.pacman.behaviortree.tasks;

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

    public static final int RUN_DISTANCE = 25;
    public static final int CHASE_DISTANCE = 20;
	private int distance;

	public IsGhostNearTask(MyPacMan parent, int distance)
    {
        super(parent);
		this.distance = distance;
    }

    @Override
    public boolean DoAction(Game game)
    {
        return state.getNearestGhost() != null
                && state.getNearestGhost().getDistance() < distance;
    }
}
