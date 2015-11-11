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
import pacman.entries.pacman.behaviortree.helpers.Inverter;
import pacman.entries.pacman.behaviortree.helpers.Leaf;
import pacman.entries.pacman.behaviortree.helpers.Node;
import pacman.entries.pacman.behaviortree.helpers.Selector;

/**
 *
 * @author Romsahel
 */
public class TreePrinter
{

	private static List<Leaf> setOfConditions;

	private TreePrinter()
	{
	}

	public void print(Composite root, Leaf[] setOfConditions)
	{
		TreePrinter.setOfConditions = Arrays.asList(setOfConditions);
		System.out.println("#lineWidth: 2\n" + "#padding: 12\n" + "#arrowSize: 0.7\n");
		HashMap<String, Integer> map = new HashMap<>();
		final int nodeId = getId(map, root.getClass().getSimpleName());
		System.out.println("[" + getClassifier(root) + root.getClass().getSimpleName() + " " + nodeId + "]");
		printTree(map, root, nodeId);
	}

	private void printTree(HashMap<String, Integer> map, Composite root, int id)
	{
		for (Node node : root.getChildren())
		{
			int nodeId = getId(map, node.getClass().getSimpleName());
			if (node.getClass() == Inverter.class)
				System.out.println("[" + root.getClass().getSimpleName() + " " + id + "] -> [NOT "
								   + ((Inverter) node).getInvertedNode().getClass().getSimpleName() + " " + nodeId + "]");
			else
				System.out.println("[" + root.getClass().getSimpleName() + " " + id + "] -> ["
								   + getClassifier(node) + node.getClass().getSimpleName() + " " + nodeId + "]");

			if (node instanceof Composite)
			{
				Composite composite = (Composite) node;
				printTree(map, composite, nodeId);
			}
		}
	}

	private String getClassifier(Node node)
	{
		String classifier = "";
		if (node instanceof Composite)
			if (node.getClass() == Selector.class)
				classifier = "<choice> ";
			else
				classifier = "<state> ";
		else if (setOfConditions.contains((Leaf) node))
			classifier = "<abstract> ";

		return classifier;
	}

	private int getId(HashMap<String, Integer> map, String key)
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
