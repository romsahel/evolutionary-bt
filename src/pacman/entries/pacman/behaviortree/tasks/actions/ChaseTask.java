package pacman.entries.pacman.behaviortree.tasks.actions;

import pacman.entries.pacman.behaviortree.BTPacMan;
import pacman.entries.pacman.behaviortree.helpers.Task;
import pacman.game.Constants;
import pacman.game.Game;

/**
 * Action task: sets the next move to chase the nearest ghost
 * @author romsahel
 */
public class ChaseTask extends Task
{

    public ChaseTask(BTPacMan parent)
    {
        super(parent);
    }

    @Override
    public boolean DoAction(Game game)
    {
    	if (state.getNearestGhost() == null)
    		return false;
    	
        parent.setMove(game.getNextMoveTowardsTarget(
                state.getCurrent(),
                state.getNearestGhost().getIndex(),
                Constants.DM.PATH)
        );
        return true;
    }

}
