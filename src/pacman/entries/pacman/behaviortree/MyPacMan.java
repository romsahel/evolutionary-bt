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
	public MyPacMan()
	{
		int numIterations = 15;
		int populationSize = 35;
		int numTrials = 10;
		Trainer trainer = new Trainer(numIterations, populationSize, numTrials);
		bestIndividual = trainer.train();
		bestIndividual.print();
	}

	@Override
	public MOVE getMove(Game game, long timeDue)
	{
		return bestIndividual.getMove(game, timeDue);
	}

	public GameState getState()
	{
		return state;
	}

}
