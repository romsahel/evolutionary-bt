/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pacman.entries.pacman.behaviortree;

import java.util.ArrayList;
import java.util.Random;
import pacman.entries.pacman.behaviortree.helpers.Composite;
import pacman.entries.pacman.behaviortree.helpers.Inverter;
import pacman.entries.pacman.behaviortree.helpers.Leaf;
import pacman.entries.pacman.behaviortree.helpers.Node;
import pacman.entries.pacman.behaviortree.helpers.Selector;
import pacman.entries.pacman.behaviortree.helpers.Sequence;
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
	public final Leaf[] setOfActions, setOfConditions;
	private ArrayList<Node> leaves = new ArrayList<>();

	private static final Random random = new Random();

	public TreeGenerator(BTPacMan thisPacman)
	{
		setOfActions = new Leaf[]
		{
			new AvoidPowerpillTask(thisPacman),
			new ChasePowerPillTask(thisPacman),
			new ChaseTask(thisPacman),
			new EatPillTask(thisPacman),
			new GoToJunctionTask(thisPacman),
			new RunAwayMultipleTask(thisPacman),
			new RunAwayTask(thisPacman)
		};
		setOfConditions = new Leaf[]
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
                    Node leaf = null;
                    
			if (root.getChildrenCount() == root.nbChildren - 1)
				leaf = setOfActions[random.nextInt(setOfActions.length)];
			else
				leaf = generateCondition(root);
                        leaves.add(leaf);
                        return leaf;
                }
                else
			return generateComposite(type, root);
	}

	private Node generateComposite(int type, Composite root)
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

	private Node generateCondition(Composite root)
	{
		Leaf leaf = null;
		do
		{
			leaf = setOfConditions[random.nextInt(setOfConditions.length)];
		} while (rootHasNode(root, leaf));

		if (random.nextInt(100) < 10)
			return new Inverter(leaf);
		return leaf;
	}

	private void generateChildren(Composite node)
	{
		node.nbChildren = random.nextInt(MAX_CHILDREN + 1 - 2) + 2;
		for (int i = 0; i < node.nbChildren; i++)
			node.addChildren(generate(node));
	}

	private boolean rootHasNode(Composite root, Leaf leaf)
	{
		for (Node n : root.getChildren())
			if (n.getClass() == leaf.getClass()
				|| ((n instanceof Inverter)
					&& ((Inverter) n).getInvertedNode().getClass() == leaf.getClass()))
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
        
        public ArrayList<Node> getLeaves() 
        {
            return leaves;
        }
}
