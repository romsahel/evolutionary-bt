package pacman.entries.pacman.behaviortree;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import pacman.controllers.Controller;
import pacman.entries.pacman.GameState;
import pacman.entries.pacman.behaviortree.helpers.Composite;
import pacman.entries.pacman.behaviortree.helpers.Inverter;
import pacman.entries.pacman.behaviortree.helpers.Leaf;
import pacman.entries.pacman.behaviortree.helpers.Node;
import pacman.entries.pacman.behaviortree.helpers.Selector;
import pacman.entries.pacman.behaviortree.helpers.Sequence;
import pacman.entries.pacman.behaviortree.tasks.actions.AvoidPowerpillTask;
import pacman.entries.pacman.behaviortree.tasks.actions.ChasePowerPillTask;
import pacman.entries.pacman.behaviortree.tasks.actions.ChaseTask;
import pacman.entries.pacman.behaviortree.tasks.actions.EatPillTask;
import pacman.entries.pacman.behaviortree.tasks.actions.GoToJunctionTask;
import pacman.entries.pacman.behaviortree.tasks.actions.RunAwayMultipleTask;
import pacman.entries.pacman.behaviortree.tasks.actions.RunAwayTask;
import pacman.entries.pacman.behaviortree.tasks.conditions.AreGhostFarAwayTask;
import pacman.entries.pacman.behaviortree.tasks.conditions.IsGhostCloserTask;
import pacman.entries.pacman.behaviortree.tasks.conditions.IsGhostEdibleTask;
import pacman.entries.pacman.behaviortree.tasks.conditions.IsGhostNearTask;
import pacman.entries.pacman.behaviortree.tasks.conditions.IsPacmanAtJunction;
import pacman.entries.pacman.behaviortree.tasks.conditions.IsPathToJunctionSafeTask;
import pacman.entries.pacman.behaviortree.tasks.conditions.IsPathToPowerPillSafeTask;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class MyPacMan extends Controller<MOVE>
{

	private static final int MAX_DEPTH = 10;
	private static final int MAX_CHILDREN = 3;
	private static final int LEAF_TYPE = 0;
	private static final int SELECTOR_TYPE = 1;
	private static final int SEQUENCE_TYPE = 2;

	private static Leaf[] setOfActions, setOfConditions;

	private final Random random;

	/*
	 * Root of the behavior tree
	 */
	private final Composite rootNode;

	private final GameState state;

	/*
	 * Type of MOVE that will be returned at each game step
	 */
	private MOVE move;

	public MyPacMan()
	{
		state = new GameState();
		rootNode = new Selector();
		rootNode.setMaxDepth(MAX_DEPTH);
		random = new Random();

		setOfActions = new Leaf[]
		{
			new AvoidPowerpillTask(this),
			new ChasePowerPillTask(this),
			new ChaseTask(this),
			new EatPillTask(this),
			new GoToJunctionTask(this),
			new RunAwayMultipleTask(this),
			new RunAwayTask(this)
		};
		setOfConditions = new Leaf[]
		{
			new AreGhostFarAwayTask(this),
			new IsGhostCloserTask(this),
			new IsGhostEdibleTask(this),
			new IsGhostNearTask(this),
			new IsPacmanAtJunction(this),
			new IsPathToJunctionSafeTask(this),
			new IsPathToPowerPillSafeTask(this)
		};

		generateChildren(rootNode);
		printTree(rootNode);
	}

	private void printTree(Composite root)
	{
		System.out.println("#lineWidth: 2\n" + "#padding: 12\n" + "#arrowSize: 0.7\n");
		HashMap<String, Integer> map = new HashMap<>();
		final int nodeId = getId(map, root.getClass().getSimpleName());
		System.out.println("[" + getClassifier(root) + root.getClass().getSimpleName() + " " + nodeId + "]");
		printTree(map, rootNode, nodeId);
	}

	private int getId(HashMap<String, Integer> map, String key)
	{
		int id = 0;
		if (map.containsKey(key))
			id = map.get(key);
		map.put(key, id + 1);
		return id;
	}

	private void printTree(HashMap<String, Integer> map, Composite root, int id)
	{
		for (Node node : root.getChildren())
		{
			int nodeId = getId(map, node.getClass().getSimpleName());
			if (node.getClass() == Inverter.class)
				System.out.println("[" + root.getClass().getSimpleName() + " " + id + "] -> [NOT "
								   + ((Inverter) node).getInvertedNode().getClass().getSimpleName() + " " + nodeId + "]");
			else
				System.out.println("[" + root.getClass().getSimpleName() + " " + id + "] -> ["
								   + getClassifier(node) + node.getClass().getSimpleName() + " " + nodeId + "]");

			if (node instanceof Composite)
			{
				Composite composite = (Composite) node;
				printTree(map, composite, nodeId);
			}
		}
	}

	private String getClassifier(Node node)
	{
		String classifier = "";
		if (node instanceof Composite)
			if (node.getClass() == Selector.class)
				classifier = "<choice> ";
			else
				classifier = "<state> ";
		else if (Arrays.asList(setOfConditions).contains((Leaf) node))
			classifier = "<abstract> ";

		return classifier;
	}

	private Node generate(Composite root)
	{
		int type;
		if (root.getDepth() >= root.getMaxDepth())
			type = LEAF_TYPE;
		else
			type = random.nextInt(SEQUENCE_TYPE + 1);

		if (type == LEAF_TYPE)
			if (root.getChildrenCount() == root.nbChildren - 1)
				return setOfActions[random.nextInt(setOfActions.length)];
			else
				return generateCondition(root);
		else
			return generateComposite(type, root);
	}

	private Node generateComposite(int type, Composite root)
	{
		Composite node;
		if (type == SELECTOR_TYPE)
			node = new Selector();
		else
			node = new Sequence();
		node.setDepth(root.getDepth() + 1);
		node.setMaxDepth(random.nextInt(MAX_DEPTH + 1));
		generateChildren(node);
		return node;
	}

	private Node generateCondition(Composite root)
	{
		Leaf leaf = null;
		do
		{
			leaf = setOfConditions[random.nextInt(setOfConditions.length)];
		} while (rootHasNode(root, leaf));

		if (random.nextInt(100) < 10)
			return new Inverter(leaf);
		return leaf;
	}

	private void generateChildren(Composite node)
	{
		node.nbChildren = random.nextInt(MAX_CHILDREN + 1 - 2) + 2;
		for (int i = 0; i < node.nbChildren; i++)
			node.addChildren(generate(node));
	}

	private boolean rootHasNode(Composite root, Leaf leaf)
	{
		for (Node n : root.getChildren())
			if (n.getClass() == leaf.getClass()
				|| ((n instanceof Inverter)
					&& ((Inverter) n).getInvertedNode().getClass() == leaf.getClass()))
				return true;
		return false;
	}

	@Override
	public MOVE getMove(Game game, long timeDue)
	{
		state.update(game);
		rootNode.DoAction(game);
		if (Composite.DEBUG)
			System.out.println("===================");
		return move;
	}

	/**
	 * @param move
	 *             the move to set
	 */
	public void setMove(MOVE move)
	{
		this.move = move;
	}

	public GameState getState()
	{
		return state;
	}

}
