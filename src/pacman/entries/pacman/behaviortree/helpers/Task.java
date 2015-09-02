/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pacman.entries.pacman.behaviortree.helpers;

import pacman.game.Game;

/**
 *
 * @author romsahel
 */
public abstract class Task
{
    public abstract boolean DoAction(Game game);
}
