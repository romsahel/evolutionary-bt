package pacman.entries.rosa.pacman;

import pacman.game.Constants;
import pacman.game.Game;

/**
 * Represents a ghost with required information:
 * its type, its position and the distance to the player.
 * 
 * @author romsahel
 */
public class NearGhost
{

    private final Constants.GHOST type;
    private final int index;
    private final double distance;

    public NearGhost(Game game, Constants.GHOST type, double distance)
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

    @Override
    public String toString()
    {
        return type + " (" + index + ")" + ": at " + distance;
    }
    
    

}
