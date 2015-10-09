package pacman.entries.rosa.pacman.genetic;

import pacman.controllers.Controller;
import pacman.entries.rosa.pacman.genetic.mlp.MLPTrainer;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class MyPacMan extends Controller<MOVE>
{
	private final MLPTrainer trainer;

	public MyPacMan(boolean train)
	{
		System.out.println("Training: beginning...");
		trainer = new MLPTrainer(train);
		System.out.println("Training: ended.");
	}

	@Override
	public MOVE getMove(Game game, long timeDue)
	{
		return trainer.getBestPacMan().getMove(game, timeDue);
	}
}
