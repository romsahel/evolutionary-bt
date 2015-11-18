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
import pacman.entries.pacman.behaviortree.helpers.Task;

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

	public EvolvingBTPacMan(EvolvingBTPacMan copy)
	{
		super(copy);
	}

	public static EvolvingBTPacMan Mutate(EvolvingBTPacMan parent)
	{
		EvolvingBTPacMan mutatedParent = parent;

		int index = random.nextInt(mutatedParent.treeGenerator.getLeaves().size());
		Leaf leaf = mutatedParent.treeGenerator.getLeaves().get(index);

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

	private static void switchLeaf(final Task[] set, Leaf leaf, EvolvingBTPacMan mutatedParent)
	{
		Task newTask = set[random.nextInt(set.length)];
		while (newTask.equals(leaf.getTask()))
			newTask = set[random.nextInt(set.length)];

		replaceLeaf(leaf, new Leaf(newTask, false), mutatedParent.treeGenerator.getLeaves());
	}

	public static EvolvingBTPacMan[] combine(EvolvingBTPacMan parent1, EvolvingBTPacMan parent2)
	{
		// create two trees from the two parents
		EvolvingBTPacMan[] result = new EvolvingBTPacMan[]
		{
			new EvolvingBTPacMan(parent1),
			new EvolvingBTPacMan(parent2),
		};

		// get a random leaf from the parent1: node1
		final ArrayList<Leaf> leaves1 = result[0].treeGenerator.getLeaves();
		final Leaf node1 = leaves1.get(random.nextInt(leaves1.size()));

		// get a random leaf from the parent2, matching the type of node1: node2
		final ArrayList<Leaf> leaves2 = result[1].treeGenerator.getLeaves();
		Leaf node2 = leaves2.get(random.nextInt(leaves2.size()));
		while (node2.type != node1.type || node2.getTask().getClass() == node1.getTask().getClass())
			node2 = leaves2.get(random.nextInt(leaves2.size()));

		System.out.println(node1.getTask().getClass().getSimpleName());
		System.out.println(node2.getTask().getClass().getSimpleName());

		// replace node1 by node2 in result[0] (copy of parent1)
		replaceLeaf(node1, node2, leaves1);

		// replace node2 by node1 in result[1] (copy of parent2)
		replaceLeaf(node2, node1, leaves2);

		return result;
	}

	/**
	 * Replaces node1 by node2 in the tree.
	 * Go through node1's parent's list of children until node1 is found, removes it and add node2 in its
	 * place
	 */
	private static void replaceLeaf(final Leaf node1, final Leaf node2, final ArrayList<Leaf> leaves)
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
