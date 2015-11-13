package pacman.entries.pacman.behaviortree;

import pacman.controllers.Controller;
import pacman.entries.pacman.GameState;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class MyPacMan extends Controller<MOVE>
{


	/*
	 * Root of the behavior tree
	 */
	private final BTPacMan bestIndividual;

	private final GameState state = new GameState();

	/*
	 * Type of MOVE that will be returned at each game step
	 */
	private MOVE move;

	public MyPacMan()
	{
		Trainer trainer = new Trainer(100, 100, 10);
		bestIndividual = trainer.train();
		bestIndividual.print();
	}

	@Override
	public MOVE getMove(Game game, long timeDue)
	{
		bestIndividual.getMove(game, timeDue);
		return move;
	}

	/**
	 * @param move
	 *             the move to set
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
