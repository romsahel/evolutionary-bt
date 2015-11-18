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
public class EvolvingBTPacMan extends BTPacMan implements java.io.Serializable
{

	private static final Random random = new Random();

	public EvolvingBTPacMan(Composite rootNode)
	{
		super(rootNode);
	}

	public EvolvingBTPacMan(EvolvingBTPacMan copy)
	{
		super(copy.rootNode);
	}
	public EvolvingBTPacMan()
	{
		super();
	}

	public static EvolvingBTPacMan Mutate(EvolvingBTPacMan parent)
	{
		EvolvingBTPacMan mutatedParent = parent;

		int index = random.nextInt(mutatedParent.getTreeGenerator().getLeaves().size());
		Leaf leaf = mutatedParent.getTreeGenerator().getLeaves().get(index);

		if (leaf.type == Node.Type.Condition)
			if (random.nextBoolean())
			{
				for (int i = 0; i < leaf.parent.getChildrenCount(); i++)
					if (leaf.equals(leaf.parent.getChildren().get(i)))
					{
						leaf.parent.getChildren().remove(i);
						break;
					}
				mutatedParent.getTreeGenerator().getLeaves().remove(index);
			}
			else
				switchLeaf(mutatedParent.getTreeGenerator().setOfConditions, leaf, mutatedParent);
		else
			switchLeaf(mutatedParent.getTreeGenerator().setOfActions, leaf, mutatedParent);

		return mutatedParent;
	}

	private static void switchLeaf(final Task[] set, Leaf leaf, EvolvingBTPacMan mutatedParent)
	{
		Task newTask = set[random.nextInt(set.length)];
		while (newTask.equals(leaf.getTask()))
			newTask = set[random.nextInt(set.length)];

		replaceNode(leaf, new Leaf(newTask, false), mutatedParent.getTreeGenerator().getLeaves());
	}

	public static EvolvingBTPacMan[] combine(EvolvingBTPacMan parent1, EvolvingBTPacMan parent2)
	{
		final boolean isComposite = random.nextBoolean();
		final EvolvingBTPacMan[] result = combine(parent1, parent2, isComposite);
		if (result == null)
			return combine(parent1, parent2, !isComposite);
		return result;
	}

	/**
	 * create two trees from the two parents
	 * get a random leaf from the parent1: node1
	 * get a random leaf from the parent2, matching the type of node1: node2
	 * <p>
	 * @return
	 */
	public static EvolvingBTPacMan[] combine(EvolvingBTPacMan parent1, EvolvingBTPacMan parent2, boolean isComposite)
	{
		final EvolvingBTPacMan[] result = new EvolvingBTPacMan[]
		{
			new EvolvingBTPacMan(parent1),
			new EvolvingBTPacMan(parent2),
		};

		final ArrayList<? extends Node> list1 = getList(result[0], isComposite);
		final ArrayList<? extends Node> list2 = getList(result[1], isComposite);

		if (list1.size() == 0 || list2.size() == 0)
			return null;

		final Node node1 = list1.get(random.nextInt(list1.size()));
		Node node2 = list2.get(random.nextInt(list2.size()));

		if (isComposite)
			combineComposites(result, (Composite) node1, (Composite) node2, list1, list2);
		else
			combineLeaves(result, (Leaf) node1, (Leaf) node2, list1, list2);

		return result;
	}

	private static ArrayList<? extends Node> getList(EvolvingBTPacMan bt, boolean isComposite)
	{
		return (isComposite) ? bt.getTreeGenerator().getComposites() : bt.getTreeGenerator().getLeaves();
	}

	private static void combineComposites(final EvolvingBTPacMan[] result,
										  Composite node1, Composite node2,
										  ArrayList<? extends Node> list1,
										  ArrayList<? extends Node> list2)
	{
		trimTree(result[0], node1);
		Composite newNode2 = result[0].copy(node2);
		newNode2.parent = node1.parent;
		replaceNode(node1, newNode2, (ArrayList<Composite>) list1);

		trimTree(result[1], node2);
		Composite newNode1 = result[1].copy(node1);
		newNode1.parent = node2.parent;
		replaceNode(node2, newNode1, (ArrayList<Composite>) list2);

//		printCompositeCombination(node1, node2);
	}

	private static void printCompositeCombination(Composite node1, Composite node2)
	{
		System.out.print(node1.getClass().getSimpleName());
		System.out.print(" (" + node1.type + ") ");
		System.out.print(" <--> ");
		System.out.print(node2.getClass().getSimpleName());
		System.out.print(" (" + node2.type + ") ");
		System.out.println();
	}

	private static void trimTree(EvolvingBTPacMan tree, Composite subtree)
	{
		tree.getTreeGenerator().getComposites().remove((Composite) subtree);
		for (Node child : subtree.getChildren())
			if (child.type == Node.Type.Composite)
				trimTree(tree, (Composite) child);
			else
				tree.getTreeGenerator().getLeaves().remove((Leaf) child);
	}

	private static void combineLeaves(final EvolvingBTPacMan[] result,
									  Leaf node1, Leaf node2,
									  ArrayList<? extends Node> leaves1,
									  ArrayList<? extends Node> leaves2)
	{
		while (node2.type != node1.type || node2.getTask().getClass() == node1.getTask().getClass())
			node2 = (Leaf) leaves2.get(random.nextInt(leaves2.size()));

//		printLeaveCombination(node1, node2);

		// replace node1 by node2 in result[0] (copy of parent1)
		replaceNode(node1, new Leaf(node2, node1.parent, result[0]), (ArrayList<Leaf>) leaves1);
		// replace node2 by node1 in result[1] (copy of parent2)
		replaceNode(node2, new Leaf(node1, node2.parent, result[1]), (ArrayList<Leaf>) leaves2);
	}

	private static void printLeaveCombination(Leaf node1, Leaf node2)
	{
		System.out.print(((node1.isInverter()) ? "NOT " : "") + node1.getTask().getClass().getSimpleName());
		System.out.print(" (" + node1.type + ") ");
		System.out.print(" <--> ");
		System.out.print(((node2.isInverter()) ? "NOT " : "") + node2.getTask().getClass().getSimpleName());
		System.out.print(" (" + node2.type + ") ");
		System.out.println();
	}

	/**
	 * Replaces node1 by node2 in the tree.
	 * Go through node1's parent's list of children until node1 is found, removes it and add node2 in its
	 * place
	 */
	private static <T extends Node> void replaceNode(final T node1, final T node2, final ArrayList<T> list)
	{
		final ArrayList<Node> children = node1.parent.getChildren();
		for (int i = 0; i < children.size(); i++)
			if (children.get(i) == node1)
			{
				children.set(i, node2);
				final int index = list.indexOf(node1);
				if (index > -1)
					list.set(index, node2);
				break;
			}
	}
}
