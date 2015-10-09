package pacman.entries.rosa.pacman.behaviortree.helpers;

import pacman.entries.rosa.pacman.behaviortree.MyPacMan;
import pacman.entries.rosa.pacman.GameState;

/**
 *
 * @author romsahel
 */
public abstract class Leaf extends Node
{
    protected final MyPacMan parent;
    protected final GameState state;

    public Leaf(MyPacMan parent)
    {
        this.parent = parent;
        this.state = parent.getState();
    }

}
