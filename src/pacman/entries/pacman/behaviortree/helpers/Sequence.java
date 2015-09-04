package pacman.entries.pacman.behaviortree.helpers;

import pacman.game.Game;

/**
 *
 * @author romsahel
 */
public class Sequence extends Composite
{

    public Sequence(int depth)
    {
        super(depth);
    }

    public Sequence()
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
            
            if (!result)
                return false;
        }
        return true;
    }

}
