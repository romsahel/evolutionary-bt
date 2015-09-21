package pacman.entries.pacman.behaviortree.helpers;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Represents a composite node, ie a node with multiple children 
 * such as Selectors and Sequences.
 * 
 * @author romsahel
 */
public abstract class Composite extends Node
{
	/**
	 * DEBUG constant can be used to output in the console 
	 * the path taken within the tree.
	 */
	public static final boolean DEBUG = false;
	/**
	 * The list of children nodes (subnodes) 
	 */
	ArrayList<Node> children = new ArrayList<>();
	/**
	 * Used to indent the debug output
	 */
    String prefix;

    public Composite()
    {
        this.prefix = "";
    }

    /**
     * This constructor allows to give the depth of the node in the tree,
     * which allows a cleaner and indented debug output
     * @param depth the depth of the node in the tree.
     */
    public Composite(int depth)
    {
    	if (!DEBUG)
    		return;
        StringBuilder builder = new StringBuilder(depth);
        for (int i = 0; i < depth; i++)
            builder.append('\t');
        this.prefix = builder.toString();
    }

    /**
     * Add children to the list
     * @param nodes children of the current node
     * @return this object, allows for method chaining
     */
    public Composite addChildren(Node... nodes)
    {
        children.addAll(Arrays.asList(nodes));
        return this;
    }
}
