package tests.verifiedBuilding;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import framework.Rules;
import framework.Test;
import framework.cards.Card;
import framework.interfaces.GameState;
import framework.interfaces.MoveMaker;
import framework.interfaces.activators.ArchitectusActivator;

/**
 *
 * Test the basic functionality of Architectus
 *
 * @author Damon Stacey
 * @author Lauren Spooner
 *
 */

public class CardActivatorArchitectusZBasicTest extends Test {

    @Override
    public String getShortDescription() {
        return "Test the basic functionality of Architectus";
    }

    @Override
    public void run(GameState gameState, MoveMaker move) throws AssertionError,
            UnsupportedOperationException, IllegalArgumentException {

        gameState.setWhoseTurn(0);

        List<Card> discard = new ArrayList<Card>();
        gameState.setDiscard(discard);

        Collection<Card> hand = new ArrayList<Card>();
        hand.add(Card.ARCHITECTUS);
        hand.add(Card.TURRIS);
        hand.add(Card.MACHINA);
        hand.add(Card.FORUM);
        hand.add(Card.LEGAT);
        hand.add(Card.ONAGER);

        gameState.setPlayerHand(0, hand);

        //no cards on the field
        Card[] field = new Card[Rules.NUM_DICE_DISCS];
        for(int i = 0; i < field.length; i++) {
            field[i] = Card.NOT_A_CARD;
        }
        gameState.setPlayerCardsOnDiscs(0, field);
        gameState.setPlayerCardsOnDiscs(1, field);

        //player has enough Sestertiis to lay cards
        gameState.setPlayerSestertii(0, 30);

        gameState.setActionDice(new int[] {1,1,5});

        //===================== test1 ======================
        move.placeCard(Card.ARCHITECTUS, Rules.DICE_DISC_1);

        //the correct amount of money should be deducted
        //game removed from hand and get placed on the field
        assert(gameState.getPlayerSestertii(0) == 27);
        assert(gameState.getPlayerHand(0).size() == 5);
        assert(!gameState.getPlayerHand(0).contains(Card.ARCHITECTUS));

        field = gameState.getPlayerCardsOnDiscs(0);
        assert(field[0] == Card.ARCHITECTUS);

        //if you lay a building card now, it should still cost you money
        move.placeCard(Card.FORUM, Rules.DICE_DISC_3);
        assert(gameState.getPlayerSestertii(0) == 22);
        assert(gameState.getPlayerHand(0).size() == 4);
        assert(!gameState.getPlayerHand(0).contains(Card.FORUM));

        field = gameState.getPlayerCardsOnDiscs(0);
        assert(field[2] == Card.FORUM);

        //activate Architectus
        ArchitectusActivator activator = (ArchitectusActivator) move.chooseCardToActivate(Rules.DICE_DISC_1);
        activator.layCard(Card.MACHINA, 5);
        // activator.layCard(Card.LEGAT, 7);
        activator.layCard(Card.ONAGER, 3);
        activator.complete();

        //cards shouldn't have cost money to lay
        assert(gameState.getPlayerSestertii(0) == 22);

        field = gameState.getPlayerCardsOnDiscs(0);
        assert(field[0] == Card.ARCHITECTUS);
        assert(field[2] == Card.ONAGER);
        assert(field[4] == Card.MACHINA);
        assert(field[6] == Card.NOT_A_CARD);

        hand = gameState.getPlayerHand(0);
        assert(!hand.contains(Card.ONAGER));
        assert(!hand.contains(Card.MACHINA));
        assert(hand.contains(Card.LEGAT));

        discard = gameState.getDiscard();
        assert(discard.contains(Card.FORUM));

        //building cards should cost money to lay again
        move.placeCard(Card.TURRIS, 6);

        assert(gameState.getPlayerSestertii(0) == 22 - 6);

        field = gameState.getPlayerCardsOnDiscs(0);
        assert(field[5] == Card.TURRIS);

        hand = gameState.getPlayerHand(0);
        assert(!hand.contains(Card.TURRIS));

    }
}
