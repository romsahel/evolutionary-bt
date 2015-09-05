package pacman.entries.pacman.behaviortree.tasks;

import pacman.entries.pacman.behaviortree.MyPacMan;
import pacman.entries.pacman.behaviortree.helpers.Leaf;
import pacman.game.Constants;
import pacman.game.Game;

/**
 *
 * @author romsahel
 */
public class RunAwayTask extends Leaf
{

    public RunAwayTask(MyPacMan parent)
    {
        super(parent);
    }

    @Override
    public boolean DoAction(Game game)
    {
        final int current = parent.getCurrent();
        final int nearestPowerPill = parent.getNearestPowerPill();

        parent.setMove(game.getNextMoveAwayFromTarget(current, parent.getNearestGhost().getIndex(), Constants.DM.PATH));
        if (nearestPowerPill == -1)
            return true;
        
        for (int node : game.getShortestPath(current, nearestPowerPill))
        {
            if (game.getDistance(node, parent.getNearestGhost().getIndex(), Constants.DM.PATH) < 2)
                return true;

        }
        parent.setMove(game.getNextMoveTowardsTarget(current, nearestPowerPill, Constants.DM.PATH));
        return true;
    }

}
