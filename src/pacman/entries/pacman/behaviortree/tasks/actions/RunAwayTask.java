package pacman.entries.pacman.behaviortree.tasks.actions;

import pacman.entries.pacman.behaviortree.BTPacMan;
import pacman.entries.pacman.behaviortree.helpers.Leaf;
import pacman.game.Constants;
import pacman.game.Game;

/**
 * Action task: sets the next move to get away from the nearest ghost
 * @author romsahel
 */
public class RunAwayTask extends Leaf
{

    public RunAwayTask(BTPacMan parent)
    {
        super(parent);
    }

    @Override
    public boolean DoAction(Game game)
    {
    	if (state.getNearestGhost() == null)
    		return false;
        final int current = state.getCurrent();
        parent.setMove(game.getNextMoveAwayFromTarget(current, state.getNearestGhost().getIndex(), Constants.DM.PATH));
        return true;
    }

}
