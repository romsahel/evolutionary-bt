package pacman.entries.pacman.behaviortree;

import pacman.entries.pacman.behaviortree.helpers.Selector;
import pacman.entries.pacman.behaviortree.helpers.Sequence;
import pacman.controllers.Controller;
import pacman.entries.pacman.behaviortree.tasks.ChaseTask;
import pacman.entries.pacman.behaviortree.tasks.EatPillTask;
import pacman.entries.pacman.behaviortree.tasks.IsGhostEdibleTask;
import pacman.entries.pacman.behaviortree.tasks.IsGhostNearTask;
import pacman.entries.pacman.behaviortree.tasks.RunAwayTask;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

/*
 * This is the class you need to modify for your entry. In particular, you need to
 * fill in the getMove() method. Any additional classes you write should either
 * be placed in this package or sub-packages (e.g., game.entries.pacman.mypackage).
 */
public class MyPacMan extends Controller<MOVE>
{

    private final Selector selector;
    private final NearGhost nearestGhost;
    private MOVE move;

    public MyPacMan()
    {
        nearestGhost = new NearGhost();

        Sequence sequence = new Sequence();
        sequence.addChildren(
                new IsGhostNearTask(this),
                new Selector().addChildren(
                        new Sequence().addChildren(
                                new IsGhostEdibleTask(this),
                                new ChaseTask(this)),
                        new RunAwayTask(this)
                )
        );
        selector = new Selector();
        selector.addChildren(sequence, new EatPillTask(this));
    }

    @Override
    public MOVE getMove(Game game, long timeDue)
    {
        selector.DoAction(game);
        return move;
    }

    /**
     * @return the nearestGhost
     */
    public NearGhost getNearestGhost()
    {
        return nearestGhost;
    }

    /**
     * @param move the move to set
     */
    public void setMove(MOVE move)
    {
        this.move = move;
    }
}
