package pacman.entries.rosa.pacman.behaviortree.helpers;

import pacman.game.Game;

/**
 * The most basic class of the behavior tree:
 * The node. Every classes that are part of the tree should inherit from this one
 * @author romsahel
 */
public abstract class Node
{
    public abstract boolean DoAction(Game game);
}
