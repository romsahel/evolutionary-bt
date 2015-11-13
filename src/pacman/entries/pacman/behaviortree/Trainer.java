	/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pacman.entries.pacman.behaviortree;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import pacman.controllers.examples.StarterGhosts;
import pacman.entries.pacman.behaviortree.helpers.Composite;
import pacman.game.Constants;
import pacman.game.Game;

/**
 *
 * @author Romsahel
 */
public final class Trainer
{

	private final int numIterations;
	private final int populationSize;
	private final int numTrials;
	// private static final int DEATH_RATE = POPULATION / 4;

	private TreeSet<BTPacMan> population;
	private BTPacMan bestPacMan;
	private int counter;

	public Trainer(int numIterations, int populationSize, int numTrials)
	{
		population = new TreeSet<>();
		this.numIterations = numIterations;
		this.populationSize = populationSize;
		this.numTrials = numTrials;
	}

	public BTPacMan train()
	{
		System.out.println("Load population from file...");
		deserializePopulation();
		System.out.println("Creating population...");
		while (population.size() < populationSize)
			experimentAndAddPacMan(new BTPacMan());

		for (int i = 1; i < numIterations + 1; i++)
		{
			population = processPopulation(population);

			System.out.println("It " + i + ": population size = " + population.size() + " -- best: " + (population.first().getScore() / numTrials));
//			System.out.println(Double.toString((population.first().getScore() / numTrials)).replace(".", ","));
		}
		serializePopulation();

		bestPacMan = population.first();
		return bestPacMan;
	}

	/**
	 * This method runs the experiments on all the individual to make the new
	 * population. To improve performance, it uses ExecutorService and submits
	 * parallely all the individuals to testing. It then waits for all the
	 * results before returning.
	 *
	 * @param inputs
	 *               <p>
	 * @return
	 */
	public TreeSet<BTPacMan> processPopulation(TreeSet<BTPacMan> inputs)
	{
		int threads = Runtime.getRuntime().availableProcessors();
		ExecutorService service = Executors.newFixedThreadPool(threads);

		List<Future<BTPacMan>> futures = new ArrayList<>();
		BTPacMan prev = null;
		counter = 0;

		for (final BTPacMan input : inputs)
		{
			// We combine 2-by-2 the best individuals of the population to
			// create offspring
			if (prev != null)
			{
				counter++;
				futures.add(service.submit(new ExperimentRunner(input, prev)));
			}
			// We keep and re-experiment on the best individuals of the
			// population
			futures.add(service.submit(new ExperimentRunner(input)));
			counter++;

			prev = input;
			if (counter >= populationSize)
				break;
		}

		service.shutdown();

		TreeSet<BTPacMan> outputs = new TreeSet<>();
		for (Future<BTPacMan> future : futures)
			try
			{
				outputs.add(future.get());
			} catch (InterruptedException | ExecutionException e)
			{
				e.printStackTrace();
			}
		return outputs;
	}

	/**
	 *
	 */
	private void experimentAndAddPacMan(BTPacMan newPacMan)
	{
		runExperiment(newPacMan, numTrials);
		population.add(newPacMan);
	}

	/**
	 * For running multiple games without visuals.
	 * <p>
	 * @param pacman
	 * @param trials
	 */
	public void runExperiment(BTPacMan pacman, int trials)
	{
		double avgScore = 0;

		Random rnd = new Random(0);
		Game game;

		for (int i = 0; i < trials; i++)
		{
			game = new Game(rnd.nextLong());

			while (!game.gameOver())
				game.advanceGame(pacman.getMove(game.copy(), System.currentTimeMillis() + Constants.DELAY),
								 new StarterGhosts().getMove(game.copy(), System.currentTimeMillis() + Constants.DELAY));

			avgScore += game.getScore();
		}
		pacman.setScore(avgScore);
	}

	/**
	 * Serialize the population into a file to 'save' the current training
	 * session. Use the deserializePopulation to then resume it.
	 */
	private void serializePopulation()
	{
		FileOutputStream fos;
		try
		{
			fos = new FileOutputStream("population.ser");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeInt(population.size());
			for (BTPacMan pacman : population)
				oos.writeObject(pacman.getRootNode());
			oos.close();
			fos.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Deserialize the population from file to 'resume' a past training session,
	 * saved with serializePopulation
	 */
	private void deserializePopulation()
	{
		population = new TreeSet<>();
		FileInputStream fis;
		try
		{
			fis = new FileInputStream("population.ser");
			try (ObjectInputStream iis = new ObjectInputStream(fis))
			{
				int n = iis.readInt();
				for (int i = 0; i < n; i++)
					experimentAndAddPacMan(new BTPacMan((Composite) iis.readObject()));
			}
			fis.close();
		} catch (IOException | ClassNotFoundException e)
		{
			System.err.println("Can't deserialize population: " + e.getClass());
		}
		System.out.println("Population: " + population.size() + "/" + populationSize);
	}

	public BTPacMan getBestPacMan()
	{
		return bestPacMan;

	}

	private class ExperimentRunner implements Callable<BTPacMan>
	{

		private final BTPacMan prev;
		private final BTPacMan current;

		public ExperimentRunner(BTPacMan current)
		{
			this.current = current;
			this.prev = null;
		}

		public ExperimentRunner(BTPacMan current, BTPacMan prev)
		{
			this.current = current;
			this.prev = prev;
		}

		@Override
		public BTPacMan call() throws Exception
		{
			BTPacMan newPacMan = current;
			if (prev != null)
				newPacMan = new BTPacMan(prev, current);

			runExperiment(newPacMan, numTrials);
			return newPacMan;
		}
	}
}
