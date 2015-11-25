/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pacman.entries.pacman.behaviortree.helpers;

import pacman.entries.pacman.GameState;
import pacman.entries.pacman.behaviortree.BTPacMan;
import pacman.entries.pacman.behaviortree.TreeGenerator;
import pacman.game.Game;

/**
 *
 * @author Romsahel
 */
public class Leaf extends Node implements java.io.Serializable
{
	private final Task task;
	private final boolean isInverter;

	public Leaf(Task task, boolean isInverter)
	{
		this.task = task;
		this.isInverter = isInverter;
	}

	public Leaf(Leaf copy, Composite parent, BTPacMan tree)
	{
		this.isInverter = copy.isInverter;
		this.type = copy.type;
		this.parent = parent;
		this.task = copy.task;

		final TreeGenerator treeGenerator = tree.getTreeGenerator();
		treeGenerator.getLeaves().add(copy);
	}

	@Override
	public boolean DoAction(Game game, BTPacMan parent, GameState state)
	{
		final boolean result = task.DoAction(game, parent, state);
		return (isInverter) ? !result : result;
	}

	/**
	 * @return the task
	 */
	public Task getTask()
	{
		return task;
	}

	/**
	 * @return the isInverter
	 */
	public boolean isInverter()
	{
		return isInverter;
	}

}
