package pacman.entries.pacman.behaviortree.tasks;

import pacman.entries.pacman.behaviortree.MyPacMan;
import pacman.entries.pacman.behaviortree.helpers.Leaf;
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
        return true;
    }
}
