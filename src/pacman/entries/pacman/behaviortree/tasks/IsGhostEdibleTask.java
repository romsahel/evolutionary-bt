package pacman.entries.pacman.behaviortree.tasks;

import pacman.entries.pacman.behaviortree.MyPacMan;
import pacman.entries.pacman.behaviortree.helpers.Leaf;
import pacman.game.Game;

/**
 *
 * @author romsahel
 */
public class IsGhostEdibleTask extends Leaf
{

    public IsGhostEdibleTask(MyPacMan parent)
    {
        super(parent);
    }

    @Override
    public boolean DoAction(Game game)
    {
        final int edibleTime = game.getGhostEdibleTime(parent.getNearestGhost().getType());
        return (edibleTime > 1);
    }

}
