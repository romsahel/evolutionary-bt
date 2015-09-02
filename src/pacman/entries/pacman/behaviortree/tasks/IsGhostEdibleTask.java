/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pacman.entries.pacman.behaviortree.tasks;

import pacman.entries.pacman.behaviortree.MyPacMan;
import pacman.entries.pacman.behaviortree.helpers.Leaf;
import pacman.game.Constants;
import pacman.game.Game;

/**
 *
 * @author romsahel
 */
public class IsGhostEdibleTask extends Leaf
{

    public IsGhostEdibleTask(MyPacMan parent)
    {
        super(parent);
    }

    @Override
    public boolean DoAction(Game game)
    {
        System.out.println("Is ghost edible?");
        final int edibleTime = game.getGhostEdibleTime(parent.getNearestGhost().getType());
        return (edibleTime > 1);
    }

}
