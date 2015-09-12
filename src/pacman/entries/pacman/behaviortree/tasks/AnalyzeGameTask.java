package pacman.entries.pacman.behaviortree.tasks;

import pacman.entries.pacman.behaviortree.MyPacMan;
import pacman.entries.pacman.behaviortree.NearGhost;
import pacman.entries.pacman.behaviortree.helpers.Leaf;
import pacman.game.Constants;
import pacman.game.Game;

/**
 * Analyze the game state to update necessary informations such as 
 * nearest pills positions and ghosts.
 * @author romsahel
 */
public class AnalyzeGameTask extends Leaf
{

    public AnalyzeGameTask(MyPacMan parent)
    {
        super(parent);
    }

    @Override
    public boolean DoAction(Game game)
    {
        updateNearestGhosts(game);
        int current = game.getPacmanCurrentNodeIndex();
        int[] pills = game.getActivePillsIndices();
        int[] powerPills = game.getActivePowerPillsIndices();

        // merge the pills and powerpills indexes into one array to find 
        // the nearest one
        int[] availablePills = new int[pills.length + powerPills.length];
        System.arraycopy(pills, 0, availablePills, 0, pills.length);
        System.arraycopy(powerPills, 0, availablePills, pills.length, powerPills.length);

        parent.setNearestPill(game.getClosestNodeIndexFromNodeIndex(current,
                availablePills,
                Constants.DM.PATH));

        parent.setNearestPowerPill(game, game.getClosestNodeIndexFromNodeIndex(current,
                powerPills,
                Constants.DM.PATH));

        parent.setCurrent(current);
        
        return true;
    }

    /**
     * Update the sorted list of enabled ghosts (excludes ghosts in the lair)
     * @param game
     */
    private void updateNearestGhosts(Game game)
    {
        parent.getNearestGhosts().clear();
        for (Constants.GHOST ghost : Constants.GHOST.values())
            if (game.getGhostLairTime(ghost) < 1)
            {
                double shortest = game.getDistance(
                        game.getPacmanCurrentNodeIndex(),
                        game.getGhostCurrentNodeIndex(ghost),
                        Constants.DM.PATH
                );
                parent.getNearestGhosts().add(new NearGhost(game, ghost, shortest));
            }
    }
}
