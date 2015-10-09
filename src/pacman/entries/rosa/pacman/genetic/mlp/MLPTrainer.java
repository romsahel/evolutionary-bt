package pacman.entries.rosa.pacman.genetic.mlp;

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
import pacman.game.Constants;
import pacman.game.Game;

public class MLPTrainer
{
	private static final int ITERATIONS = 1;
	private static final int POPULATION = 100;
	// private static final int DEATH_RATE = POPULATION / 4;
	private static final int TRIALS = 50;

	private TreeSet<NeuralNetworkPacMan> population;
	private final NeuralNetworkPacMan bestPacMan;
	private int counter;

	public MLPTrainer(boolean train)
	{
		population = new TreeSet<>();

		deserializePopulation();
		System.out.println("Creating population...");
		if (train)
		{

			while (population.size() < POPULATION)
				experimentAndAddPacMan(new NeuralNetworkPacMan());

			for (int i = 1; i < ITERATIONS + 1; i++)
			{
				population = processPopulation(population);

				// System.out.println("It " + i + ": population size = " +
				// population.size() + " -- best: "
				// + (population.first().getScore() / TRIALS));
				System.out.println(Double.toString((population.first().getScore() / TRIALS)).replace(".", ","));
			}
			serializePopulation();
		}

		bestPacMan = population.first();
	}

	/**
	 * This method runs the experiments on all the individual to make the new
	 * population. To improve performance, it uses ExecutorService and submits
	 * parallely all the individuals to testing. It then waits for all the
	 * results before returning.
	 * 
	 * @param inputs
	 * @return
	 */
	public TreeSet<NeuralNetworkPacMan> processPopulation(TreeSet<NeuralNetworkPacMan> inputs)
	{
		int threads = Runtime.getRuntime().availableProcessors();
		ExecutorService service = Executors.newFixedThreadPool(threads);

		List<Future<NeuralNetworkPacMan>> futures = new ArrayList<Future<NeuralNetworkPacMan>>();
		NeuralNetworkPacMan prev = null;
		counter = 0;

		for (final NeuralNetworkPacMan input : inputs)
		{
			// We combine 2-by-2 the best individuals of the population to
			// create offspring
			if (prev != null && ++counter > 0)
				futures.add(service.submit(new ExperimentRunner(input, prev)));
			// We keep and re-experiment on the best individuals of the
			// population
			futures.add(service.submit(new ExperimentRunner(input)));
			counter++;

			prev = input;
			if (counter >= POPULATION)
				break;
		}

		service.shutdown();

		TreeSet<NeuralNetworkPacMan> outputs = new TreeSet<>();
		for (Future<NeuralNetworkPacMan> future : futures)
			try
			{
				outputs.add(future.get());
			}
			catch (InterruptedException | ExecutionException e)
			{
				e.printStackTrace();
			}
		return outputs;
	}

	/**
	 * 
	 */
	private void experimentAndAddPacMan(NeuralNetworkPacMan newPacMan)
	{
		runExperiment(newPacMan, TRIALS);
		population.add(newPacMan);
	}

	/**
	 * For running multiple games without visuals.
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

	/**
	 * Serialize the population into a file to 'save' the current training
	 * session. Use the deserializePopulation to then resume it.
	 */
	private void serializePopulation()
	{
		FileOutputStream fos;
		try
		{
			fos = new FileOutputStream("mlp");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeInt(population.size());
			for (NeuralNetworkPacMan pacman : population)
				oos.writeObject(pacman.getPerceptron().getWeights());
			oos.close();
			fos.close();
		}
		catch (IOException e)
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
			fis = new FileInputStream("mlp");
			ObjectInputStream iis = new ObjectInputStream(fis);
			float[] array = null;
			int n = iis.readInt();
			for (int i = 0; i < n; i++)
			{
				array = (float[]) iis.readObject();
				experimentAndAddPacMan(new NeuralNetworkPacMan(new Perceptron(array)));
			}
			iis.close();
			fis.close();
		}
		catch (IOException | ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		System.out.println("Population: " + population.size() + "/" + POPULATION);
	}

	public NeuralNetworkPacMan getBestPacMan()
	{
		return bestPacMan;
	}

	private class ExperimentRunner implements Callable<NeuralNetworkPacMan>
	{
		private NeuralNetworkPacMan prev;
		private NeuralNetworkPacMan current;

		public ExperimentRunner(NeuralNetworkPacMan current)
		{
			this.current = current;
		}

		public ExperimentRunner(NeuralNetworkPacMan current, NeuralNetworkPacMan prev)
		{
			this.current = current;
			this.prev = prev;
		}

		@Override
		public NeuralNetworkPacMan call() throws Exception
		{
			NeuralNetworkPacMan newPacMan = null;
			if (prev != null)
				newPacMan = new NeuralNetworkPacMan(prev, current);
			else
				newPacMan = new NeuralNetworkPacMan(current);

			runExperiment(newPacMan, TRIALS);
			return newPacMan;
		}
	}
}
