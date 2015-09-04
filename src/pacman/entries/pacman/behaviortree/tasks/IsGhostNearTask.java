package pacman.entries.pacman.behaviortree.tasks;

import pacman.entries.pacman.behaviortree.MyPacMan;
import pacman.entries.pacman.behaviortree.helpers.Leaf;
import pacman.game.Game;

/**
 *
 * @author romsahel
 */
public class IsGhostNearTask extends Leaf
{

    public IsGhostNearTask(MyPacMan parent)
    {
        super(parent);
    }

    @Override
    public boolean DoAction(Game game)
    {
        return parent.getNearestGhost().getType() != null
                && parent.getNearestGhost().getDistance() < 25;
    }
}
