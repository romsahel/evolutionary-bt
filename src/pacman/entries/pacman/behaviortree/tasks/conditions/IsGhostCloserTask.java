package pacman.entries.pacman.behaviortree.tasks.conditions;

import pacman.entries.pacman.behaviortree.MyPacMan;
import pacman.entries.pacman.behaviortree.helpers.Leaf;
import pacman.game.Constants.DM;
import pacman.game.Game;

/**
 * A condition task: returns true if the nearest ghost is edible
 * @author romsahel
 */
public class IsGhostCloserTask extends Leaf
{

    public IsGhostCloserTask(MyPacMan parent)
    {
        super(parent);
    }

    @Override
    public boolean DoAction(Game game)
    {
        double ghostDistance = 0, pillDistance = 999, powerPillDistance = 999;
        if (state.getNearestGhost() != null)
        ghostDistance = state.getNearestGhost().getDistance();
        if (state.getNearestPill() > -1)
        pillDistance = game.getDistance(state.getCurrent(), state.getNearestPill(), DM.PATH);
        if (state.getNearestPowerPill() > -1)
        	powerPillDistance = game.getDistance(state.getCurrent(), state.getNearestPowerPill(), DM.PATH);
        return (ghostDistance < pillDistance && ghostDistance < powerPillDistance);
    }

}
