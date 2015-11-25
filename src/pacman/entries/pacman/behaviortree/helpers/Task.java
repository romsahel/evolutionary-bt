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
    protected transient final BTPacMan parent = null;
    protected transient final GameState state = null;

    public Task()
    {
    }

    public abstract boolean DoAction(Game game, BTPacMan parent, GameState state);
}
