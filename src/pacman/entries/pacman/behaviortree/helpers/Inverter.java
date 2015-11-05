package pacman.entries.pacman.behaviortree.helpers;

import pacman.game.Game;

public class Inverter extends Node
{
	private Leaf invertedNode;

	public Inverter(Leaf leaf)
	{
		this.invertedNode = leaf;
	}
	
	@Override
	public boolean DoAction(Game game)
	{
		return !invertedNode.DoAction(game);
	}
	
	public Leaf getInvertedNode()
	{
		return invertedNode;
	}

}
