package pacman.entries.pacman.behaviortree.helpers;

import pacman.game.Game;

/**
 *
 * @author romsahel
 */
public class Sequence extends Composite
{

    @Override
    public boolean DoAction(Game game)
    {
        for (Task child : children)
        {
            if (!child.DoAction(game))
                return false;
        }
        return true;
    }

}
