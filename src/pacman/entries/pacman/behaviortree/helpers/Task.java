package pacman.entries.pacman.behaviortree.helpers;

import pacman.entries.pacman.GameState;
import pacman.entries.pacman.behaviortree.BTPacMan;
import pacman.game.Game;

/**
 *
 * @author romsahel
 */
public abstract class Task implements java.io.Serializable
{
    protected transient final BTPacMan parent;
    protected transient final GameState state;

    public Task(BTPacMan parent)
    {
        this.parent = parent;
        this.state = parent.getState();
    }

    public abstract boolean DoAction(Game game);
}
