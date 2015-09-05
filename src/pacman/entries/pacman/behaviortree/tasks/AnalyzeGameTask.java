package pacman.entries.pacman.behaviortree.tasks;

import pacman.entries.pacman.behaviortree.MyPacMan;
import pacman.entries.pacman.behaviortree.NearGhost;
import pacman.entries.pacman.behaviortree.helpers.Leaf;
import pacman.game.Constants;
import pacman.game.Game;

/**
 *
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
        getNearestGhost(game);
        int current = game.getPacmanCurrentNodeIndex();
        int[] pills = game.getActivePillsIndices();
        int[] powerPills = game.getActivePowerPillsIndices();

        int[] availablePills = new int[pills.length + powerPills.length];
        System.arraycopy(pills, 0, availablePills, 0, pills.length);
        System.arraycopy(powerPills, 0, availablePills, pills.length, powerPills.length);

        parent.setCurrent(current);
        
        parent.setNearestPill(game.getClosestNodeIndexFromNodeIndex(current,
                availablePills,
                Constants.DM.PATH));

        parent.setNearestPowerPill(game, game.getClosestNodeIndexFromNodeIndex(current,
                powerPills,
                Constants.DM.PATH));
        return true;
    }

    private void getNearestGhost(Game game)
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
