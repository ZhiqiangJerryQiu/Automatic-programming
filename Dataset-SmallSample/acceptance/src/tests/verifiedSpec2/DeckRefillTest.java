package tests.verifiedSpec2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import framework.Test;
import framework.cards.Card;
import framework.interfaces.GameState;
import framework.interfaces.MoveMaker;

/**
*
* Testing the deck gets refilled when you try to draw cards and it's empty
*
* @author Lauren Spooner
* @author Shannon Green
*
*/

public class DeckRefillTest extends Test{

	@Override
	public String getShortDescription() {
		return "Test that a deck will refill itself once it has been emptied";
	}

	@Override
	public void run(GameState gameState, MoveMaker move) throws AssertionError,
			UnsupportedOperationException, IllegalArgumentException {
		
		gameState.setPlayerVictoryPoints(0, 15);
		gameState.setPlayerVictoryPoints(1, 15);
		
		gameState.setPlayerSestertii(0, 100);
		gameState.setPlayerSestertii(1, 100);
		
		Card[] board = new Card[]{Card.NOT_A_CARD, Card.NOT_A_CARD, Card.NOT_A_CARD, Card.NOT_A_CARD, Card.NOT_A_CARD, Card.NOT_A_CARD, Card.NOT_A_CARD};
		gameState.setPlayerCardsOnDiscs(0, board);
		gameState.setPlayerCardsOnDiscs(1, board);
		
		List<Card> deck = new ArrayList<Card>();
		gameState.setDiscard(deck);
		gameState.setPlayerHand(0, deck);
		gameState.setPlayerHand(1, deck);
		
		deck.add(Card.AESCULAPINUM);
		deck.add(Card.ARCHITECTUS);
		deck.add(Card.BASILICA);
		deck.add(Card.CENTURIO);
		deck.add(Card.CONSILIARIUS);
		deck.add(Card.CONSUL);
		deck.add(Card.ESSEDUM);
		deck.add(Card.FORUM);
		deck.add(Card.GLADIATOR);
		deck.add(Card.GRIMREAPER);
		deck.add(Card.HARUSPEX);
		gameState.setDeck(deck);
		
		gameState.setWhoseTurn(0);
		
		gameState.setActionDice(new int[]{1, 2, 3});
		
		move.activateCardsDisc(1, Card.AESCULAPINUM);
		Collection<Card> hand = gameState.getPlayerHand(0);
		assert(hand.size() == 1);
		assert(hand.contains(Card.AESCULAPINUM));
		List<Card> discard = gameState.getDiscard();
		assert(discard.size() == 0);
		deck = gameState.getDeck();
		assert(deck.size() == 10);
		assert(!deck.contains(Card.AESCULAPINUM));
		
		move.activateCardsDisc(3, Card.BASILICA);
		hand = gameState.getPlayerHand(0);
		assert(hand.size() == 2);
		assert(hand.contains(Card.BASILICA));
		discard = gameState.getDiscard();
		assert(discard.size() == 2);
		assert(discard.contains(Card.ARCHITECTUS));
		assert(discard.contains(Card.CENTURIO));
		deck = gameState.getDeck();
		assert(deck.size() == 7);
		assert(!deck.contains(Card.ARCHITECTUS));
		assert(!deck.contains(Card.BASILICA));
		assert(!deck.contains(Card.CENTURIO));
		
		move.activateCardsDisc(2, Card.CONSUL);
		hand = gameState.getPlayerHand(0);
		assert(hand.size() == 3);
		assert(hand.contains(Card.CONSUL));
		discard = gameState.getDiscard();
		assert(discard.size() == 3);
		assert(discard.contains(Card.CONSILIARIUS));
		deck = gameState.getDeck();
		assert(deck.size() == 5);
		assert(!deck.contains(Card.CONSILIARIUS));
		assert(!deck.contains(Card.CONSUL));
		
		move.placeCard(Card.AESCULAPINUM, 4);
		assert(gameState.getPlayerCardsOnDiscs(0)[3] == Card.AESCULAPINUM);
		
		move.endTurn();
		
		gameState.setActionDice(new int[]{4, 2, 1});
		
		move.activateCardsDisc(4, Card.GLADIATOR);
		hand = gameState.getPlayerHand(1);
		assert(hand.size() == 1);
		assert(hand.contains(Card.GLADIATOR));
		discard = gameState.getDiscard();
		assert(discard.size() == 6);
		assert(discard.contains(Card.ESSEDUM));
		assert(discard.contains(Card.FORUM));
		assert(discard.contains(Card.GRIMREAPER));
		deck = gameState.getDeck();
		assert(deck.size() == 1);
		assert(!deck.contains(Card.ESSEDUM));
		assert(!deck.contains(Card.FORUM));
		assert(!deck.contains(Card.GLADIATOR));
		assert(!deck.contains(Card.GRIMREAPER));
		
		//now taking more cards than is left in the deck
		move.activateCardsDisc(2, Card.HARUSPEX);
		hand = gameState.getPlayerHand(1);
		assert(hand.size() == 2);
		assert(hand.contains(Card.HARUSPEX));
		discard = gameState.getDiscard();
		assert(discard.size() == 1);
		assert(!discard.contains(Card.AESCULAPINUM));
		assert(!discard.contains(Card.BASILICA));
		assert(!discard.contains(Card.CONSUL));
		assert(!discard.contains(Card.GLADIATOR));
		assert(!discard.contains(Card.HARUSPEX));
		deck = gameState.getDeck();
		assert(deck.size() == 5);
		assert(!deck.contains(Card.AESCULAPINUM));
		assert(!deck.contains(Card.BASILICA));
		assert(!deck.contains(Card.CONSUL));
		assert(!deck.contains(Card.GLADIATOR));
		assert(!deck.contains(Card.HARUSPEX));
		
		
		
	}

}
