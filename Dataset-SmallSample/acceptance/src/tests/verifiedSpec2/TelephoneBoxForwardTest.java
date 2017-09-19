/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tests.verifiedSpec2;

import framework.Rules;
import framework.Test;
import framework.cards.Card;
import framework.interfaces.GameState;
import framework.interfaces.MoveMaker;
import framework.interfaces.activators.TelephoneBoxActivator;

/**
 *
 * @author Matt
 */
public class TelephoneBoxForwardTest extends Test {

    @Override
    public String getShortDescription() {
        return "Test telephone box forward movement";
    }

    @Override
    public void run(GameState gameState, MoveMaker move) throws AssertionError, UnsupportedOperationException, IllegalArgumentException {

        // Set the cards to all Tribunis Plebisis, to remove start of turn penalties
        Card[] startingCards = new Card[Rules.NUM_DICE_DISCS];
        for (int i = 0; i < Rules.NUM_DICE_DISCS; i++) {
            startingCards[i] = Card.TRIBUNUSPLEBIS;
        }
        gameState.setPlayerCardsOnDiscs(0, startingCards);

        for (int i = 0; i < Rules.NUM_DICE_DISCS - 1; i++) {
            startingCards[i] = Card.TRIBUNUSPLEBIS;
        }
        startingCards[Rules.NUM_DICE_DISCS - 1] = Card.TELEPHONEBOX;
        gameState.setPlayerCardsOnDiscs(1, startingCards);
        // Check no start of turn penalty is applied

        gameState.setPlayerSestertii(1, 54);

        gameState.setWhoseTurn(0);
        move.endTurn();

        gameState.setActionDice(new int[] {1,2,3});

        assert (gameState.getWhoseTurn() == 1);
        assert (gameState.getPlayerCardsOnDiscs(1)[5] == Card.TRIBUNUSPLEBIS);
        assert (gameState.getPlayerCardsOnDiscs(1)[6] == Card.TELEPHONEBOX);

        TelephoneBoxActivator activator = (TelephoneBoxActivator) move.activateBribeDisc(1);
        activator.shouldMoveForwardInTime(true);
        activator.setSecondDiceUsed(2);
        activator.chooseDiceDisc(Rules.DICE_DISC_6);
        activator.complete();

        assert (gameState.getPlayerCardsOnDiscs(1)[5] == Card.NOT_A_CARD);
        assert (gameState.getPlayerCardsOnDiscs(1)[6] == Card.TELEPHONEBOX);

        move.endTurn();

        assert (gameState.getPlayerCardsOnDiscs(1)[5] == Card.NOT_A_CARD);
        assert (gameState.getPlayerCardsOnDiscs(1)[6] == Card.TELEPHONEBOX);

        move.endTurn();

        assert (gameState.getPlayerCardsOnDiscs(1)[5] == Card.TRIBUNUSPLEBIS);
        assert (gameState.getPlayerCardsOnDiscs(1)[6] == Card.TELEPHONEBOX);
    }

}
