package pacman.entries.pacman.behaviortree.tasks.actions;

import pacman.entries.pacman.behaviortree.BTPacMan;
import pacman.entries.pacman.behaviortree.helpers.Leaf;
import pacman.game.Constants.*;
import pacman.game.Game;

public class RunAwayMultipleTask extends Leaf {
    public RunAwayMultipleTask(BTPacMan parent)
    {
        super(parent);
    }

    @Override
    public boolean DoAction(Game game)
    {
        int numOfLeft = 0;
        int numOfRight = 0;
        int numOfUp = 0;
        int numOfDown = 0;

        MOVE move;
        for(GHOST ghost : GHOST.values()){
            MOVE dir = game.getNextMoveAwayFromTarget(state.getCurrent(),game.getGhostCurrentNodeIndex(ghost),DM.PATH);
            switch(dir){
                case DOWN:
                    numOfDown++;
                    break;
                case UP:
                    numOfUp++;
                    break;
                case LEFT:
                    numOfLeft++;
                    break;
                case RIGHT:
                    numOfRight++;
                    break;
                default:
                    break;
            }
        }

        if(numOfDown >= 3){
            move = MOVE.DOWN;
        }
        else if(numOfUp >= 3){
            move = MOVE.UP;
        }
        else if(numOfLeft >= 3){
            move = MOVE.LEFT;
        }
        else if(numOfRight >= 3){
            move = MOVE.RIGHT;
        }
        else{
            move = MOVE.NEUTRAL;
        }

        parent.setMove(move);

        return true;
    }
}
