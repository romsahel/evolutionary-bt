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
		int numIterations = 25;
		int populationSize = 5;
		int numTrials = 100;
		Trainer trainer = new Trainer(numIterations, populationSize, numTrials);
		bestIndividual = trainer.train();
		bestIndividual.print();
	}

	@Override
	public MOVE getMove(Game game, long timeDue)
	{
		return bestIndividual.getMove(game, timeDue);
//		return move;
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
