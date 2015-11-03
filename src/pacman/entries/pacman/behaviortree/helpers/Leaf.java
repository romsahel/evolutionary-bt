package pacman.entries.pacman.behaviortree.helpers;

import pacman.entries.pacman.GameState;
import pacman.entries.pacman.behaviortree.MyPacMan;

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
