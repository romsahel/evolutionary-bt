package pacman.entries.rosa.pacman.behaviortree.tasks;

import pacman.entries.rosa.pacman.behaviortree.MyPacMan;
import pacman.entries.rosa.pacman.behaviortree.helpers.Leaf;
import pacman.game.Constants;
import pacman.game.Game;

/**
 * Action task: sets the next move to get away from the nearest ghost
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
        final int current = state.getCurrent();
        parent.setMove(game.getNextMoveAwayFromTarget(current, state.getNearestGhost().getIndex(), Constants.DM.PATH));
        return true;
    }

}
