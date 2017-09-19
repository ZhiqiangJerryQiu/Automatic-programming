/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tests.verifiedChar;

import framework.Test;
import framework.interfaces.GameState;
import framework.interfaces.MoveMaker;

/**
 *
 * @author Matt
 */
public class GameStateInterfaceTest extends Test {

    @Override
    public String getShortDescription() {
        return "Tests your implementation of the GameState interface";
    }

    @Override
    public void run(GameState gameState, MoveMaker move) throws AssertionError, UnsupportedOperationException, IllegalArgumentException {
        gameState.setWhoseTurn(0);
        assert (gameState.getWhoseTurn() == 0);
        gameState.setWhoseTurn(1);
        assert (gameState.getWhoseTurn() == 1);

        gameState.setActionDice(new int[] {1,2,3});
        assert (gameState.getActionDice().length == 3);
        boolean[] seen = new boolean[3];
        int[] returned = gameState.getActionDice();
        for (int d : new int[] {1,2,3}) {
            for (int i = 0; i < 3; i++) {
                if (returned[i] == d) {
                    seen[i] = true;
                }
            }
        }
        for (int i = 0; i < 3; i++) {
            assert (seen[i] == true);
        }
    }


}
