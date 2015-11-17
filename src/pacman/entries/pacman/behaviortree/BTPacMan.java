package pacman.entries.pacman.behaviortree;

import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import pacman.controllers.Controller;
import pacman.entries.pacman.GameState;
import pacman.entries.pacman.behaviortree.helpers.Composite;
import pacman.entries.pacman.behaviortree.helpers.Inverter;
import pacman.entries.pacman.behaviortree.helpers.Leaf;
import pacman.entries.pacman.behaviortree.helpers.Node;
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
        
	private static final Random random = new Random();

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

	public BTPacMan(BTPacMan parent1, BTPacMan parent2)
	{
		this.rootNode = null;
		// combine
		throw new UnsupportedOperationException("Not implemented yet");
	}

        public static BTPacMan[] combine(BTPacMan parent1, BTPacMan parent2)
        {
            // create two trees from the two parents
            BTPacMan[] result = new BTPacMan[] {
                new BTPacMan(parent1.getRootNode().copy()),
                new BTPacMan(parent2.getRootNode().copy()),
            };
            
            // get a random leaf from the parent1: node1
            final ArrayList<Node> leaves1 = result[0].treeGenerator.getLeaves();
            final Node node1 = leaves1.get(random.nextInt(leaves1.size()));
            
            // get a random leaf from the parent2, matching the type of node1: node2
            final ArrayList<Node> leaves2 = result[1].treeGenerator.getLeaves();
            Node node2 = leaves2.get(random.nextInt(leaves2.size()));
            while (node2.type !=  node1.type)
                node2 = leaves2.get(random.nextInt(leaves2.size()));
 
            // replace node1 by node2 in result[0] (copy of parent1)
            final ArrayList<Node> children1 = node1.parent.getChildren();
            for (int i = 0; i < children1.size(); i++) 
            {
                if (children1.get(i) == node1)
                {
                    children1.remove(i);
                    children1.add(i, node2);
                    leaves1.remove(node1);
                    leaves1.add(node2);
                    break;
                }
            }
            
            // replace node2 by node1 in result[1] (copy of parent2)
            final ArrayList<Node> children2 = node2.parent.getChildren();
            for (int i = 0; i < children2.size(); i++) 
            {
                if (children2.get(i) == node2)
                {
                    children2.remove(i);
                    children2.add(i, node1);
                    leaves1.remove(node2);
                    leaves1.add(node1);
                    break;
                }
            }
            
            return result;
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
