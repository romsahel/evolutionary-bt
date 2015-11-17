package pacman.entries.pacman.behaviortree;

import pacman.controllers.Controller;
import pacman.entries.pacman.GameState;
import pacman.entries.pacman.behaviortree.helpers.Composite;
import pacman.entries.pacman.behaviortree.helpers.Leaf;
import pacman.entries.pacman.behaviortree.helpers.Node;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
		this.rootNode = treeGenerator.generate();
	}

	public BTPacMan(BTPacMan parent1, BTPacMan parent2)
	{
		this.rootNode = null;
		// combine
		throw new UnsupportedOperationException("Not implemented yet");
	}

    public static BTPacMan Mutate(BTPacMan parent){
        BTPacMan mutatedParent = parent;

        Random random = new Random();
        int index = random.nextInt(mutatedParent.treeGenerator.getLeaves().size());
        Node leaf = mutatedParent.treeGenerator.getLeaves().get(index);

        if(leaf.type == Node.Type.Condition){
            if(random.nextBoolean()){
                for(int i = 0; i < leaf.parent.getChildrenCount(); i++){
                    if(leaf.equals(leaf.parent.getChildren().get(i))){
                        leaf.parent.getChildren().remove(i);
                    }
                }

                mutatedParent.treeGenerator.getLeaves().remove(index);
            }
            else{
                Leaf newLeaf = mutatedParent.treeGenerator.setOfConditions[random.nextInt(mutatedParent.treeGenerator.setOfConditions.length)];
                while(newLeaf.equals(leaf)){
                    newLeaf = mutatedParent.treeGenerator.setOfConditions[random.nextInt(mutatedParent.treeGenerator.setOfConditions.length)];
                }

                for(int i = 0; i < leaf.parent.getChildrenCount(); i++){
                    if(leaf.equals(leaf.parent.getChildren().get(i))){
                        leaf.parent.getChildren().set(i, newLeaf);
                    }
                }

                mutatedParent.treeGenerator.getLeaves().set(index, newLeaf);
            }
        }
        else{
            Leaf newLeaf = mutatedParent.treeGenerator.setOfActions[random.nextInt(mutatedParent.treeGenerator.setOfActions.length)];
            while(newLeaf.equals(leaf)){
                newLeaf = mutatedParent.treeGenerator.setOfActions[random.nextInt(mutatedParent.treeGenerator.setOfActions.length)];
            }

            for(int i = 0; i < leaf.parent.getChildrenCount(); i++){
                if(leaf.equals(leaf.parent.getChildren().get(i))){
                    leaf.parent.getChildren().set(i, newLeaf);
                }
            }

            mutatedParent.treeGenerator.getLeaves().set(index, newLeaf);
        }

        return mutatedParent;
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
