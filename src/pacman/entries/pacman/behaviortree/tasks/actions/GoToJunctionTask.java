package pacman.entries.pacman.behaviortree.tasks.actions;

import pacman.entries.pacman.GameState;
import pacman.entries.pacman.behaviortree.BTPacMan;
import pacman.entries.pacman.behaviortree.helpers.Task;
import pacman.entries.pacman.behaviortree.tasks.conditions.IsPathToJunctionSafeTask;
import pacman.game.Constants;
import pacman.game.Game;

public class GoToJunctionTask extends Task {
    public GoToJunctionTask()
    {
        super();
    }

    @Override
    public boolean DoAction(Game game, BTPacMan parent, GameState state)
    {
    	new IsPathToJunctionSafeTask().DoAction(game, parent, state);

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
