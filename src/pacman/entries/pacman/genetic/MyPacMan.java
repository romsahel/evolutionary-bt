package pacman.entries.pacman.genetic;

import java.util.Random;
import java.util.TreeSet;

import pacman.controllers.Controller;
import pacman.controllers.examples.StarterGhosts;
import pacman.entries.pacman.genetic.mlp.NeuralNetworkPacMan;
import pacman.game.Constants;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class MyPacMan extends Controller<MOVE>
{
	private static final int ITERATIONS = 27;
	private static final int POPULATION = 50;
	private static final int DEATH_RATE = POPULATION / 2;	
	private static final int TRIALS = 10;
	
	private TreeSet<NeuralNetworkPacMan> population;
	private final NeuralNetworkPacMan bestPacMan;

	public MyPacMan()
	{
		population = new TreeSet<>();

		while (population.size() < POPULATION)
			experimentAndAddPacMan(new NeuralNetworkPacMan());
		
		for (int i = 0; i < ITERATIONS; i++)
		{	
			int j = 0;
			NeuralNetworkPacMan prev = null;
			final TreeSet<NeuralNetworkPacMan> oldPopulation = population;
			population = new TreeSet<>();
			for (NeuralNetworkPacMan pacman : oldPopulation)
			{
				experimentAndAddPacMan(new NeuralNetworkPacMan(pacman));
				if (prev != null)
					experimentAndAddPacMan(new NeuralNetworkPacMan(prev, pacman));
				
				prev = pacman;
				if (j++ > DEATH_RATE)
					break;
			}
			System.out.println("It " + i + ": population size = " + population.size() + " -- best: " + (population.first().getScore() / TRIALS));
		}

		bestPacMan = population.first();

		System.out.println((population.first().getScore() / TRIALS));

	}

	private void experimentAndAddPacMan(NeuralNetworkPacMan newPacMan)
	{
		runExperiment(newPacMan, TRIALS);
		population.add(newPacMan);
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
		pacman.setScore(avgScore);
	}

	@Override
	public MOVE getMove(Game game, long timeDue)
	{
		return this.bestPacMan.getMove(game, timeDue);
	}
}
