package tests.verifiedBuilding;

import java.util.ArrayList;
import java.util.List;

import framework.Test;
import framework.cards.Card;
import framework.interfaces.GameState;
import framework.interfaces.MoveMaker;
import framework.interfaces.activators.CenturioActivator;
import framework.interfaces.activators.ForumActivator;


/**
*
* Test the use of Templum
*
* @author Peter Xu
*
*/


public class CardActivatorTemplumZTest extends Test {

	@Override
	public String getShortDescription() {
		return "Testing the use of Templum";
	}

	@Override
	public void run(GameState gameState, MoveMaker move) {

		//SETTING THE FIELD
		gameState.setPlayerCardsOnDiscs(0, new Card[] {

				Card.SCAENICUS,
				Card.NOT_A_CARD,
				Card.PRAETORIANUS,
				Card.FORUM,
				Card.TEMPLUM,
				Card.NOT_A_CARD,
				Card.HARUSPEX 

		});

		gameState.setPlayerCardsOnDiscs(1, new Card[] {

				Card.NOT_A_CARD,
				Card.GLADIATOR,
				Card.CENTURIO,
				Card.CENTURIO,
				Card.CENTURIO,
				Card.NOT_A_CARD,
				Card.NOT_A_CARD

		});

		List<Card> hand = new ArrayList<Card>();
		hand.add(Card.TEMPLUM);
		gameState.setPlayerHand(0, hand);
		gameState.setPlayerSestertii(0, 2);

		//CHECKING THE FIELD IS CORRECT
		assert(gameState.getPlayerCardsOnDiscs(0)[0] == Card.SCAENICUS);
		assert(gameState.getPlayerCardsOnDiscs(0)[2] == Card.PRAETORIANUS);
		assert(gameState.getPlayerCardsOnDiscs(0)[3] == Card.FORUM);
		assert(gameState.getPlayerCardsOnDiscs(0)[4] == Card.TEMPLUM);
		assert(gameState.getPlayerCardsOnDiscs(0)[6] == Card.HARUSPEX);

		assert(gameState.getPlayerCardsOnDiscs(1)[1] == Card.GLADIATOR);
		assert(gameState.getPlayerCardsOnDiscs(1)[2] == Card.CENTURIO);
		assert(gameState.getPlayerCardsOnDiscs(1)[3] == Card.CENTURIO);
		assert(gameState.getPlayerCardsOnDiscs(1)[4] == Card.CENTURIO);

		gameState.setWhoseTurn(0);
		gameState.setActionDice(new int[] {4, 1, 3});

		//BEGINNING PLAYER 1'S TURN

		//ACTIVATING FORUM WITH TEMPLUM ON ITS RIGHT
		int beforeActivation = gameState.getPlayerVictoryPoints(0);
		ForumActivator fa = (ForumActivator)move.chooseCardToActivate(4);
		fa.chooseActivateTemplum(true);
		fa.chooseActivateTemplum(1);
		fa.chooseActionDice(3);
		fa.complete();
		//SHOULD GAIN 4 VP
		assert(gameState.getPlayerVictoryPoints(0) == (beforeActivation + 1 + 3));

		move.endTurn();

		assert(gameState.getWhoseTurn() == 1);

		gameState.setActionDice(new int[] {2, 3, 5});

		//BEGINNING PLAYER 2'S TURN

		//ATTACK THE TEMPLUM
		CenturioActivator ca = (CenturioActivator)move.chooseCardToActivate(5);
		ca.giveAttackDieRoll(1);
		ca.chooseCenturioAddActionDie(false);
		ca.complete();
		//SHOULD NOT KILL THE TEMPLUM
		assert(gameState.getPlayerCardsOnDiscs(0)[4] == Card.TEMPLUM);

		move.endTurn();

		assert(gameState.getWhoseTurn() == 0);

		//BEGINNING PLAYER 1'S TURN

		gameState.setActionDice(new int[] {1,2,4});
		
		//PLAY THE TEMPLUM
		move.placeCard(Card.TEMPLUM, 3);
		//SHOULD BE DOWN
		assert(gameState.getPlayerCardsOnDiscs(0)[2] == Card.TEMPLUM);
		assert(gameState.getPlayerSestertii(0) == 0);


		//ACTIVATING FORUM WITH TEMPLUM TO BOTH SIDES
		beforeActivation = gameState.getPlayerVictoryPoints(0);
		fa = (ForumActivator)move.chooseCardToActivate(4);
		fa.chooseActivateTemplum(true);
		fa.chooseActivateTemplum(1);
		fa.chooseActionDice(2);
		fa.complete();
		//SHOULD GAIN 3 VP
		assert(gameState.getPlayerVictoryPoints(0) == (beforeActivation + 1 + 2));


		move.endTurn();

		assert(gameState.getWhoseTurn() == 1);

		//BEGINNING PLAYER 2'S TURN

		gameState.setActionDice(new int[] {2,3,5});

		//ATTACK THE TEMPLUM
		ca = (CenturioActivator)move.chooseCardToActivate(5);
		ca.giveAttackDieRoll(1);
		ca.chooseCenturioAddActionDie(false);
		ca.complete();
		//SHOULD NOT KILL THE TEMPLUM
		assert(gameState.getPlayerCardsOnDiscs(0)[4] == Card.TEMPLUM);

		move.endTurn();

		assert(gameState.getWhoseTurn() == 0);

		//BEGINNING PLAYER 1'S TURN

		gameState.setActionDice(new int[] {1,2,4});

		//ACTIVATING FORUM WITH TEMPLUM ON BOTH SIDES
		beforeActivation = gameState.getPlayerVictoryPoints(0);
		fa = (ForumActivator)move.chooseCardToActivate(4);
		fa.chooseActivateTemplum(true);
		fa.chooseActivateTemplum(1);
		fa.chooseActionDice(2);
		fa.complete();
		//SHOULD GAIN 3 VP
		assert(gameState.getPlayerVictoryPoints(0) == (beforeActivation + 1 + 2));

		move.endTurn();

		assert(gameState.getWhoseTurn() == 1);

		//BEGINNING PLAYER 2'S TURN

		gameState.setActionDice(new int[] {2,3,5});

		//ATTACK THE TEMPLUM
		ca = (CenturioActivator)move.chooseCardToActivate(5);
		ca.giveAttackDieRoll(2);
		ca.chooseCenturioAddActionDie(false);
		ca.complete();
		//SHOULD KILL THE TEMPLUM
		assert(gameState.getPlayerCardsOnDiscs(0)[4] == Card.NOT_A_CARD);

		move.endTurn();

		assert(gameState.getWhoseTurn() == 0);

		//BEGINNING PLAYER 1'S TURN

		gameState.setActionDice(new int[] {1,2,4});

		//ACTIVATING FORUM WITH TEMPLUM TO ITS LEFT
		beforeActivation = gameState.getPlayerVictoryPoints(0);
		fa = (ForumActivator)move.chooseCardToActivate(4);
		fa.chooseActivateTemplum(true);
		fa.chooseActivateTemplum(1);
		fa.chooseActionDice(2);
		fa.complete();
		//SHOULD GAIN 3 VP
		assert(gameState.getPlayerVictoryPoints(0) == (beforeActivation + 1 + 2));

	}

}
