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
	private static final Random rnd = new Random(0);
	private final int numIterations;
	private final int populationSize;
	private final int numTrials;
	// private static final int DEATH_RATE = POPULATION / 4;

	private TreeSet<EvolvingBTPacMan> population;
	private EvolvingBTPacMan bestPacMan;
	private int counter;

	public Trainer(int numIterations, int populationSize, int numTrials)
	{
		population = new TreeSet<>();
		this.numIterations = numIterations;
		this.populationSize = populationSize;
		this.numTrials = numTrials;
	}

	public EvolvingBTPacMan train()
	{
		System.out.println("Training of " + numIterations + " iterations with " + numTrials + " trials.");
		System.out.println("Creating population of " + populationSize + " individuals...");
		deserializePopulation();
		while (population.size() < populationSize)
			experimentAndAddPacMan(new EvolvingBTPacMan());
		System.out.println("Population created.");

		for (int i = 1; i < numIterations + 1; i++)
		{
//			population.first().print();
			population = processPopulation(population);
			System.out.println("It " + i + ": population size = " + population.size() + " -- best: " + (population.first().getScore() / numTrials));
//			System.out.println(Double.toString((population.first().getScore() / numTrials)).replace(".", ","));
		}

		serializePopulation();
		bestPacMan = population.first();

		population.clear();
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
	public TreeSet<EvolvingBTPacMan> threadedProcessPopulation(TreeSet<EvolvingBTPacMan> inputs)
	{
		int threads = Runtime.getRuntime().availableProcessors();
		ExecutorService service = Executors.newFixedThreadPool(threads);

		List<Future<EvolvingBTPacMan[]>> futures = new ArrayList<>();
		EvolvingBTPacMan prev = null;
		counter = 0;

		for (final EvolvingBTPacMan input : inputs)
		{
			// We combine 2-by-2 the best individuals of the population to
			// create offspring
			if (prev != null)
				futures.add(service.submit(new ExperimentRunner(input, prev)));
			// We keep and re-experiment on the best individuals of the
			// population
			futures.add(service.submit(new ExperimentRunner(input)));
			counter++;

			prev = input;
			if (counter >= populationSize / 2)
				break;
		}
		service.shutdown();

		TreeSet<EvolvingBTPacMan> outputs = new TreeSet<>();
		for (Future<EvolvingBTPacMan[]> future : futures)
			try
			{
				final EvolvingBTPacMan[] get = future.get();
				for (EvolvingBTPacMan result : get)
					if (result != null)
						outputs.add(result);
			} catch (InterruptedException | ExecutionException e)
			{
				e.printStackTrace();
			}
		return outputs;
	}

	public TreeSet<EvolvingBTPacMan> processPopulation(TreeSet<EvolvingBTPacMan> inputs)
	{
		EvolvingBTPacMan prev = null;
		counter = 0;

		TreeSet<EvolvingBTPacMan> outputs = new TreeSet<>();
		for (final EvolvingBTPacMan input : inputs)
		{
			if (prev != null)
			{
				final EvolvingBTPacMan[] combine = EvolvingBTPacMan.combine(prev, input);
				if (combine != null)
					for (EvolvingBTPacMan bt : combine)
					{
						runExperiment(bt, numTrials);
						outputs.add(bt);
					}
			}
			runExperiment(input, numTrials);
			outputs.add(input);
			counter++;

			prev = input;
			if (counter >= populationSize / 2)
				break;
		}
		return outputs;
	}

	/**
	 *
	 */
	private void experimentAndAddPacMan(EvolvingBTPacMan newPacMan)
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
	public void runExperiment(EvolvingBTPacMan pacman, int trials)
	{
		int avgScore = 0;
		for (int i = 0; i < trials; i++)
		{
			Game game = new Game(rnd.nextLong());

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
			for (EvolvingBTPacMan pacman : population)
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
		System.out.print("Load population from file");
		population = new TreeSet<>();
		FileInputStream fis;
		try
		{
			fis = new FileInputStream("population.ser");
			try (ObjectInputStream iis = new ObjectInputStream(fis))
			{
				int n = iis.readInt();
				System.out.print(" -- size: " + n + "...");
				for (int i = 0; i < n; i++)
				{
					final Composite composite = (Composite) iis.readObject();
					final EvolvingBTPacMan pacman = new EvolvingBTPacMan(composite);
					experimentAndAddPacMan(pacman);
					population.add(pacman);
				}
			}
			fis.close();
		} catch (IOException | ClassNotFoundException e)
		{
			System.err.println("Can't deserialize population: " + e.getClass());
		}
		System.out.println();
		System.out.println("Population: " + population.size() + "/" + populationSize);
	}

	public EvolvingBTPacMan getBestPacMan()
	{
		return bestPacMan;

	}

	private class ExperimentRunner implements Callable<EvolvingBTPacMan[]>
	{

		private final EvolvingBTPacMan prev;
		private final EvolvingBTPacMan current;

		public ExperimentRunner(EvolvingBTPacMan current)
		{
			this.current = current;
			this.prev = null;
		}

		public ExperimentRunner(EvolvingBTPacMan current, EvolvingBTPacMan prev)
		{
			this.current = current;
			this.prev = prev;
		}

		@Override
		public EvolvingBTPacMan[] call() throws Exception
		{
			EvolvingBTPacMan[] results;
			if (prev != null)
				results = EvolvingBTPacMan.combine(prev, current);
			else
				results = new EvolvingBTPacMan[]
				{
					current
				};

			for (EvolvingBTPacMan pacman : results)
				runExperiment(pacman, numTrials);
			return results;
		}
	}
}
