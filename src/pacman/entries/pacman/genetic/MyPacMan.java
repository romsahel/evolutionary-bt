package pacman.entries.pacman.genetic;

import pacman.controllers.Controller;
import pacman.entries.pacman.genetic.mlp.MLPTrainer;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class MyPacMan extends Controller<MOVE>
{
	private final MLPTrainer trainer;

	public MyPacMan()
	{
		trainer = new MLPTrainer();
	}

	@Override
	public MOVE getMove(Game game, long timeDue)
	{
		return trainer.getBestPacMan().getMove(game, timeDue);
	}
}
