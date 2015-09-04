package pacman.entries.pacman.behaviortree.helpers;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author romsahel
 */
public abstract class Composite extends Task
{

    ArrayList<Task> children = new ArrayList<>();
    String prefix;

    public Composite()
    {
        this.prefix = "";
    }

    public Composite(int depth)
    {
        StringBuilder builder = new StringBuilder(depth);
        for (int i = 0; i < depth; i++)
            builder.append('\t');
        this.prefix = builder.toString();
    }

    public Composite addChildren(Task... tasks)
    {
        children.addAll(Arrays.asList(tasks));
        return this;
    }
}
