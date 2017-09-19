package tests.verifiedSpec2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import framework.Rules;
import framework.Test;
import framework.cards.Card;
import framework.interfaces.GameState;
import framework.interfaces.MoveMaker;
import framework.interfaces.activators.ConsiliariusActivator;
import framework.interfaces.activators.TelephoneBoxActivator;

/**
*
* @author Robert Cen
* 
*/

public class TelephoneConsililiariusParadoxTest extends Test {

	@Override
	public String getShortDescription() {
		// TODO Auto-generated method stub
		return "Testing simple time paradox with consiliarius";
	}

	@Override
	public void run(GameState gameState, MoveMaker move) throws AssertionError,
			UnsupportedOperationException, IllegalArgumentException {

		gameState.setWhoseTurn(0);
		gameState.setPlayerSestertii(0, 15);
		gameState.setPlayerSestertii(1, 10);
		gameState.setPlayerVictoryPoints(0, 10);
		gameState.setPlayerVictoryPoints(1, 10);
		gameState.setActionDice(new int[] {1,2,3});
		List<Card> discard = new LinkedList<Card>();
		discard.add(Card.CENTURIO);
		discard.add(Card.BASILICA);
		gameState.setDiscard(discard);
		
		Collection<Card> player0Hand = new ArrayList<Card>();
		player0Hand.add(Card.TELEPHONEBOX);
		player0Hand.add(Card.GLADIATOR);
		gameState.setPlayerHand(0, player0Hand);
		
		Card[] player0Field = {
				Card.CONSILIARIUS,
				Card.NOT_A_CARD,
				Card.ARCHITECTUS,
				Card.HARUSPEX,
				Card.FORUM,
				Card.NOT_A_CARD,
				Card.NOT_A_CARD
		};
		
		gameState.setPlayerCardsOnDiscs(0, player0Field);
		
		Card[] player1Field = {
				Card.NOT_A_CARD,
				Card.FORUM,
				Card.TELEPHONEBOX,
				Card.ESSEDUM,
				Card.TURRIS,
				Card.NOT_A_CARD,
				Card.NOT_A_CARD,
		};
		
		gameState.setPlayerCardsOnDiscs(1, player1Field);
		
		//Activate a consiliarius and rearrange all character cards
		ConsiliariusActivator activator = (ConsiliariusActivator)move.chooseCardToActivate(1);
		activator.placeCard(Card.CONSILIARIUS, 1);
		activator.placeCard(Card.ARCHITECTUS, 2);
		activator.placeCard(Card.HARUSPEX, 3);
		activator.complete();
		
		player0Field[1] = Card.ARCHITECTUS;
		player0Field[2] = Card.HARUSPEX;
		player0Field[3] = Card.NOT_A_CARD;
		player0Field[4] = Card.FORUM;
		player0Field[5] = Card.NOT_A_CARD;
		player0Field[6] = Card.NOT_A_CARD;
		
		//Test that cards have been rearranged
		for (int i = 0; i < Rules.NUM_DICE_DISCS; i++) {
			assert (gameState.getPlayerCardsOnDiscs(0)[i] == player0Field[i]);
		}
		
		//Test money has not changed
		assert (gameState.getPlayerSestertii(0) == 15);
		
		move.endTurn();
		gameState.setActionDice(new int[] {4,6,2});
		move.endTurn();
		gameState.setActionDice(new int[] {1,1,2});
		
		move.placeCard(Card.TELEPHONEBOX, 7);
		move.placeCard(Card.GLADIATOR, 6);
		
		//Teleport the gladiator back 2 turns 
		TelephoneBoxActivator tBoxActivator = (TelephoneBoxActivator)move.activateBribeDisc(1);
		tBoxActivator.setSecondDiceUsed(2);
		tBoxActivator.chooseDiceDisc(6);
		tBoxActivator.shouldMoveForwardInTime(false);
		tBoxActivator.complete();
		
		//Should cause a time paradox because the set of cards that was activated with
		//consiliarius is now different as there is an extra card (gladiator).
		assert (gameState.isGameCompleted());

	}

}
