package pacman.entries.pacman.behaviortree.tasks;

import pacman.entries.pacman.behaviortree.MyPacMan;
import pacman.entries.pacman.behaviortree.helpers.Leaf;
import pacman.game.Constants;
import pacman.game.Game;

/**
 *
 * @author romsahel
 */
public class EatPillTask extends Leaf
{

    public EatPillTask(MyPacMan parent)
    {
        super(parent);
    }

    @Override
    public boolean DoAction(Game game)
    {
        int current = game.getPacmanCurrentNodeIndex();
        int[] pills = game.getActivePillsIndices();
        int[] powerPills = game.getActivePowerPillsIndices();

        int[] availablePills = new int[pills.length + powerPills.length];
        System.arraycopy(pills, 0, availablePills, 0, pills.length);
        System.arraycopy(powerPills, 0, availablePills, pills.length, powerPills.length);

        final int closestNode = game.getClosestNodeIndexFromNodeIndex(current,
                availablePills,
                Constants.DM.MANHATTAN);
        parent.setMove(game.getNextMoveTowardsTarget(current, closestNode, Constants.DM.PATH));
        return true;
    }
}
