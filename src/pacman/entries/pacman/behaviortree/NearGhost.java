package pacman.entries.pacman.behaviortree;

import pacman.game.Constants;
import pacman.game.Game;

/**
 *
 * @author romsahel
 */
public class NearGhost
{

    private Constants.GHOST type;
    private int index;
    private double distance;

    public void update(Game game, Constants.GHOST type, double distance)
    {
        this.type = type;
        this.index = game.getGhostCurrentNodeIndex(type);
        this.distance = distance;
    }

    /**
     * @return the type
     */
    public Constants.GHOST getType()
    {
        return type;
    }

    /**
     * @return the index
     */
    public int getIndex()
    {
        return index;
    }

    /**
     * @return the distance
     */
    public double getDistance()
    {
        return distance;
    }

}
