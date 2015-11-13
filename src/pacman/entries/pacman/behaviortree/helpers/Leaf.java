package pacman.entries.pacman.behaviortree.helpers;

import pacman.entries.pacman.GameState;
import pacman.entries.pacman.behaviortree.BTPacMan;

/**
 *
 * @author romsahel
 */
public abstract class Leaf extends Node
{
    protected final BTPacMan parent;
    protected final GameState state;

    public Leaf(BTPacMan parent)
    {
        this.parent = parent;
        this.state = parent.getState();
    }

}
