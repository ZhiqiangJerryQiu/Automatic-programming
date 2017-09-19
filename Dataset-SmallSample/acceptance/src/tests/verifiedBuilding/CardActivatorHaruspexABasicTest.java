package tests.verifiedBuilding;

import java.util.ArrayList;
import java.util.List;

import framework.Rules;
import framework.Test;
import framework.cards.Card;
import framework.interfaces.GameState;
import framework.interfaces.MoveMaker;
import framework.interfaces.activators.HaruspexActivator;

/**
 *
 * Test the functionality of Haruspex
 *
 * @author Karla Burnett
 *
 */

public class CardActivatorHaruspexABasicTest extends Test {

    @Override
    public String getShortDescription() {
        return "Test the basic functionality of Haruspex";
    }

    @Override
    public void run(GameState gameState, MoveMaker move) throws AssertionError,
            UnsupportedOperationException, IllegalArgumentException {

        gameState.setWhoseTurn(0);

        // Initialise both players' hands to being empty
        gameState.setPlayerHand(0, new ArrayList<Card>());
        gameState.setPlayerHand(1, new ArrayList<Card>());

        // Initialise the board with just one Haruspex laid for the first player
        // and nothing laid for the second
        Card [] board = new Card[Rules.NUM_DICE_DISCS];
        for (int i = 0; i < Rules.NUM_DICE_DISCS; i++) {
           board[i] = Card.NOT_A_CARD;
        }

        gameState.setPlayerCardsOnDiscs(1, board);
        board[3] = Card.HARUSPEX;
        gameState.setPlayerCardsOnDiscs(0, board);

        // Initialise the deck
        List<Card> deck = new ArrayList<Card>();
        deck.add(Card.NERO);
        deck.add(Card.SENATOR);
        deck.add(Card.VELITES);
        deck.add(Card.ESSEDUM);
        deck.add(Card.HARUSPEX);
        deck.add(Card.PRAETORIANUS);
        deck.add(Card.SENATOR);
        deck.add(Card.VELITES);
        deck.add(Card.ESSEDUM);
        deck.add(Card.PRAETORIANUS);
        deck.add(Card.SENATOR);
        deck.add(Card.VELITES);
        deck.add(Card.ESSEDUM);
        deck.add(Card.PRAETORIANUS);
        deck.add(Card.ONAGER);
        gameState.setDeck(deck);

        // Initialise the discard
        List<Card> discard = new ArrayList<Card>();
        discard.add(Card.TURRIS);
        discard.add(Card.SCAENICUS);
        discard.add(Card.SICARIUS);
        discard.add(Card.LEGAT);
        discard.add(Card.CONSUL);
        discard.add(Card.LEGIONARIUS);
        discard.add(Card.CONSILIARIUS);
        gameState.setDiscard(discard);

        // Initialise the dice
        gameState.setActionDice(new int[] {4, 4, 4});

        // ---- CHOOSING THE FIRST CARD IN THE DECK ----

        // Activate the Haruspex, choosing the first card in the deck
        HaruspexActivator activator = (HaruspexActivator) move.chooseCardToActivate(Rules.DICE_DISC_4);
        activator.chooseCardFromPile(getIndexFromPile(Card.NERO, gameState.getDeck()));
        activator.complete();

        // Check that hand, deck, discard, etc. are as expected
        assert(gameState.getPlayerHand(0).size() == 1);
        assert( gameState.getPlayerHand(0).contains(Card.NERO));
        assert(!gameState.getPlayerHand(1).contains(Card.NERO));
        assert(!gameState.getDeck().contains(Card.NERO));
        assert(!gameState.getDiscard().contains(Card.NERO));

        // Approximate check that the deck contains the cards it should.
        // Doesn't check the count, only that there is at least one of each
        // type of card there should be, and no NERO, since we just removed
        // the only one.
        deck.remove(0);
        assert(!gameState.getDeck().contains(Card.NERO));
        assert( gameState.getDeck().containsAll(deck));
        

        // ---- CHOOSING THE LAST CARD IN THE DECK ----
        // Activate the Haruspex, choosing the last card in the deck
        activator = (HaruspexActivator) move.chooseCardToActivate(Rules.DICE_DISC_4);
        activator.chooseCardFromPile(getIndexFromPile(Card.ONAGER, gameState.getDeck()));
        activator.complete();

        // Check that hand, deck, discard, etc. are as expected
        assert(gameState.getPlayerHand(0).size() == 2);
        assert( gameState.getPlayerHand(0).contains(Card.ONAGER));
        assert(!gameState.getPlayerHand(1).contains(Card.ONAGER));
        assert(!gameState.getDeck().contains(Card.ONAGER));
        assert(!gameState.getDiscard().contains(Card.ONAGER));

        // ---- CHOOSING ANOTHER HARUSPEX ----
        // Activate the Haruspex, choosing the first card in the deck
        activator = (HaruspexActivator) move.chooseCardToActivate(Rules.DICE_DISC_4);
        activator.chooseCardFromPile(getIndexFromPile(Card.HARUSPEX, gameState.getDeck()));
        activator.complete();

        // Check that hand, deck, discard, etc. are as expected
        assert(gameState.getPlayerHand(0).size() == 3);
        assert( gameState.getPlayerHand(0).contains(Card.HARUSPEX));
        assert(!gameState.getPlayerHand(1).contains(Card.HARUSPEX));
        assert(!gameState.getDeck().contains(Card.HARUSPEX));
        assert(!gameState.getDiscard().contains(Card.HARUSPEX));

        // Check that board state is right
        board = gameState.getPlayerCardsOnDiscs(0);
        for (int i = 0; i < board.length; i++) {
           if (i == 3) {
              assert(board[i] == Card.HARUSPEX);
           } else {
              assert(board[i] == Card.NOT_A_CARD);
           }
        }

        board = gameState.getPlayerCardsOnDiscs(1);
        for (int i = 0; i < board.length; i++) {
           assert(board[i] == Card.NOT_A_CARD);
        }
    }
}
