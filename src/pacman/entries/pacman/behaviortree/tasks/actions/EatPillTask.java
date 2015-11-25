package pacman.entries.pacman.behaviortree.tasks.actions;

import pacman.entries.pacman.GameState;
import pacman.entries.pacman.behaviortree.BTPacMan;
import pacman.entries.pacman.behaviortree.helpers.Task;
import pacman.game.Constants;
import pacman.game.Game;

/**
 * Action task: sets the next move to go eat the nearest pill
 * @author romsahel
 */
public class EatPillTask extends Task
{

    public EatPillTask()
    {
        super();
    }

    @Override
    public boolean DoAction(Game game, BTPacMan parent, GameState state)
    {
        parent.setMove(game.getNextMoveTowardsTarget(
                state.getCurrent(),
                state.getNearestPill(),
                Constants.DM.PATH)
        );
        return true;
    }
}
