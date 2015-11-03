package pacman.entries.pacman.behaviortree.tasks.actions;

import pacman.entries.pacman.behaviortree.MyPacMan;
import pacman.entries.pacman.behaviortree.helpers.Leaf;
import pacman.game.Constants;
import pacman.game.Game;

public class GoToJunctionTask extends Leaf {
    public GoToJunctionTask(MyPacMan parent)
    {
        super(parent);
    }

    @Override
    public boolean DoAction(Game game)
    {
        parent.setMove(game.getNextMoveTowardsTarget(
                        state.getCurrent(),
                        state.getNearestSafeJunction().getIndex(),
                        Constants.DM.PATH)
        );
        return true;
    }
}
