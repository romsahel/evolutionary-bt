package pacman.entries.pacman.behaviortree.helpers;

import pacman.game.Game;

/**
 *
 * @author romsahel
 */
public class Selector extends Composite
{

    public Selector(int depth)
    {
        super(depth);
    }

    public Selector()
    {
    }

    @Override
    public boolean DoAction(Game game)
    {
        boolean isComposite;
        for (Task child : children)
        {
            isComposite = child instanceof Composite;
            if (isComposite)
                System.out.println(prefix + child.getClass().getSimpleName());

            final boolean result = child.DoAction(game);

            if (!isComposite)
                System.out.println(prefix + child.getClass().getSimpleName() + ": " + result);

            if (result)
                return true;
        }
        return false;
    }

}
