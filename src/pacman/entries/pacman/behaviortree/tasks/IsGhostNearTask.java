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
public class IsGhostNearTask extends Leaf
{

    public IsGhostNearTask(MyPacMan parent)
    {
        super(parent);
    }

    @Override
    public boolean DoAction(Game game)
    {
        System.out.println("Is ghost near?");
        getNearestGhost(game);
        return parent.getNearestGhost().getType() != null
                && parent.getNearestGhost().getDistance() < 20;
    }

    private void getNearestGhost(Game game)
    {
        double minimum = Integer.MAX_VALUE;
        Constants.GHOST nearestGhostType = null;
        for (Constants.GHOST ghost : Constants.GHOST.values())
            if (game.getGhostLairTime(ghost) < 2)
            {
                double shortest = game.getEuclideanDistance(game.getPacmanCurrentNodeIndex(),
                        game.getGhostCurrentNodeIndex(ghost));
                if (shortest < minimum)
                {
                    nearestGhostType = ghost;
                    minimum = shortest;
                }
            }
        if (nearestGhostType != null)
            parent.getNearestGhost().update(game, nearestGhostType, minimum);
    }
}
