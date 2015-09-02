package pacman.entries.pacman.behaviortree.tasks;

import pacman.entries.pacman.behaviortree.MyPacMan;
import pacman.entries.pacman.behaviortree.helpers.Leaf;
import pacman.game.Constants;
import pacman.game.Game;

/**
 *
 * @author romsahel
 */
public class ChaseTask extends Leaf
{

    public ChaseTask(MyPacMan parent)
    {
        super(parent);
    }

    @Override
    public boolean DoAction(Game game)
    {
        int current = game.getPacmanCurrentNodeIndex();
        parent.setMove(game.getNextMoveTowardsTarget(
                current,
                parent.getNearestGhost().getIndex(),
                Constants.DM.EUCLID)
        );
        return true;
    }

}
