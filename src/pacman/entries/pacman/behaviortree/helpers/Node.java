package pacman.entries.pacman.behaviortree.helpers;

import pacman.game.Game;

/**
 * The most basic class of the behavior tree:
 * The node. Every classes that are part of the tree should inherit from this one
 * @author romsahel
 */
public abstract class Node implements java.io.Serializable
{
    public enum Type
    {
        Composite,
        Condition,
        Action,
    }
    public Type type = Type.Composite;
    public Composite parent = null;

    private int depth;
    public abstract boolean DoAction(Game game);

    public int getDepth()
	{
		return depth;
	}
	public void setDepth(int depth)
	{
		this.depth = depth;
	}
}
