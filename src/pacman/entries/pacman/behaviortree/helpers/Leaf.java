/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pacman.entries.pacman.behaviortree.helpers;

import pacman.entries.pacman.behaviortree.MyPacMan;

/**
 *
 * @author romsahel
 */
public abstract class Leaf extends Task
{
    protected final MyPacMan parent;

    public Leaf(MyPacMan parent)
    {
        this.parent = parent;
    }

}
