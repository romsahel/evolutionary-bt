/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pacman.entries.pacman.behaviortree;

import java.util.ArrayList;
import java.util.Random;
import pacman.entries.pacman.behaviortree.helpers.Composite;
import pacman.entries.pacman.behaviortree.helpers.Leaf;
import pacman.entries.pacman.behaviortree.helpers.Node;

/**
 *
 * @author Romsahel
 */
public class EvolvingBTPacMan extends BTPacMan
{

	private static final Random random = new Random();

	public EvolvingBTPacMan(Composite rootNode)
	{
		super(rootNode);
	}

	public EvolvingBTPacMan()
	{
		super();
	}

	public EvolvingBTPacMan(BTPacMan parent1, BTPacMan parent2)
	{
	}

	public static BTPacMan Mutate(BTPacMan parent)
	{
		BTPacMan mutatedParent = parent;

		int index = random.nextInt(mutatedParent.treeGenerator.getLeaves().size());
		Node leaf = mutatedParent.treeGenerator.getLeaves().get(index);

		if (leaf.type == Node.Type.Condition)
			if (random.nextBoolean())
			{
				for (int i = 0; i < leaf.parent.getChildrenCount(); i++)
					if (leaf.equals(leaf.parent.getChildren().get(i)))
					{
						leaf.parent.getChildren().remove(i);
						break;
					}
				mutatedParent.treeGenerator.getLeaves().remove(index);
			}
			else
				switchLeaf(mutatedParent.treeGenerator.setOfConditions, leaf, mutatedParent);
		else
			switchLeaf(mutatedParent.treeGenerator.setOfActions, leaf, mutatedParent);

		return mutatedParent;
	}

	private static void switchLeaf(final Leaf[] set, Node leaf, BTPacMan mutatedParent)
	{
		Leaf newLeaf = set[random.nextInt(set.length)];
		while (newLeaf.equals(leaf))
			newLeaf = set[random.nextInt(set.length)];

		replaceNode(leaf, newLeaf, mutatedParent.treeGenerator.getLeaves());
	}

	public static BTPacMan[] combine(BTPacMan parent1, BTPacMan parent2)
	{
		// create two trees from the two parents
		BTPacMan[] result = new BTPacMan[]
		{
			new BTPacMan(parent1.getRootNode().copy()),
			new BTPacMan(parent2.getRootNode().copy()),
		};

		// get a random leaf from the parent1: node1
		final ArrayList<Node> leaves1 = result[0].treeGenerator.getLeaves();
		final Node node1 = leaves1.get(random.nextInt(leaves1.size()));

		// get a random leaf from the parent2, matching the type of node1: node2
		final ArrayList<Node> leaves2 = result[1].treeGenerator.getLeaves();
		Node node2 = leaves2.get(random.nextInt(leaves2.size()));
		while (node2.type != node1.type)
			node2 = leaves2.get(random.nextInt(leaves2.size()));

		// replace node1 by node2 in result[0] (copy of parent1)
		replaceNode(node1, node2, leaves1);

		// replace node2 by node1 in result[1] (copy of parent2)
		replaceNode(node2, node1, leaves2);

		return result;
	}

	/**
	 * Replaces node1 by node2 in the tree.
	 * Go through node1's parent's list of children until node1 is found, removes it and add node2 in its
	 * place
	 */
	private static void replaceNode(final Node node1, final Node node2, final ArrayList<Node> leaves)
	{
		final ArrayList<Node> children = node1.parent.getChildren();
		for (int i = 0; i < children.size(); i++)
			if (children.get(i) == node1)
			{
				children.set(i, node2);
				leaves.set(leaves.indexOf(node1), node2);
				break;
			}
	}
}
