package pacman.entries.rosa.pacman.genetic.mlp;

public class EvolvingPerceptron extends Perceptron
{
	public EvolvingPerceptron(Perceptron parent1, Perceptron parent2)
	{
		super(null);

		for (int i = 0; i < Perceptron.NB_TOTAL_WEIGHTS; i++)
		{
			if (rand.nextBoolean())
				weights[i] = parent1.weights[i];
			else
				weights[i] = parent2.weights[i];

			// mutation
			if (rand.nextFloat() * 100 < MUTATION_CHANCES)
				weights[i] = (rand.nextFloat() / 2) - (0.5f / 2);
		}
	}

}
