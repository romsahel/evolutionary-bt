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

	private final Composite rootNode;
	private final GameState state = new GameState();
	private final TreeGenerator treeGenerator = new TreeGenerator(this);
	private double score;

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
		this.rootNode = treeGenerator.generate(true);
	}

	public BTPacMan(BTPacMan parent1, BTPacMan parent2)
	{
		this.rootNode = null;
		// combine
		throw new UnsupportedOperationException("Not implemented yet");
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
}