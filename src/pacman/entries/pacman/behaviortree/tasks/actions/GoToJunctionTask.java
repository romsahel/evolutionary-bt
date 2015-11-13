package pacman.entries.pacman.behaviortree.tasks.actions;

import pacman.entries.pacman.behaviortree.BTPacMan;
import pacman.entries.pacman.behaviortree.helpers.Leaf;
import pacman.entries.pacman.behaviortree.tasks.conditions.IsPathToJunctionSafeTask;
import pacman.game.Constants;
import pacman.game.Game;

public class GoToJunctionTask extends Leaf {
    public GoToJunctionTask(BTPacMan parent)
    {
        super(parent);
    }

    @Override
    public boolean DoAction(Game game)
    {
    	new IsPathToJunctionSafeTask(parent).DoAction(game);
    	
    	if (state.getNearestSafeJunction() == null)
    		return false;
    	
        parent.setMove(game.getNextMoveTowardsTarget(
                        state.getCurrent(),
                        state.getNearestSafeJunction().getIndex(),
                        Constants.DM.PATH)
        );
        return true;
    }
}
