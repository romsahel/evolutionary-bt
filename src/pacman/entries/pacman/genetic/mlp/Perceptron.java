package pacman.entries.pacman.genetic.mlp;

import java.util.ArrayList;
import java.util.Random;

public class Perceptron
{
	public static final int NB_NEARGHOST_INPUT = 2;
	private static final int NB_INPUT = NB_NEARGHOST_INPUT * 2 + 2;
	private static final int NB_LAYERS = 2;
	private static final int NB_HIDDEN = NB_INPUT * 4;
	private static final int NB_OUTPUT = 1;
	private static final int NB_INPUT_WEIGHTS = NB_INPUT * NB_HIDDEN;
	private static final int NB_HIDDEN_WEIGHTS = NB_HIDDEN * NB_HIDDEN;
	private static final int NB_TOTAL_HIDDEN_WEIGHTS = NB_HIDDEN * NB_HIDDEN * (NB_LAYERS - 1);
	private static final int NB_OUTPUT_WEIGHTS = NB_HIDDEN * NB_OUTPUT;
	protected static final int NB_TOTAL_WEIGHTS = NB_INPUT_WEIGHTS + NB_TOTAL_HIDDEN_WEIGHTS + NB_OUTPUT_WEIGHTS;

	private static final Random rand = new Random();

	protected ArrayList<Integer> inputNeurons;
	protected final float[] hiddenNeurons;
	protected final float[] outputNeurons;
	private final float[] weights;

	public Perceptron(float[] weights)
	{
		inputNeurons = new ArrayList<>(NB_INPUT);
		hiddenNeurons = new float[NB_LAYERS * NB_HIDDEN];
		outputNeurons = new float[NB_OUTPUT];
		if (weights == null)
			this.weights = new float[Perceptron.NB_TOTAL_WEIGHTS];
		else
			this.weights = weights;
	}

	public Perceptron(Perceptron parent1, Perceptron parent2)
	{
		this((float[]) null);

		for (int i = 0; i < Perceptron.NB_TOTAL_WEIGHTS; i++)
		{
			if (rand.nextBoolean())
				weights[i] = parent1.weights[i];
			else
				weights[i] = parent2.weights[i];

			// 10% chances of mutation
			if (rand.nextFloat() * 100 < 10)
				weights[i] = (rand.nextFloat() / 2) - (0.5f / 2);
		}
	}

	public Perceptron(Perceptron perceptron)
	{
		this(perceptron.weights);
	}

	public Perceptron()
	{
		this((float[]) null);

		int i = 0;
		// randomize weights for input to hidden layer
		for (; i < NB_INPUT_WEIGHTS; i++)
			weights[i] = rand.nextFloat() - 0.5f;

		// randomize weights for inner hidden layers
		for (int j = 1; j < NB_LAYERS; j++)
			for (int k = 0; k < NB_HIDDEN_WEIGHTS; k++, i++)
				weights[i] = rand.nextFloat() - 0.5f;

		// randomize weights for hidden layer to output
		for (int k = 0; k < NB_OUTPUT_WEIGHTS; k++, i++)
			weights[i] = rand.nextFloat() - 0.5f;
	}

	public float[] getOutput(ArrayList<Integer> inputs)
	{
		this.inputNeurons = inputs;

		propagateFromInputToHidden();
		propagateToHiddenLayers();
		propagateToOutput();

		return outputNeurons;
	}

	/**
	 * Propagate the inputs towards the first hidden layer for each neuron of
	 * the first hidden layer, we calculate the weighted sum of each input, then
	 * apply our activation function on the result then assign it to the hidden
	 * neuron
	 */
	private void propagateFromInputToHidden()
	{
		for (int n = 0; n < NB_HIDDEN; n++)
		{
			float value = 0;
			for (int input = 0; input < NB_INPUT; input++)
				value += inputNeurons.get(input) * inputToHidden(input, n);

			hiddenNeurons[getHiddenIndex(1, n)] = sigmoid(value);
		}
	}

	/**
	 * If there is more than one hidden layer Propagate the values for each
	 * neuron of each hidden layer in the same way
	 */
	private void propagateToHiddenLayers()
	{
		for (int i = 2; i <= NB_LAYERS; i++)
		{
			for (int j = 0; j < NB_HIDDEN; j++)
			{
				float value = 0;
				for (int k = 0; k < NB_HIDDEN; k++)
					value += hiddenNeurons[getHiddenIndex(i - 1, k)] * hiddenToHidden(i, k, j);

				hiddenNeurons[getHiddenIndex(i, j)] = sigmoid(value);
			}
		}
	}

	/**
	 * Propagate from the last hidden layer to the output layer
	 */
	private void propagateToOutput()
	{
		for (int i = 0; i < NB_OUTPUT; i++)
		{
			float value = 0;
			for (int j = 0; j < NB_HIDDEN; j++)
				value += hiddenNeurons[getHiddenIndex(NB_LAYERS, j)] * hiddenToOutput(j, i);

			outputNeurons[i] = sigmoid(value);
		}
	}

	/**
	 * Helper function to get the weights index of a hidden neuron
	 * 
	 * @param layer
	 *            the index of the layer the neurons belongs to
	 * @param index
	 *            the index of the neuron within its layer
	 * @return the index of the neuron in the weights array
	 */
	private int getHiddenIndex(int layer, int index)
	{
		return (layer - 1) * NB_HIDDEN + index;
	}

	/**
	 * Helper function to get the weight value of the synapse of a neuron from
	 * the input layer to the neurob of the first hidden layer
	 * 
	 * @param inputIndex
	 *            the index of the neuron of the input layer
	 * @param hiddenIndex
	 *            the index of the neuron of the first hidden layer
	 * @return
	 */
	private float inputToHidden(int inputIndex, int hiddenIndex)
	{
		return weights[NB_INPUT * hiddenIndex + inputIndex];
	}

	/**
	 * Helper function to get the weigh value of the synapse connecting two
	 * hidden neurons
	 */
	private float hiddenToHidden(int layerIndex, int startIndex, int endIndex)
	{
		return weights[NB_INPUT_WEIGHTS + ((layerIndex - 2) * NB_HIDDEN_WEIGHTS) + NB_HIDDEN * startIndex
		        + endIndex];
	}

	/**
	 * Helper function to get the weight value of the synapse of a neuron from
	 * the last hidden layer to the neuron of the output layer
	 */
	private float hiddenToOutput(int hiddenIndex, int outputIndex)
	{
		return weights[NB_INPUT_WEIGHTS + NB_TOTAL_HIDDEN_WEIGHTS + hiddenIndex * NB_OUTPUT + outputIndex];
	}

	/**
	 * Activation function, to normalize the output values
	 */
	private float sigmoid(float value)
	{
		if (Float.isNaN(value))
			System.out.println("value is NaN");
		// double d = 1 / 1 + Math.exp(-value);
		// float f = (float) d;
		return (float) Math.tanh(value);
	}

	public float[] getWeights()
	{
		return weights;
	}
}
