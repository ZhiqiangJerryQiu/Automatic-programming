package tests.verifiedBuilding;

import java.util.ArrayList;
import java.util.Collection;

import framework.Rules;
import framework.Test;
import framework.cards.Card;
import framework.interfaces.GameState;
import framework.interfaces.MoveMaker;
import framework.interfaces.activators.ForumActivator;

/**
 *
 * Test the basic functionality of Basilica
 *
 * @author Jacky CHEN
 *
 */

public class CardActivatorBasilicaBasicTest extends Test {

    @Override
    public String getShortDescription() {
        return "Test the basic functionality of Basilica";
    }

    @Override
    public void run(GameState gameState, MoveMaker move) throws AssertionError,
            UnsupportedOperationException, IllegalArgumentException {



        gameState.setWhoseTurn(0);
        gameState.setPlayerVictoryPoints(0, 10);
        gameState.setPlayerVictoryPoints(1, 10);
        gameState.setPlayerSestertii(0, 30);
        assert(gameState.getPoolVictoryPoints() == 16);

        Collection<Card> hand = new ArrayList<Card>();
        hand.add(Card.FORUM);
        hand.add(Card.BASILICA);

        gameState.setPlayerHand(0, hand);

        //no cards on the field
        Card[] field = new Card[Rules.NUM_DICE_DISCS];
        for(int i = 0; i < field.length; i++) {
            field[i] = Card.NOT_A_CARD;
        }

        gameState.setPlayerCardsOnDiscs(0, field);
        gameState.setPlayerCardsOnDiscs(1, field);

        gameState.setActionDice(new int[] {2,2,2});

        //================ test starts ===================
        move.placeCard(Card.FORUM, Rules.DICE_DISC_2);
        assert(gameState.getPlayerSestertii(0) == 25);

        move.placeCard(Card.BASILICA, Rules.DICE_DISC_1);
        assert(gameState.getPlayerSestertii(0) == 19);

        ForumActivator activator = (ForumActivator) move.chooseCardToActivate(Rules.DICE_DISC_2);
        activator.chooseActionDice(2);
        activator.chooseActivateTemplum(false);
        activator.complete();

        assert(gameState.getPlayerVictoryPoints(0) == 14);
        assert(gameState.getPoolVictoryPoints() == 12);

    }
}
