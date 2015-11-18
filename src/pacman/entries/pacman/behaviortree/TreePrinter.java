/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pacman.entries.pacman.behaviortree;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import pacman.entries.pacman.behaviortree.helpers.Composite;
import pacman.entries.pacman.behaviortree.helpers.Leaf;
import pacman.entries.pacman.behaviortree.helpers.Node;
import pacman.entries.pacman.behaviortree.helpers.Selector;
import pacman.entries.pacman.behaviortree.helpers.Task;

/**
 *
 * @author Romsahel
 */
public class TreePrinter
{

	private static List<Task> setOfConditions;
	private static HashMap<String, Integer> map = new HashMap<>();
	private static boolean hasBeenCalled = false;

	private TreePrinter()
	{
	}

	public void print(Composite root, Task[] setOfConditions)
	{
		if (!hasBeenCalled)
		{
			hasBeenCalled = true;
			System.out.println("#lineWidth: 2\n" + "#padding: 12\n" + "#arrowSize: 0.7\n");
		}
		
		TreePrinter.setOfConditions = Arrays.asList(setOfConditions);
		final int nodeId = getId(root.getClass().getSimpleName());
		System.out.println("[" + getClassifier(root) + " " + nodeId + "]");
		printTree(root, nodeId);
	}

	private void printTree(Composite root, int id)
	{
		for (Node node : root.getChildren())
		{
			int nodeId = getId(node.getClass().getSimpleName());
			System.out.println("[" + root.getClass().getSimpleName() + " " + id + "] -> " + nodeToString(node, nodeId));

			if (node instanceof Composite)
			{
				Composite composite = (Composite) node;
				printTree(composite, nodeId);
			}
		}
	}

	private String nodeToString(Node node, int nodeId)
	{
		return "[" + getClassifier(node) + " " + nodeId + "]";
	}

	private String getClassifier(Node node)
	{
		String classifier = "";
		if (node instanceof Composite)
		{
			if (node.getClass() == Selector.class)
				classifier = "<choice> ";
			else
				classifier = "<state> ";
			classifier += node.getClass().getSimpleName();
		}
		else
		{
			final Leaf leaf = (Leaf) node;
			if (setOfConditions.contains(leaf.getTask()))
				classifier = "<abstract> ";
			classifier += (leaf.isInverter()) ? "NOT " : "";
			classifier += leaf.getTask().getClass().getSimpleName();
		}
		return classifier;
	}

	private int getId(String key)
	{
		int id = 0;
		if (map.containsKey(key))
			id = map.get(key);
		map.put(key, id + 1);
		return id;
	}

	// <editor-fold defaultstate="collapsed" desc="Singleton">
	public static TreePrinter getInstance()
	{
		return TreePrinterHolder.INSTANCE;
	}

	private static class TreePrinterHolder
	{

		private static final TreePrinter INSTANCE = new TreePrinter();
	}
	// </editor-fold>
}
