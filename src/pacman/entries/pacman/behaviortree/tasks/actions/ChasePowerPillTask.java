package pacman.entries.pacman.behaviortree.tasks.actions;

import pacman.entries.pacman.GameState;
import pacman.entries.pacman.behaviortree.BTPacMan;
import pacman.entries.pacman.behaviortree.helpers.Task;
import pacman.game.Constants;
import pacman.game.Game;

/**
 * Action task: sets the next move to go to the nearest powerpill
 * <p>
 * @author romsahel
 */
public class ChasePowerPillTask extends Task
{

	public ChasePowerPillTask()
	{
		super();
	}

	@Override
	public boolean DoAction(Game game, BTPacMan parent, GameState state)
	{
		if (state.getNearestPowerPill() == -1)
			return false;

		parent.setMove(game.getNextMoveTowardsTarget(
				state.getCurrent(),
				state.getNearestPowerPill(),
				Constants.DM.PATH));
		return true;
	}

}
