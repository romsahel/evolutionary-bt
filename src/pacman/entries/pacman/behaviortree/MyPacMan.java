package pacman.entries.pacman.behaviortree;

import pacman.controllers.Controller;
import pacman.entries.pacman.behaviortree.helpers.Composite;
import pacman.entries.pacman.behaviortree.helpers.Selector;
import pacman.entries.pacman.behaviortree.helpers.Sequence;
import pacman.entries.pacman.behaviortree.tasks.AnalyzeGameTask;
import pacman.entries.pacman.behaviortree.tasks.ChasePowerPillTask;
import pacman.entries.pacman.behaviortree.tasks.EatPillTask;
import pacman.entries.pacman.behaviortree.tasks.IsGhostEdibleTask;
import pacman.entries.pacman.behaviortree.tasks.IsGhostNearTask;
import pacman.entries.pacman.behaviortree.tasks.IsPathSafeTask;
import pacman.entries.pacman.behaviortree.tasks.RunAwayTask;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class MyPacMan extends Controller<MOVE>
{

    private final Composite rootNode;
    private final NearGhost nearestGhost;
    private int nearestPill, nearestPowerPill;
    public int current;
    private MOVE move;

    public MyPacMan()
    {
        nearestGhost = new NearGhost();

        rootNode = new Sequence();
        rootNode.addChildren(
                new AnalyzeGameTask(this),
                new Selector(1).addChildren(
                        
                        new Sequence(2).addChildren(
                                new IsGhostNearTask(this),
                                
                                new Selector(3).addChildren(
                                        new Sequence(4).addChildren(
                                                new IsGhostEdibleTask(this),
                                                new EatPillTask(this)),
                                        new Selector(4).addChildren(
                                                
                                                new Sequence(5).addChildren(
                                                        new IsPathSafeTask(this),
                                                        new ChasePowerPillTask(this)
                                                ),
                                                new RunAwayTask(this)
                                        )
                                )
                        ),
                        new EatPillTask(this))
        );
    }

    @Override
    public MOVE getMove(Game game, long timeDue)
    {
        rootNode.DoAction(game);
        System.out.println("===================");
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

    /**
     * @return the nearestPill
     */
    public int getNearestPill()
    {
        return nearestPill;
    }

    /**
     * @param nearestPill the nearestPill to set
     */
    public void setNearestPill(int nearestPill)
    {
        this.nearestPill = nearestPill;
    }

    /**
     * @return the nearestPowerPill
     */
    public int getNearestPowerPill()
    {
        return nearestPowerPill;
    }

    /**
     * @param game
     * @param nearestPowerPill the nearestPowerPill to set
     */
    public void setNearestPowerPill(Game game, int nearestPowerPill)
    {
        this.nearestPowerPill = nearestPowerPill;
    }

    /**
     * @return the current
     */
    public int getCurrent()
    {
        return current;
    }

    /**
     * @param current the current to set
     */
    public void setCurrent(int current)
    {
        this.current = current;
    }

}
