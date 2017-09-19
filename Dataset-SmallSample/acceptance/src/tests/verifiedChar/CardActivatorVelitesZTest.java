package tests.verifiedChar;
import java.util.*;

import framework.Test;
import framework.cards.Card;
import framework.interfaces.GameState;
import framework.interfaces.MoveMaker;
import framework.interfaces.activators.CenturioActivator;
import framework.interfaces.activators.VelitesActivator;


/**
 * 
 * Testing the use of Velites
 * 
 * @author Peter Xu
 *
 */


public class CardActivatorVelitesZTest extends Test {

	@Override
	public String getShortDescription() {
		return "Testing the use of Velites";
	}

	@Override
	public void run(GameState gameState, MoveMaker move) {

		//SETTING THE FIELD
		gameState.setPlayerCardsOnDiscs(0, new Card[] {
				
				Card.SCAENICUS,
				Card.NOT_A_CARD,
				Card.VELITES,
				Card.LEGIONARIUS,
				Card.NOT_A_CARD,
				Card.NOT_A_CARD,
				Card.HARUSPEX 
		
		});
		
		gameState.setPlayerCardsOnDiscs(1, new Card[] {
				
				Card.NOT_A_CARD,
				Card.CONSILIARIUS,
				Card.CENTURIO,
				Card.CONSUL,
				Card.GLADIATOR,
				Card.NOT_A_CARD,
				Card.NOT_A_CARD
		
		});
		
		List<Card> deck = new ArrayList<Card>();
		deck.add(Card.VELITES);
		gameState.setDeck(deck);
		gameState.setDiscard(new ArrayList<Card>());
				
		//CHECKING THE FIELD IS CORRECT
		assert(gameState.getPlayerCardsOnDiscs(0)[0] == Card.SCAENICUS);
		assert(gameState.getPlayerCardsOnDiscs(0)[2] == Card.VELITES);
		assert(gameState.getPlayerCardsOnDiscs(0)[3] == Card.LEGIONARIUS);
		assert(gameState.getPlayerCardsOnDiscs(0)[6] == Card.HARUSPEX);

		assert(gameState.getPlayerCardsOnDiscs(1)[1] == Card.CONSILIARIUS);
		assert(gameState.getPlayerCardsOnDiscs(1)[2] == Card.CENTURIO);
		assert(gameState.getPlayerCardsOnDiscs(1)[3] == Card.CONSUL);
		assert(gameState.getPlayerCardsOnDiscs(1)[4] == Card.GLADIATOR);
		
		gameState.setWhoseTurn(0);
		gameState.setActionDice(new int[] {1, 3, 4});
		
		//BEGINNING PLAYER 1'S TURN
		
		//TAKING THE VELITES FROM THE DECK
		move.activateCardsDisc(1, Card.VELITES);
		assert(gameState.getPlayerHand(0).contains(Card.VELITES));
		
		//TAKING MONEY
		move.activateMoneyDisc(4);
		
		//ATTACK CONSILIARIUS
		VelitesActivator va = (VelitesActivator)move.chooseCardToActivate(3);
		va.chooseDiceDisc(2);
		va.giveAttackDieRoll(3);
		va.complete();
		//SHOULD NOT KILL THE CONSILIARIUS
		assert(gameState.getPlayerCardsOnDiscs(1)[1] == Card.CONSILIARIUS);
		
		//END TURN
		move.endTurn();
		
		assert(gameState.getWhoseTurn() == 1);
		
		//BEGINNING PLAYER 2'S TURN
		gameState.setActionDice(new int[] {3, 3, 3});
		
		//CHECKING THE DEFENSE OF VELITES IS CORRECT
		
		//OPPONENT ATTACKS THE VELITES
		CenturioActivator ca = (CenturioActivator)move.chooseCardToActivate(3);
		ca.giveAttackDieRoll(2);
		ca.chooseCenturioAddActionDie(false);
		ca.complete();
		//SHOULD NOT KILL THE VELITES
		assert(gameState.getPlayerCardsOnDiscs(0)[2] == Card.VELITES);
		
		//OPPONENT ATTACKS THE VELITES AGAIN
		ca = (CenturioActivator)move.chooseCardToActivate(3);
		ca.giveAttackDieRoll(3);
		ca.chooseCenturioAddActionDie(false);
		ca.complete();
		//SHOULD BE ENOUGH TO KILL THE VELITES
		assert(gameState.getPlayerCardsOnDiscs(0)[2] == Card.NOT_A_CARD);
		
		move.endTurn();
		
		assert(gameState.getWhoseTurn() == 0);
		
		//BEGINNING PLAYER 1'S TURN
		gameState.setActionDice(new int[] {1, 4, 4});
		
		//TAKING MONEY
		move.activateMoneyDisc(1);
		
		//SHOULD BE ABLE TO PLAY THE VELITES NOW
		move.placeCard(Card.VELITES, 4);
		//SHOULD HAVE REPLACED LEGIONARIUS
		assert(gameState.getPlayerCardsOnDiscs(0)[3] == Card.VELITES);
		
		//ATTACK CONSILIARIUS
		VelitesActivator va2 = (VelitesActivator)move.chooseCardToActivate(4);
		va2.chooseDiceDisc(2);
		va2.giveAttackDieRoll(4);
		va2.complete();
		//SHOULD BE ENOUGH TO KILL CONSILIARIUS
		assert(gameState.getPlayerCardsOnDiscs(1)[1] == Card.NOT_A_CARD);
		
		//ATTACK CENTURIO
		va2 = (VelitesActivator)move.chooseCardToActivate(4);
		va2.chooseDiceDisc(3);
		va2.giveAttackDieRoll(5);
		va2.complete();
		//SHOULD BE ENOUGH TO KILL CENTURIO
		assert(gameState.getPlayerCardsOnDiscs(1)[2] == Card.NOT_A_CARD);
		
		move.endTurn();
		
		assert(gameState.getWhoseTurn() == 1);
		
		move.endTurn();
		
		assert(gameState.getWhoseTurn() == 0);
		
		gameState.setActionDice(new int[] {4,4,4});
		
		//ATTACK CONSUL
		va2 = (VelitesActivator)move.chooseCardToActivate(4);
		va2.chooseDiceDisc(4);
		va2.giveAttackDieRoll(4);
		va2.complete();
		//MORE THAN ENOUGH TO KILL CONSUL
		assert(gameState.getPlayerCardsOnDiscs(1)[3] == Card.NOT_A_CARD);
		
		//ATTACK GLADIATOR
		va2 = (VelitesActivator)move.chooseCardToActivate(4);
		va2.chooseDiceDisc(5);
		va2.giveAttackDieRoll(4);
		va2.complete();
		//NOT ENOUGH TO KILL GLADIATOR
		assert(gameState.getPlayerCardsOnDiscs(1)[4] == Card.GLADIATOR);
		
		//NEVER GIVE UP
		va2 = (VelitesActivator)move.chooseCardToActivate(4);
		va2.chooseDiceDisc(5);
		va2.giveAttackDieRoll(6);
		va2.complete();
		//MORE THAN ENOUGH TO KILL GLADIATOR
		assert(gameState.getPlayerCardsOnDiscs(1)[4] == Card.NOT_A_CARD);
		
	}

}
