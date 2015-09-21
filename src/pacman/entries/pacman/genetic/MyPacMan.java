package pacman.entries.pacman.genetic;

import java.util.ArrayList;
import java.util.Random;

import pacman.controllers.Controller;
import pacman.controllers.examples.StarterGhosts;
import pacman.entries.pacman.genetic.mlp.NeuralNetworkPacMan;
import pacman.game.Constants;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class MyPacMan extends Controller<MOVE>
{
	private final ArrayList<NeuralNetworkPacMan> population;

	public MyPacMan()
	{
		population = new ArrayList<>();
		for (int i = 0; i < 100; i++)
		{
			NeuralNetworkPacMan pacman = new NeuralNetworkPacMan();
			population.add(pacman);
			runExperiment(pacman, 10);
		}
	}

	/**
	 * For running multiple games without visuals.
	 *
	 * @param pacman
	 *            The Pac-Man controller
	 * @param ghostController
	 *            The Ghosts controller
	 * @param trials
	 *            The number of trials to be executed
	 */
	public void runExperiment(NeuralNetworkPacMan pacman, int trials)
	{
		double avgScore = 0;

		Random rnd = new Random(0);
		Game game;

		for (int i = 0; i < trials; i++)
		{
			game = new Game(rnd.nextLong());

			while (!game.gameOver())
			{
				game.advanceGame(pacman.getMove(game.copy(), System.currentTimeMillis() + Constants.DELAY),
				        new StarterGhosts().getMove(game.copy(), System.currentTimeMillis() + Constants.DELAY));
			}

			avgScore += game.getScore();
		}
		pacman.setScore(avgScore / trials);
	}

	@Override
	public MOVE getMove(Game game, long timeDue)
	{
		return MOVE.DOWN;
	}
}
