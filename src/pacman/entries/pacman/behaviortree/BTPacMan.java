package pacman.entries.pacman.behaviortree;

import pacman.controllers.Controller;
import pacman.entries.pacman.GameState;
import pacman.entries.pacman.behaviortree.helpers.Composite;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class BTPacMan extends Controller<MOVE>
		implements Comparable<BTPacMan>
{
	/*
	 * Root of the behavior tree
	 */

	final Composite rootNode;
	final GameState state = new GameState();
	final TreeGenerator treeGenerator = new TreeGenerator(this);
	double score;


	/*
	 * Type of MOVE that will be returned at each game step
	 */
	private MOVE move;

	public BTPacMan(Composite rootNode)
	{
		this.rootNode = rootNode;
		treeGenerator.setRoot(this.rootNode);
	}

	public BTPacMan()
	{
		this.rootNode = treeGenerator.generate();
	}

	@Override
	public MOVE getMove(Game game, long timeDue)
	{
		state.update(game);
		rootNode.DoAction(game);
		return move;
	}

	/**
	 * @param move
	 *             the move to set
	 */
	public void setMove(MOVE move)
	{
		this.move = move;
	}

	public GameState getState()
	{
		return state;
	}

	public double getScore()
	{
		return score;
	}

	public void setScore(double score)
	{
		this.score = score;
	}

	@Override
	public int compareTo(BTPacMan o)
	{
		return (int) (o.getScore() - this.getScore());
	}

	/**
	 * @return the rootNode
	 */
	public Composite getRootNode()
	{
		return rootNode;
	}

	public void print()
	{
		treeGenerator.print();
	}
}
