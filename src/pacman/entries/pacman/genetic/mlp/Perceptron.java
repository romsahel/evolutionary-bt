package pacman.entries.pacman.genetic.mlp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Perceptron
{
	protected static final int MUTATION_CHANCES = 10;
	public static final int NB_NEARGHOST_INPUT = 2;
	protected static final int NB_INPUT = NB_NEARGHOST_INPUT * 2 + 2 + 2;
	protected static final int NB_LAYERS = 2;
	protected static final int NB_HIDDEN = NB_INPUT * 4;
	
	protected static final int NB_OUTPUTS = 1;
	protected static final int NB_INPUT_WEIGHTS = NB_INPUT * NB_HIDDEN;
	protected static final int NB_HIDDEN_WEIGHTS = NB_HIDDEN * NB_HIDDEN;
	protected static final int NB_TOTAL_HIDDEN_WEIGHTS = NB_HIDDEN * NB_HIDDEN * (NB_LAYERS - 1);
	protected static final int NB_OUTPUT_WEIGHTS = NB_HIDDEN * NB_OUTPUTS;
	protected static final int NB_TOTAL_WEIGHTS = NB_INPUT_WEIGHTS + NB_TOTAL_HIDDEN_WEIGHTS + NB_OUTPUT_WEIGHTS;

	protected static final Random rand = new Random();

	protected ArrayList<Integer> inputNeurons;
	protected final float[] hiddenNeurons;
	protected final float[] outputNeurons;
	protected final float[] weights;

	public Perceptron(float[] weights)
	{
		inputNeurons = new ArrayList<>(NB_INPUT);
		hiddenNeurons = new float[NB_LAYERS * NB_HIDDEN];
		outputNeurons = new float[NB_OUTPUTS];
		if (weights == null)
			this.weights = new float[Perceptron.NB_TOTAL_WEIGHTS];
		else
			this.weights = Arrays.copyOf(weights, weights.length);
	}

	public Perceptron()
	{
		this(null);

		// randomize all weights
		for (int i = 0; i < NB_TOTAL_WEIGHTS; i++)
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
	protected void propagateFromInputToHidden()
	{
		for (int n = 0; n < NB_HIDDEN; n++)
		{
			float value = 0;
			for (int input = 0; input < NB_INPUT; input++)
				value += inputNeurons.get(input) * getInputToHiddenWeight(input, n);

			hiddenNeurons[getHiddenIndex(1, n)] = activate(value);
		}
	}

	/**
	 * If there is more than one hidden layer, propagate the values for each
	 * neuron of each hidden layer in the same way
	 */
	protected void propagateToHiddenLayers()
	{
		for (int i = 2; i <= NB_LAYERS; i++)
		{
			for (int j = 0; j < NB_HIDDEN; j++)
			{
				float value = 0;
				for (int k = 0; k < NB_HIDDEN; k++)
					value += hiddenNeurons[getHiddenIndex(i - 1, k)] * getHiddenWeight(i, k, j);

				hiddenNeurons[getHiddenIndex(i, j)] = activate(value);
			}
		}
	}

	/**
	 * Propagate from the last hidden layer to the output layer
	 */
	protected void propagateToOutput()
	{
		for (int i = 0; i < NB_OUTPUTS; i++)
		{
			float value = 0;
			for (int j = 0; j < NB_HIDDEN; j++)
				value += hiddenNeurons[getHiddenIndex(NB_LAYERS, j)] * getHiddenToOutputWeight(j, i);

			outputNeurons[i] = activate(value);
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
	protected int getHiddenIndex(int layer, int index)
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
	protected float getInputToHiddenWeight(int inputIndex, int hiddenIndex)
	{
		return weights[NB_INPUT * hiddenIndex + inputIndex];
	}

	/**
	 * Helper function to get the weigh value of the synapse connecting two
	 * hidden neurons
	 */
	protected float getHiddenWeight(int layerIndex, int originIndex, int endIndex)
	{
		return weights[NB_INPUT_WEIGHTS + ((layerIndex - 2) * NB_HIDDEN_WEIGHTS) + NB_HIDDEN * originIndex
		        + endIndex];
	}

	/**
	 * Helper function to get the weight value of the synapse of a neuron from
	 * the last hidden layer to the neuron of the output layer
	 */
	protected float getHiddenToOutputWeight(int hiddenIndex, int outputIndex)
	{
		return weights[NB_INPUT_WEIGHTS + NB_TOTAL_HIDDEN_WEIGHTS + hiddenIndex * NB_OUTPUTS + outputIndex];
	}

	/**
	 * Activation function, to normalize the output values
	 */
	protected float activate(float value)
	{
		if (Float.isNaN(value))
			System.out.println("value is NaN");
		// return (float) Math.tanh(value);
		return (float) (1 / (1 + Math.exp(-value)));
	}

	public float[] getWeights()
	{
		return weights;
	}
}
