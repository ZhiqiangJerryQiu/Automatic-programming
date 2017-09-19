package tests.verifiedBuilding;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import framework.Test;
import framework.Rules;
import framework.cards.Card;
import framework.interfaces.GameState;
import framework.interfaces.MoveMaker;
import framework.interfaces.activators.AesculapinumActivator;

/**
 *
 * Test the the card Aesculapinum
 *
 * @author Junjie CHEN
 *
 */

public class CardActivatorAesculapinumBasicTest extends Test {

    @Override
    public String getShortDescription() {
        return "Test the the card Aesculapinum";
    }

    @Override
    public void run(GameState gameState, MoveMaker move) throws AssertionError,
            UnsupportedOperationException, IllegalArgumentException {
      if (1==1) {
         throw new IllegalArgumentException();
      }        

        out.println("Testing Aesculapinum");

        //only has one card in hand
        Collection<Card> hand = new ArrayList<Card>();
        hand.add(Card.AESCULAPINUM);
        gameState.setPlayerHand(0, hand);

        //two character cards and one building card in the discard pile
        List<Card> discard = new ArrayList<Card>();
        discard.add(Card.ARCHITECTUS);
        discard.add(Card.BASILICA);
        discard.add(Card.CONSUL);
        gameState.setDiscard(discard);

        //no other cards on the field
        Card[] field = new Card[Rules.NUM_DICE_DISCS];
        for(int i = 0; i < field.length; i++) {
            field[i] = Card.NOT_A_CARD;
        }
        gameState.setPlayerCardsOnDiscs(0, field);

        gameState.setWhoseTurn(0);
        gameState.setPlayerSestertii(0, 20);
        gameState.setPlayerHand(0, hand);

        gameState.setActionDice(new int[] {3,3,3});

        //================== test1 =====================

        move.placeCard(Card.AESCULAPINUM, Rules.DICE_DISC_3);

        //the correct amount of Sestertii should be deducted from the player
        assert(gameState.getPlayerSestertii(0) == 15);
        assert(gameState.getPlayerHand(0).size() == 0);

        field = gameState.getPlayerCardsOnDiscs(0);

        //the card get laid there
        assert(field[2] == Card.AESCULAPINUM);


        //=================== test2 =====================

        //activate the card
        AesculapinumActivator activator = (AesculapinumActivator) move.chooseCardToActivate(Rules.DICE_DISC_3);
        activator.chooseCardFromPile(getIndexFromPile(Card.ARCHITECTUS, gameState.getDiscard()));
        activator.complete();

        assert(gameState.getPlayerHand(0).size() == 1);
        assert(gameState.getPlayerHand(0).contains(Card.ARCHITECTUS));
        assert(gameState.getDiscard().size() == 2);
        assert(!gameState.getDiscard().contains(Card.ARCHITECTUS));
        assert(gameState.getDiscard().contains(Card.BASILICA));
        assert(gameState.getDiscard().contains(Card.CONSUL));


        //================== test3 ======================

        //activate the card
        activator = (AesculapinumActivator) move.chooseCardToActivate(Rules.DICE_DISC_3);
        activator.chooseCardFromPile(getIndexFromPile(Card.CONSUL, gameState.getDiscard()));
        activator.complete();

        assert(gameState.getPlayerHand(0).size() == 2);
        assert(gameState.getPlayerHand(0).contains(Card.ARCHITECTUS));
        assert(gameState.getPlayerHand(0).contains(Card.CONSUL));

        assert(gameState.getDiscard().size() == 1);
        assert(!gameState.getDiscard().contains(Card.ARCHITECTUS));
        assert(gameState.getDiscard().contains(Card.BASILICA));
        assert(!gameState.getDiscard().contains(Card.CONSUL));


        //================== test4 ======================

        //test another player
        move.endTurn();
        discard.clear();
        discard.add(Card.HARUSPEX);
        discard.add(Card.ESSEDUM);
        discard.add(Card.LEGAT);
        discard.add(Card.SICARIUS);

        gameState.setDiscard(discard);

        hand.clear();
        hand.add(Card.AESCULAPINUM);
        hand.add(Card.AESCULAPINUM);
        hand.add(Card.AESCULAPINUM);

        gameState.setPlayerHand(1, hand);

        //no other cards on the field
        for(int i = 0; i < field.length; i++) {
            field[i] = Card.NOT_A_CARD;
        }

        gameState.setPlayerCardsOnDiscs(1, field);
        gameState.setPlayerSestertii(1, 15);

        gameState.setActionDice(new int[] {4,5,6});

        //lay the cards
        move.placeCard(Card.AESCULAPINUM, Rules.DICE_DISC_5);
        move.placeCard(Card.AESCULAPINUM, Rules.DICE_DISC_6);

        //test if the money get removed from the player
        assert(gameState.getPlayerSestertii(1) == 5);
        assert(gameState.getPlayerHand(1).size() == 1);

        //test if the card has been placed on the field
        field = gameState.getPlayerCardsOnDiscs(1);

        assert(field[4] == Card.AESCULAPINUM);
        assert(field[5] == Card.AESCULAPINUM);

        move.placeCard(Card.AESCULAPINUM, Rules.DICE_DISC_4);

        assert(gameState.getPlayerSestertii(1) == 0);
        assert(gameState.getPlayerHand(1).size() == 0);

        field = gameState.getPlayerCardsOnDiscs(1);

        assert(field[3] == Card.AESCULAPINUM);

        //activate the card
        activator = (AesculapinumActivator) move.chooseCardToActivate(Rules.DICE_DISC_4);
        activator.chooseCardFromPile(getIndexFromPile(Card.ESSEDUM, gameState.getDiscard()));
        activator.complete();

        //the card get removed from the discard
        assert(gameState.getDiscard().size() == 3);
        assert(!gameState.getDiscard().contains(Card.ESSEDUM));
        assert(gameState.getPlayerHand(1).size() == 1);
        assert(gameState.getPlayerHand(1).contains(Card.ESSEDUM));

        discard.remove(Card.ESSEDUM);
        gameState.setDiscard(discard);

        activator = (AesculapinumActivator) move.chooseCardToActivate(Rules.DICE_DISC_5);
        activator.chooseCardFromPile(getIndexFromPile(Card.SICARIUS, gameState.getDiscard()));
        activator.complete();

        assert(gameState.getDiscard().size() == 2);
        assert(!gameState.getDiscard().contains(Card.SICARIUS));
        assert(gameState.getPlayerHand(1).size() == 2);
        assert(gameState.getPlayerHand(1).contains(Card.SICARIUS));

        discard.remove(Card.SICARIUS);
        gameState.setDiscard(discard);

        activator = (AesculapinumActivator) move.chooseCardToActivate(Rules.DICE_DISC_6);
        activator.chooseCardFromPile(getIndexFromPile(Card.LEGAT, gameState.getDiscard()));
        activator.complete();

        assert(gameState.getDiscard().size() == 1);
        assert(!gameState.getDiscard().contains(Card.LEGAT));
        assert(gameState.getPlayerHand(1).size() == 3);
        assert(gameState.getPlayerHand(1).contains(Card.LEGAT));
    }

}
