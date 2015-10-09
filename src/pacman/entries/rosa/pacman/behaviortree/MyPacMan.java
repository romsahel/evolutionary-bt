package pacman.entries.rosa.pacman.behaviortree;

import pacman.controllers.Controller;
import pacman.entries.rosa.pacman.behaviortree.helpers.Composite;
import pacman.entries.rosa.pacman.behaviortree.helpers.Selector;
import pacman.entries.rosa.pacman.behaviortree.helpers.Sequence;
import pacman.entries.rosa.pacman.behaviortree.tasks.ChasePowerPillTask;
import pacman.entries.rosa.pacman.behaviortree.tasks.ChaseTask;
import pacman.entries.rosa.pacman.behaviortree.tasks.EatPillTask;
import pacman.entries.rosa.pacman.behaviortree.tasks.IsGhostEdibleTask;
import pacman.entries.rosa.pacman.behaviortree.tasks.IsGhostNearTask;
import pacman.entries.rosa.pacman.behaviortree.tasks.IsPathSafeTask;
import pacman.entries.rosa.pacman.behaviortree.tasks.RunAwayTask;
import pacman.entries.rosa.pacman.behaviortree.tasks.isGhostCloserTask;
import pacman.entries.rosa.pacman.GameState;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class MyPacMan extends Controller<MOVE>
{
	/*
	 * Root of the behavior tree
	 */
	private final Composite rootNode;

	private final GameState state;

	/*
	 * Type of MOVE that will be returned at each game step
	 */
	private MOVE move;

	// instead of running away from the nearest, look at all the nearest
	// and find an exit that would work for all of them
	public MyPacMan()
	{
		state = new GameState();
		rootNode = new Selector();
		rootNode.addChildren(
		        new Sequence(1).addChildren(
		                new IsGhostNearTask(this),
		                new Selector(2).addChildren(
		                        new Sequence(3).addChildren(
		                                new IsGhostEdibleTask(this),
		                                new Selector(4).addChildren(
		                                        new Sequence(5).addChildren(
		                                                new isGhostCloserTask(this),
		                                                new ChaseTask(this)),
		                                        new EatPillTask(this))),
		                        new Selector(3).addChildren(
		                                new Sequence(4).addChildren(
		                                        new IsPathSafeTask(this),
		                                        new ChasePowerPillTask(this)),
		                                new RunAwayTask(this)))),
		        new EatPillTask(this));
	}

	@Override
	public MOVE getMove(Game game, long timeDue)
	{
		state.update(game);
		rootNode.DoAction(game);
		if (Composite.DEBUG)
			System.out.println("===================");
		return move;
	}

	/**
	 * @param move
	 *            the move to set
	 */
	public void setMove(MOVE move)
	{
		this.move = move;
	}

	public GameState getState()
	{
		return state;
	}

}
