package tests.verifiedSpec2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import framework.Test;
import framework.cards.Card;
import framework.interfaces.GameState;
import framework.interfaces.MoveMaker;
import framework.interfaces.activators.NeroActivator;
import framework.interfaces.activators.SicariusActivator;

/**
*
* Test grim reaper protects from assassinations
*
* @author Lauren Spooner
*
*/

public class CardActivatorGrimReaperAssassinationTest extends Test{

	@Override
	public String getShortDescription() {
		return "Testing Grim Reaper protects from Assassination";
	}

	@Override
	public void run(GameState gameState, MoveMaker move) throws AssertionError,
			UnsupportedOperationException, IllegalArgumentException {
		
		List<Card> discard = new ArrayList<Card>();
		gameState.setDiscard(discard);
		
		Collection<Card> hand = new ArrayList<Card>();
		gameState.setPlayerHand(0, hand);
		gameState.setPlayerHand(1, hand);
		
		Card[] board0 = new Card[]{
				Card.SICARIUS,
				Card.NOT_A_CARD,
				Card.NOT_A_CARD,
				Card.NOT_A_CARD,
				Card.NOT_A_CARD,
				Card.NERO,
				Card.NOT_A_CARD
		};
		
		Card[] board1 = new Card[]{
				Card.GRIMREAPER,
				Card.NOT_A_CARD,
				Card.NOT_A_CARD,
				Card.NOT_A_CARD,
				Card.NOT_A_CARD,
				Card.GLADIATOR,
				Card.AESCULAPINUM
		};
		
		gameState.setPlayerCardsOnDiscs(0, board0);
		gameState.setPlayerCardsOnDiscs(1, board1);
		
		gameState.setPlayerVictoryPoints(0, 10);
		gameState.setPlayerVictoryPoints(1, 10);
		
		gameState.setWhoseTurn(0);
		gameState.setActionDice(new int[]{1, 6, 3});
		
		//attack gladiator with sicarius
		SicariusActivator s = (SicariusActivator)move.chooseCardToActivate(1);
		s.chooseDiceDisc(6);
		s.complete();
		
		board0 = gameState.getPlayerCardsOnDiscs(0);
		assert(board0[0] == Card.NOT_A_CARD);
		
		board1 = gameState.getPlayerCardsOnDiscs(1);
		System.out.println(board1[6]);
		assert(board1[5] == Card.NOT_A_CARD);
		
		hand = gameState.getPlayerHand(1);
		assert(hand.contains(Card.GLADIATOR));
		
		discard = gameState.getDiscard();
		assert(!discard.contains(Card.GLADIATOR));
		
		//attack aesculapinum with nero
		NeroActivator n = (NeroActivator)move.chooseCardToActivate(6);
		n.chooseDiceDisc(7);
		n.complete();
		
		board0 = gameState.getPlayerCardsOnDiscs(0);
		assert(board0[5] == Card.NOT_A_CARD);
		
		board1 = gameState.getPlayerCardsOnDiscs(1);
		assert(board1[6] == Card.NOT_A_CARD);
		
		hand = gameState.getPlayerHand(1);
		assert(!hand.contains(Card.AESCULAPINUM));
		
		discard = gameState.getDiscard();
		assert(discard.contains(Card.AESCULAPINUM));
		
	}

}
