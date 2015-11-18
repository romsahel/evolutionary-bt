/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pacman.entries.pacman.behaviortree.helpers;

import pacman.game.Game;

/**
 *
 * @author Romsahel
 */
public class Leaf extends Node
{
	private final Task task;
	private final boolean isInverter;

	public Leaf(Task task, boolean isInverter)
	{
		this.task = task;
		this.isInverter = isInverter;
	}

	@Override
	public boolean DoAction(Game game)
	{
		final boolean result = task.DoAction(game);
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
