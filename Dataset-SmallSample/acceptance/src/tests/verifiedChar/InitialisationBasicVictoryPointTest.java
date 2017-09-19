package tests.verifiedChar;

import framework.Test;
import framework.interfaces.GameState;
import framework.interfaces.MoveMaker;

/**
 * Testing the basic mechanics of victory point addition and removal.
 *
 * @author Karla Burnett (karla.burnett)
 */
public class InitialisationBasicVictoryPointTest extends Test {

    @Override
    public String getShortDescription() {
        return "Checking the basic mechanics of Victory Points";
    }

    @Override
    public void run(GameState gameState, MoveMaker move)
                                          throws AssertionError,
                                          UnsupportedOperationException,
                                          IllegalArgumentException {
        
        // Check that players can have no victory points,
        // And that the total number of victory points is 36
        gameState.setPlayerVictoryPoints(0, 0);
        gameState.setPlayerVictoryPoints(1, 0);
        assert(gameState.getPlayerVictoryPoints(0) == 0);
        assert(gameState.getPlayerVictoryPoints(1) == 0);
        assert(gameState.getPoolVictoryPoints() == 36);
        
        // Check that players can have a different number of victory points
        gameState.setPlayerVictoryPoints(0, 10);
        gameState.setPlayerVictoryPoints(1, 5);        
        assert(gameState.getPlayerVictoryPoints(0) == 10);
        assert(gameState.getPlayerVictoryPoints(1) == 5);
        assert(gameState.getPoolVictoryPoints() == 21);
        
        // Check that it is possible to set each player's victory points
        // individually
        gameState.setPlayerVictoryPoints(0, 20);
        assert(gameState.getPlayerVictoryPoints(0) == 20);
        assert(gameState.getPlayerVictoryPoints(1) == 5);
        assert(gameState.getPoolVictoryPoints() == 11);
        
        gameState.setPlayerVictoryPoints(1, 1);
        assert(gameState.getPlayerVictoryPoints(0) == 20);
        assert(gameState.getPlayerVictoryPoints(1) == 1);
        assert(gameState.getPoolVictoryPoints() == 15);
        
        // Check that victory points and sestertii act independently
        gameState.setPlayerSestertii(0, 100);
        gameState.setPlayerSestertii(1, 23);
        gameState.setPlayerVictoryPoints(0, 15);
        gameState.setPlayerVictoryPoints(1, 2);        
        
        assert(gameState.getPlayerSestertii(0) == 100);
        assert(gameState.getPlayerSestertii(1) == 23);
        assert(gameState.getPlayerVictoryPoints(0) == 15);
        assert(gameState.getPlayerVictoryPoints(1) == 2);
        assert(gameState.getPoolVictoryPoints() == 19);
    }
}
