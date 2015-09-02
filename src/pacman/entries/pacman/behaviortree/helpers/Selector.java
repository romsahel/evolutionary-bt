package pacman.entries.pacman.behaviortree.helpers;

import pacman.game.Game;

/**
 *
 * @author romsahel
 */
public class Selector extends Composite
{

    @Override
    public boolean DoAction(Game game)
    {
        for (Task child : children)
        {
            if (child.DoAction(game))
                return true;
        }
        return false;
    }

}
