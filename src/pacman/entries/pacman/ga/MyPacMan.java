package pacman.entries.pacman.ga;

import pacman.controllers.Controller;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class MyPacMan extends Controller<MOVE>
{

	public MyPacMan()
	{
	}

	@Override
	public MOVE getMove(Game game, long timeDue)
	{
		return MOVE.DOWN;
	}
}
