package pacman.entries.pacman.genetic.mlp;

import java.awt.Color;
import java.util.ArrayList;

import pacman.controllers.Controller;
import pacman.controllers.examples.StarterGhosts;
import pacman.entries.pacman.GameState;
import pacman.entries.pacman.NearGhost;
import pacman.game.Constants;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import pacman.game.GameView;

public class NeuralNetworkPacMan extends Controller<MOVE>implements Comparable<NeuralNetworkPacMan>
{
	private static final StarterGhosts GHOSTS = new StarterGhosts();
	private final Perceptron perceptron;
	private final GameState state;
	private double score;

	public NeuralNetworkPacMan(NeuralNetworkPacMan copy)
	{
		perceptron = new Perceptron(copy.getPerceptron().getWeights());
		state = new GameState();
	}

	public NeuralNetworkPacMan(Perceptron perceptron)
	{
		this.perceptron = perceptron;
		state = new GameState();
	}

	public NeuralNetworkPacMan(NeuralNetworkPacMan parent1, NeuralNetworkPacMan parent2)
	{
		perceptron = new EvolvingPerceptron(parent1.getPerceptron(), parent2.getPerceptron());
		state = new GameState();
	}

	public NeuralNetworkPacMan()
	{
		perceptron = new Perceptron();
		state = new GameState();
	}

	@Override
	public MOVE getMove(Game game, long timeDue)
	{
		int current = game.getPacmanCurrentNodeIndex();
		MOVE bestMove = null;
		float bestScore = -2;
		// let's explore each possible move
		state.update(game);
		MOVE[] possibleMoves;
//		if (state.getNearestGhost() != null && game.getGhostEdibleTime(state.getNearestGhost().getType()) <= 2 && state.getNearestGhost().getDistance() < 10)
//			possibleMoves = game.getPossibleMoves(current);
//		else
			possibleMoves = game.getPossibleMoves(current, game.getPacmanLastMoveMade());
		
		for (MOVE move : possibleMoves)
		{
			Game tmpGame = game.copy();
			// get new position for new current move
			tmpGame.advanceGame(move, GHOSTS.getMove());
			
			state.update(tmpGame);
			GameView.addPoints(tmpGame, Color.RED, state.getCurrent());
			
			ArrayList<Integer> input = new ArrayList<>();

			int i = Perceptron.NB_NEARGHOST_INPUT;
			for (NearGhost ghost : state.getNearestGhosts())
			{
				input.add((int) ghost.getDistance());
				input.add(tmpGame.getGhostEdibleTime(ghost.getType()) - Constants.EDIBLE_TIME / 2);
				if (--i <= 0)
					break;
			}
			for (; i > 0; i--)
			{
				input.add(999);
				input.add(0);
			}
			input.add(state.getNearestPill());
			input.add(state.getNearestPowerPill());
			input.add(tmpGame.getScore());
			input.add(tmpGame.getPacmanNumberOfLivesRemaining());

			float[] score = perceptron.getOutput(input);
			if (score[0] > bestScore)
			{
				bestScore = score[0];
				bestMove = move;
			}
		}
		return bestMove;
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

	@Override
	public int compareTo(NeuralNetworkPacMan o)
	{
		return (int) (o.getScore() - this.getScore());
	}
}
