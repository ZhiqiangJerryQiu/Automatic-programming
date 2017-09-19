package tests.verifiedChar;

import java.util.ArrayList;
import java.util.Collection;

import framework.Test;
import framework.cards.Card;
import framework.interfaces.GameState;
import framework.interfaces.MoveMaker;

/**
 * Testing the basic mechanics of Tribunus Plebis.
 *
 * @author Karla Burnett (karla.burnett)
 * @author Robert Cen (robert.cen)
 */

public class CardActivatorTribunusPlebisBasicTest extends Test {

    @Override
    public String getShortDescription() {
        return "Checking the basic mechanics of Tribunus Plebis";
    }

    @Override
    public void run(GameState gameState, MoveMaker move)
                                          throws AssertionError,
                                          UnsupportedOperationException,
                                          IllegalArgumentException {

        // Set up the player stats
        gameState.setPlayerVictoryPoints(0, 10);
        gameState.setPlayerVictoryPoints(1, 10);
        gameState.setPlayerSestertii(0, 10);
        gameState.setPlayerSestertii(1, 0);

        // Set up the game state for the test
        gameState.setWhoseTurn(0);
        gameState.setActionDice(new int [] {3, 3, 4});

        Collection<Card> hand = new ArrayList<Card>();
        hand.add(Card.TRIBUNUSPLEBIS);
        hand.add(Card.TRIBUNUSPLEBIS);
        gameState.setPlayerHand(0, hand);

        // Place the Tribunus Plebis on disc 3 and activate it
        move.placeCard(Card.TRIBUNUSPLEBIS, 3);
        move.chooseCardToActivate(3).complete();

        // Check that player 0 has gained a victory point, but player
        // 1's score has not changed
        assert(gameState.getPlayerVictoryPoints(0) == 11);
        assert(gameState.getPlayerVictoryPoints(1) == 9);
        assert(gameState.getPlayerSestertii(0) == 5);
        assert(gameState.getPlayerSestertii(1) == 0);

        // Place a tribunus plebis on the 4, to check that only the one
        // card is activated
        move.placeCard(Card.TRIBUNUSPLEBIS, 4);
        move.chooseCardToActivate(4).complete();

        // Check that player 0 has gained a victory point, but player
        // 1's score has not changed
        assert(gameState.getPlayerVictoryPoints(0) == 12);
        assert(gameState.getPlayerVictoryPoints(1) == 8);
        assert(gameState.getPlayerSestertii(0) == 0);
        assert(gameState.getPlayerSestertii(1) == 0);
    }
}
