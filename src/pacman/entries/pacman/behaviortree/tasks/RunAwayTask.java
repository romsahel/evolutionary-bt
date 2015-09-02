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
        int current = game.getPacmanCurrentNodeIndex();
        parent.setMove(game.getNextMoveAwayFromTarget(current, parent.getNearestGhost().getIndex(), Constants.DM.EUCLID));
        return true;
    }

}
