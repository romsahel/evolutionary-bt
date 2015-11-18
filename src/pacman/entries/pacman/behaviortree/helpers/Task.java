package pacman.entries.pacman.behaviortree.helpers;

import pacman.entries.pacman.GameState;
import pacman.entries.pacman.behaviortree.BTPacMan;
import pacman.game.Game;

/**
 *
 * @author romsahel
 */
public abstract class Task
{
    protected final BTPacMan parent;
    protected final GameState state;

    public Task(BTPacMan parent)
    {
        this.parent = parent;
        this.state = parent.getState();
    }

    public abstract boolean DoAction(Game game);
}
