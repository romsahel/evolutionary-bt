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
import pacman.entries.pacman.behaviortree.helpers.Selector;
import pacman.entries.pacman.behaviortree.helpers.Sequence;
import pacman.entries.pacman.behaviortree.helpers.Task;
import pacman.entries.pacman.behaviortree.tasks.actions.AvoidPowerpillTask;
import pacman.entries.pacman.behaviortree.tasks.actions.ChasePowerPillTask;
import pacman.entries.pacman.behaviortree.tasks.actions.ChaseTask;
import pacman.entries.pacman.behaviortree.tasks.actions.EatPillTask;
import pacman.entries.pacman.behaviortree.tasks.actions.GoToJunctionTask;
import pacman.entries.pacman.behaviortree.tasks.actions.RunAwayMultipleTask;
import pacman.entries.pacman.behaviortree.tasks.actions.RunAwayTask;
import pacman.entries.pacman.behaviortree.tasks.conditions.AreGhostFarAwayTask;
import pacman.entries.pacman.behaviortree.tasks.conditions.IsGhostCloserTask;
import pacman.entries.pacman.behaviortree.tasks.conditions.IsGhostEdibleTask;
import pacman.entries.pacman.behaviortree.tasks.conditions.IsGhostNearTask;
import pacman.entries.pacman.behaviortree.tasks.conditions.IsPacmanAtJunction;
import pacman.entries.pacman.behaviortree.tasks.conditions.IsPathToJunctionSafeTask;
import pacman.entries.pacman.behaviortree.tasks.conditions.IsPathToPowerPillSafeTask;

/**
 *
 * @author Romsahel
 */
public class TreeGenerator
{

	/**
	 * Parameters
	 */
	private static final int MAX_DEPTH = 4;
	private static final int MAX_CHILDREN = 3;

	/**
	 *
	 */
	private static final int LEAF_TYPE = 0;
	private static final int SELECTOR_TYPE = 1;
	private static final int SEQUENCE_TYPE = 2;

	private Composite rootNode;
	public final Task[] setOfActions, setOfConditions;
	private final ArrayList<Leaf> leaves = new ArrayList<>();
	private final ArrayList<Composite> composites = new ArrayList<>();

	private static final Random random = new Random();

	public TreeGenerator(BTPacMan thisPacman)
	{
		setOfActions = new Task[]
		{
			new AvoidPowerpillTask(thisPacman),
			new ChasePowerPillTask(thisPacman),
			new ChaseTask(thisPacman),
			new EatPillTask(thisPacman),
			new GoToJunctionTask(thisPacman),
			new RunAwayMultipleTask(thisPacman),
			new RunAwayTask(thisPacman)
		};
		setOfConditions = new Task[]
		{
			new AreGhostFarAwayTask(thisPacman),
			new IsGhostCloserTask(thisPacman),
			new IsGhostEdibleTask(thisPacman),
			new IsGhostNearTask(thisPacman),
			new IsPacmanAtJunction(thisPacman),
			new IsPathToJunctionSafeTask(thisPacman),
			new IsPathToPowerPillSafeTask(thisPacman)
		};
	}

	public Composite generate()
	{
		return generate(false);
	}

	public Composite generate(boolean print)
	{
		rootNode = new Selector();
		rootNode.setMaxDepth(MAX_DEPTH);
		generateChildren(rootNode);
		if (print)
			this.print();
		return rootNode;
	}

	private Node generate(Composite root)
	{
		int type;
		if (root.getDepth() >= root.getMaxDepth())
			type = LEAF_TYPE;
		else
			type = random.nextInt(SEQUENCE_TYPE + 1);

		if (type == LEAF_TYPE)
		{
			Leaf newLeaf;
			if (root.getChildrenCount() == root.nbChildren - 1)
			{
				newLeaf = new Leaf(setOfActions[random.nextInt(setOfActions.length)], false);
				newLeaf.type = Node.Type.Action;
			}
			else
			{
				newLeaf = generateCondition(root);
				newLeaf.type = Node.Type.Condition;
			}

			leaves.add(newLeaf);
			newLeaf.parent = root;
			return newLeaf;
		}
		else
		{
			final Composite newComposite = generateComposite(type, root);
			newComposite.parent = root;
			composites.add(newComposite);
			return newComposite;
		}
	}

	private Composite generateComposite(int type, Composite root)
	{
		Composite node;
		if (type == SELECTOR_TYPE)
			node = new Selector();
		else
			node = new Sequence();
		node.setDepth(root.getDepth() + 1);
		node.setMaxDepth(random.nextInt(MAX_DEPTH + 1));
		generateChildren(node);
		return node;
	}

	private Leaf generateCondition(Composite root)
	{
		Task task = null;
		do
		{
			task = setOfConditions[random.nextInt(setOfConditions.length)];
		} while (rootHasNode(root, task));

		return new Leaf(task, (random.nextInt(100) < 10));
	}

	private void generateChildren(Composite node)
	{
		node.nbChildren = random.nextInt(MAX_CHILDREN + 1 - 2) + 2;
		for (int i = 0; i < node.nbChildren; i++)
			node.addChildren(generate(node));
	}

	private boolean rootHasNode(Composite root, Task leaf)
	{
		for (Node n : root.getChildren())
			if (n instanceof Leaf)
				if (((Leaf) n).getTask().getClass() == leaf.getClass())
					return true;
		return false;
	}

	public void print()
	{
		TreePrinter.getInstance().print(rootNode, setOfConditions);
	}

	public Composite getRoot()
	{
		return rootNode;
	}

	public void setRoot(Composite rootNode)
	{
		this.rootNode = rootNode;
	}

	public ArrayList<Leaf> getLeaves()
	{
		return leaves;
	}

	/**
	 * @return the composites
	 */
	public ArrayList<Composite> getComposites()
	{
		return composites;
	}
}
