package pacman.entries.pacman.mcts;

import java.util.ArrayList;

import pacman.game.Constants.MOVE;
import pacman.game.Game;

/**
 * Represents a Node in a tree
 * @author Romsahel
 *
 */
public class Node
{
	private static final double C = 2;

	private final Node parent;
	private final ArrayList<Node> children;
	private final MOVE move;
	private final Game game;

	private int visits;
	private double score;

	public Node(Game state)
	{
		this(state, null, null);
	}

	public Node(Game state, Node parent, MOVE move)
	{
		this.children = new ArrayList<>();

		this.visits = 1;
		this.score = 0;

		this.game = state;
		this.parent = parent;
		this.move = move;
	}

	public void visit()
	{
		this.visits++;
	}

	public void addScore(double score)
	{
		this.score += score;
	}

	public double getUCTScore()
	{
		if (game.gameOver())
			return 0;
		double exploration = Math.log(parent.getVisits()) / visits;
		return (score / visits) + C * Math.sqrt(exploration);
	}

	public Node getBestUCTChild()
	{
		Node bestChild = null;
		double score = Double.NEGATIVE_INFINITY;
		for (Node node : children)
		{
			double uctScore = node.getUCTScore();
			if (uctScore > score)
			{
				bestChild = node;
				score = uctScore;
			}
		}
		return bestChild;
	}
	
	public Node getBestChild()
	{
		Node bestChild = null;
		double score = Double.NEGATIVE_INFINITY;
		for (Node node : children)
		{
			double uctScore = node.getScore();
			if (uctScore > score)
			{
				bestChild = node;
				score = uctScore;
			}
		}
		return bestChild;
	}

	public double getScore()
	{
		return score;
	}

	public void setScore(double score)
	{
		this.score = score;
	}

	public ArrayList<Node> getChildren()
	{
		return children;
	}

	public Node getParent()
	{
		return parent;
	}

	public MOVE getMove()
	{
		return move;
	}

	public int getVisits()
	{
		return visits;
	}

	public Game getGameCopy()
	{
		return game.copy();
	}

	public Game getGame()
	{
		return game;
	}
}