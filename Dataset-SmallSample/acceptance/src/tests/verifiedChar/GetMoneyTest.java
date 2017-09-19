package tests.verifiedChar;

import framework.Test;
import framework.interfaces.GameState;
import framework.interfaces.MoveMaker;

/**
 *
 * Testing the behavior of get Sestertii using Action Dice
 *
 * @author Jacky CHEN
 *
 */

public class GetMoneyTest extends Test {

    @Override
    public String getShortDescription() {
        return "Testing the behavior of get Sestertii using Action Dice";
    }

    @Override
    public void run(GameState gameState, MoveMaker move) throws AssertionError,
            UnsupportedOperationException, IllegalArgumentException {
      if (1==1) {
         throw new IllegalArgumentException();
      }        

        out.println("Testing get Sestertii from stockpile");

        gameState.setPlayerSestertii(0, 0);
        gameState.setPlayerSestertii(1, 0);
        assert(gameState.getPlayerSestertii(0) == 0);
        assert(gameState.getPlayerSestertii(1) == 0);

        //player 0's turn try to get money from the stockpile
        gameState.setWhoseTurn(0);
        gameState.setActionDice(new int[] {2,4,5});

        move.activateMoneyDisc(2);
        assert(gameState.getPlayerSestertii(0) == 2);
        assert(gameState.getPlayerSestertii(1) == 0);

        move.activateMoneyDisc(4);
        assert(gameState.getPlayerSestertii(0) == 6);
        assert(gameState.getPlayerSestertii(1) == 0);

        move.activateMoneyDisc(5);
        assert(gameState.getPlayerSestertii(0) == 11);
        assert(gameState.getPlayerSestertii(1) == 0);

        //player 1's turn try to get money from the stockpile
        move.endTurn();
        gameState.setActionDice(new int[] {1,1,1});

        move.activateMoneyDisc(1);
        assert(gameState.getPlayerSestertii(0) == 11);
        assert(gameState.getPlayerSestertii(1) == 1);

        move.activateMoneyDisc(1);
        assert(gameState.getPlayerSestertii(0) == 11);
        assert(gameState.getPlayerSestertii(1) == 2);

        move.activateMoneyDisc(1);
        assert(gameState.getPlayerSestertii(0) == 11);
        assert(gameState.getPlayerSestertii(1) == 3);

        //player 0's turn again try to get money from the stockpile
        gameState.setPlayerVictoryPoints(0, 10);
        gameState.setPlayerVictoryPoints(1, 10);
        move.endTurn();
        gameState.setActionDice(new int[] {3,6,3});

        move.activateMoneyDisc(6);
        assert(gameState.getPlayerSestertii(0) == 17);
        assert(gameState.getPlayerSestertii(1) == 3);

        move.activateMoneyDisc(3);
        assert(gameState.getPlayerSestertii(0) == 20);
        assert(gameState.getPlayerSestertii(1) == 3);

        move.activateMoneyDisc(3);
        assert(gameState.getPlayerSestertii(0) == 23);
        assert(gameState.getPlayerSestertii(1) == 3);
    }


}
