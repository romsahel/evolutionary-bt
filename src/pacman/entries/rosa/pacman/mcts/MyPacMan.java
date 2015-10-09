package pacman.entries.rosa.pacman.mcts;

import java.util.EnumMap;
import java.util.Random;

import pacman.controllers.Controller;
import pacman.controllers.examples.StarterGhosts;
import pacman.entries.rosa.pacman.GameState;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class MyPacMan extends Controller<MOVE>
{
	private static final Random random = new Random();
	private final Controller<EnumMap<GHOST, MOVE>> ghosts = new StarterGhosts();
	private static final GameState state = new GameState();

	public MyPacMan()
	{
	}

	@Override
	public MOVE getMove(Game game, long timeDue)
	{
		Node root = new Node(game.copy());

		while (System.currentTimeMillis() < timeDue - 10)
		{
			Node node = treePolicy(root);
			double score = defaultPolicy(node);
			backup(node, score);
		}

		Node bestChild = root.getBestChild();
		return bestChild.getMove();
	}

	/**
	 * Select a node that should be most promising and expands it
	 * @param node
	 * @return
	 */
	private Node treePolicy(Node node)
	{
		int livesRemaining = node.getGame().getPacmanNumberOfLivesRemaining();
		int currentLevel = node.getGame().getCurrentLevel();

		// terminal conditions are: game over, losing a life, finishing a level
		// this allows to reduce the depth of the tree
		while (!node.getGame().gameOver()
		        && livesRemaining == node.getGame().getPacmanNumberOfLivesRemaining()
		        && currentLevel == node.getGame().getCurrentLevel()
		        && node.depth < 20)
		{
			if (!isFullyExpanded(node))
				return expand(node);
			else
				node = node.getBestUCTChild();
		}
		return node;
	}

	/**
	 * Simulate the game until a terminal condition is reached (i.e. gameover, a
	 * life is lost, next level is reached)
	 * 
	 * @param node
	 *            the root node of the simulation
	 * @return the reward value
	 */
	private double defaultPolicy(Node node)
	{
		Game game = node.getGameCopy();
		int currentLevel = game.getCurrentLevel();

		while (!game.gameOver())
		{
			int livesRemaining = game.getPacmanNumberOfLivesRemaining();
			EnumMap<GHOST, MOVE> ghostMove = ghosts.getMove(game, System.currentTimeMillis() + 1);
			MOVE[] moves = getPossibleMoves(game);
			MOVE pacManMove = moves[random.nextInt(moves.length)];
			
			game.advanceGame(pacManMove, ghostMove);

			// interrupt playout if a life is lost
			if (livesRemaining > game.getPacmanNumberOfLivesRemaining())
				break;
			if (currentLevel != game.getCurrentLevel())
				return Double.MAX_VALUE;
		}
		// System.out.println(game.getScore());
		return game.getScore() - game.getNumberOfActivePills() * 100;
	}

	/**
	 * Back-propagates the reward to all parent nodes
	 * 
	 * @param node
	 *            the node associated with the reward
	 * @param score
	 *            the reward to be propagated
	 */
	private void backup(Node node, double score)
	{
		while (node != null)
		{
			node.visit();
			node.addScore(score);
			node = node.getParent();
		}
	}

	/**
	 * Expand the given node by choosing a random legal move (that has not
	 * already been tried) and adding it to the tree
	 * 
	 * @param node
	 *            the node to expand
	 * @return the created node
	 */
	private Node expand(Node node)
	{
		Game game = node.getGameCopy();

		MOVE[] possibleMoves = getPossibleMoves(game);

		int index = random.nextInt(possibleMoves.length);
		while (moveAlreadyTried(node, possibleMoves[index]))
			index = (index + 1) % possibleMoves.length;

		do
		{
			game.advanceGame(possibleMoves[index], ghosts.getMove(game, System.currentTimeMillis() + 1));
		} while (!game.isJunction(game.getPacmanCurrentNodeIndex()) && !game.gameOver());

		Node child = new Node(game, node, possibleMoves[index]);

		node.getChildren().add(child);

		return child;
	}

	/**
	 * Determines if the given move has already been tried
	 * 
	 * @return true if the move has already been tried
	 */
	private boolean moveAlreadyTried(Node node, MOVE move)
	{
		for (Node child : node.getChildren())
		{
			if (child.getMove() == move)
				return true;
		}
		return false;
	}

	/**
	 * Determines if the given node has already been fully expanded, i.e. if all
	 * possible moves from that state has already been simulated
	 * 
	 * @param node
	 *            the node to check if expanded
	 * @return true if the node cannot be expanded anymore
	 */
	private boolean isFullyExpanded(Node node)
	{
		return getPossibleMoves(node.getGame()).length == node.getChildren().size();
	}

	private MOVE[] getPossibleMoves(Game game)
	{
		int current = game.getPacmanCurrentNodeIndex();
		MOVE lastMove = game.getPacmanLastMoveMade();
		return game.getPossibleMoves(current, lastMove);
	}
}
