/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pacman.entries.pacman.behaviortree.helpers;

import java.util.ArrayList;
import java.util.Arrays;
import pacman.entries.pacman.behaviortree.MyPacMan;

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
