package pacman.entries.pacman.genetic.mlp;

import java.util.ArrayList;

import pacman.controllers.Controller;
import pacman.entries.pacman.GameState;
import pacman.entries.pacman.NearGhost;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class NeuralNetworkPacMan extends Controller<MOVE>
{
	private final Perceptron perceptron;
	private final GameState state;
	private double score;

	public NeuralNetworkPacMan()
	{
		perceptron = new Perceptron();
		state = new GameState();
	}

	@Override
	public MOVE getMove(Game game, long timeDue)
	{
		int current = game.getPacmanCurrentNodeIndex();
		// let's explore each possible move
		for (MOVE move : game.getPossibleMoves(current))
		{
			// get new position for new current move
			int newCurrent = game.getCurrentMaze().graph[current].neighbourhood.get(move);

			state.update(game, newCurrent);
			ArrayList<Integer> input = new ArrayList<>();

			int i = Perceptron.NB_NEARGHOST_INPUT;
			for (NearGhost ghost : state.getNearestGhosts())
			{
				input.add((int) ghost.getDistance());
				input.add(game.getGhostEdibleTime(ghost.getType()));
				if (--i < 0)
					break;
			}
			input.add(state.getNearestPill());
			input.add(state.getNearestPowerPill());

			perceptron.getOutput(input);
		}

		return MOVE.DOWN;
	}

	public Perceptron getPerceptron()
	{
		return perceptron;
	}

	public double getScore()
	{
		return score;
	}

	public void setScore(double score)
	{
		this.score = score;
	}
}
