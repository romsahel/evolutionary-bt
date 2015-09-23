package pacman.entries.pacman.genetic.mlp;

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
	private static final int ITERATIONS = 5;
	private static final int POPULATION = 60;
	// private static final int DEATH_RATE = POPULATION / 4;
	private static final int TRIALS = 12;

	private TreeSet<NeuralNetworkPacMan> population;
	private final NeuralNetworkPacMan bestPacMan;
	private int counter;

	public MLPTrainer()
	{
		population = new TreeSet<>();

		deserializePopulation();
		System.out.println("Creating population...");
		while (population.size() < POPULATION)
			experimentAndAddPacMan(new NeuralNetworkPacMan());

		for (int i = 0; i < ITERATIONS; i++)
		{
			population = processInputs(population);

			System.out.println("It " + i + ": population size = " + population.size() + " -- best: "
			        + (population.first().getScore() / TRIALS));
		}

		serializePopulation();
		bestPacMan = population.first();
	}

	public TreeSet<NeuralNetworkPacMan> processInputs(TreeSet<NeuralNetworkPacMan> inputs)
	{

		int threads = Runtime.getRuntime().availableProcessors();
		ExecutorService service = Executors.newFixedThreadPool(threads);

		List<Future<NeuralNetworkPacMan>> futures = new ArrayList<Future<NeuralNetworkPacMan>>();
		NeuralNetworkPacMan prev = null;
		counter = 0;
		for (final NeuralNetworkPacMan input : inputs)
		{
			if (prev != null && ++counter > 0)
				futures.add(service.submit(new ExperimentRunner(input)));
			futures.add(service.submit(new ExperimentRunner(input, prev)));
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
