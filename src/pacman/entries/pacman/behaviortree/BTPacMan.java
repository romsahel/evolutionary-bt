package pacman.entries.pacman.behaviortree;

import pacman.controllers.Controller;
import pacman.entries.pacman.GameState;
import pacman.entries.pacman.behaviortree.helpers.Composite;
import pacman.entries.pacman.behaviortree.helpers.Leaf;
import pacman.entries.pacman.behaviortree.helpers.Node;
import pacman.entries.pacman.behaviortree.helpers.Selector;
import pacman.entries.pacman.behaviortree.helpers.Sequence;
import pacman.entries.pacman.behaviortree.helpers.Task;
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
	private final TreeGenerator treeGenerator = new TreeGenerator(this);
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

	public BTPacMan(BTPacMan copy)
	{
		this.rootNode = copy(copy.getRootNode());
		treeGenerator.setRoot(this.rootNode);
	}

	protected Composite copy(Composite copy)
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
				final Task[] set = (c.type == Node.Type.Action)
								   ? treeGenerator.setOfActions
								   : treeGenerator.setOfConditions;

				Leaf leaf = (Leaf) c;
				for (Task task : set)
					if (task.getClass() == leaf.getTask().getClass())
					{
						final Leaf newLeaf = new Leaf(task, leaf.isInverter());
						newLeaf.type = c.type;
						newLeaf.parent = newRoot;
						newRoot.addChildren(newLeaf);
						treeGenerator.getLeaves().add(newLeaf);
					}
			}
		return newRoot;
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

	/**
	 * @return the treeGenerator
	 */
	public TreeGenerator getTreeGenerator()
	{
		return treeGenerator;
	}
}
