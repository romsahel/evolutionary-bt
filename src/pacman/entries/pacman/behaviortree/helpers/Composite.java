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

    public Composite addChildren(Task... tasks)
    {
        children.addAll(Arrays.asList(tasks));
        return this;
    }
}
