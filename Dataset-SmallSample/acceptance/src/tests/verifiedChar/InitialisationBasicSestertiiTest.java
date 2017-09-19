package tests.verifiedChar;

import framework.Test;
import framework.interfaces.GameState;
import framework.interfaces.MoveMaker;

/**
 * Testing the basic mechanics of Sestertii addition and removal.
 *
 * @author Karla Burnett (karla.burnett)
 */
public class InitialisationBasicSestertiiTest extends Test {

    @Override
    public String getShortDescription() {
        return "Checking the basic mechanics of Sestertii";
    }

    @Override
    public void run(GameState gameState, MoveMaker move)
                                          throws AssertionError,
                                          UnsupportedOperationException,
                                          IllegalArgumentException {
        
        // Check that players can have no sestertii
        gameState.setPlayerSestertii(0, 0);
        gameState.setPlayerSestertii(1, 0);
        assert(gameState.getPlayerSestertii(0) == 0);
        assert(gameState.getPlayerSestertii(1) == 0);
        
        // Check that players can have different numbers of sestertii
        gameState.setPlayerSestertii(0, 5);
        gameState.setPlayerSestertii(1, 10);        
        assert(gameState.getPlayerSestertii(0) == 5);
        assert(gameState.getPlayerSestertii(1) == 10);
        
        // Check that it is possible to set each player's sestertii
        // individually
        gameState.setPlayerSestertii(0, 20);
        assert(gameState.getPlayerSestertii(0) == 20);
        assert(gameState.getPlayerSestertii(1) == 10);
        
        gameState.setPlayerSestertii(1, 1);
        assert(gameState.getPlayerSestertii(0) == 20);
        assert(gameState.getPlayerSestertii(1) == 1);
    }
}
