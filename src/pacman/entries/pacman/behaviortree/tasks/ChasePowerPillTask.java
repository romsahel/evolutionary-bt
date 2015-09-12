package pacman.entries.pacman.behaviortree.tasks;

import pacman.entries.pacman.behaviortree.MyPacMan;
import pacman.entries.pacman.behaviortree.helpers.Leaf;
import pacman.game.Constants;
import pacman.game.Game;

/**
 * Action task: sets the next move to go to the nearest powerpill
 * @author romsahel
 */
public class ChasePowerPillTask extends Leaf
{

    public ChasePowerPillTask(MyPacMan parent)
    {
        super(parent);
    }

    @Override
    public boolean DoAction(Game game)
    {
        parent.setMove(game.getNextMoveTowardsTarget(
                parent.getCurrent(), 
                parent.getNearestPowerPill(), 
                Constants.DM.PATH));
        return true;
    }

}
