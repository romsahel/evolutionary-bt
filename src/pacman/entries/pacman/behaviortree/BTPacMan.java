package pacman.entries.pacman.behaviortree;

import pacman.controllers.Controller;
import pacman.entries.pacman.GameState;
import pacman.entries.pacman.behaviortree.helpers.Composite;
import pacman.entries.pacman.behaviortree.helpers.Leaf;
import pacman.entries.pacman.behaviortree.helpers.Node;
import pacman.entries.pacman.behaviortree.helpers.Selector;
import pacman.entries.pacman.behaviortree.helpers.Sequence;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class BTPacMan extends Controller<MOVE>
		implements Comparable<BTPacMan>, java.io.Serializable
{
	/*
	 * Root of the behavior tree
	 */

	final Composite rootNode;
	final GameState state = new GameState();
	private final TreeGenerator treeGenerator = new TreeGenerator();
	private int score;


	/*
	 * Type of MOVE that will be returned at each game step
	 */
	private MOVE move;

	public BTPacMan(Composite rootNode)
	{
		this.rootNode = copy(rootNode);
		treeGenerator.setRoot(this.rootNode);
	}

	public BTPacMan()
	{
		this.rootNode = treeGenerator.generate();
	}

	protected final Composite copy(Composite copy)
	{
		Composite newRoot = (copy instanceof Selector) ? new Selector() : new Sequence();

		for (Node c : copy.getChildren())
			if (c instanceof Composite)
			{
				final Composite root = copy((Composite) c);
				root.parent = newRoot;
				treeGenerator.getComposites().add(root);
				newRoot.addChildren(root);
			}
			else
			{
				Leaf leaf = (Leaf) c;
				final Leaf newLeaf = new Leaf(leaf.getTask(), leaf.isInverter());
				newLeaf.type = c.type;
				newLeaf.parent = newRoot;
				newRoot.addChildren(newLeaf);
				treeGenerator.getLeaves().add(newLeaf);
			}
		return newRoot;
	}

	@Override
	public MOVE getMove(Game game, long timeDue)
	{
		state.update(game);
		rootNode.DoAction(game, this, this.getState());
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

	public void setScore(int score)
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

	/**
	 * @return the treeGenerator
	 */
	public TreeGenerator getTreeGenerator()
	{
		return treeGenerator;
	}
}
