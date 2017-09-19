package tests.verifiedChar;

import java.util.ArrayList;
import java.util.List;

import framework.Test;
import framework.cards.Card;
import framework.interfaces.GameState;
import framework.interfaces.MoveMaker;
import framework.interfaces.activators.CenturioActivator;
import framework.interfaces.activators.GladiatorActivator;
import framework.interfaces.activators.LegatActivator;
import framework.interfaces.activators.PraetorianusActivator;


/**
*
* Test the use of Praetorianus
*
* @author Peter Xu
*
*/


public class CardActivatorPraetorianusZTest extends Test {

	@Override
	public String getShortDescription() {
		return "Testing the use of Praetorianus";
	}

	@Override
	public void run(GameState gameState, MoveMaker move) {

		//SETTING THE FIELD
		gameState.setPlayerCardsOnDiscs(0, new Card[] {

				Card.SCAENICUS,
				Card.NOT_A_CARD,
				Card.PRAETORIANUS,
				Card.LEGIONARIUS,
				Card.NOT_A_CARD,
				Card.NOT_A_CARD,
				Card.HARUSPEX

		});

		gameState.setPlayerCardsOnDiscs(1, new Card[] {

				Card.NOT_A_CARD,
				Card.LEGAT,
				Card.CENTURIO,
				Card.CONSUL,
				Card.GLADIATOR,
				Card.NOT_A_CARD,
				Card.NOT_A_CARD

		});

		List<Card> deck = new ArrayList<Card>();
		deck.add(Card.PRAETORIANUS);
		deck.add(Card.LEGAT);
		gameState.setDeck(deck);
		gameState.setDiscard(new ArrayList<Card>());

		gameState.setPlayerSestertii(0, 0);
		gameState.setPlayerSestertii(1, 5);
		gameState.setPlayerVictoryPoints(0, 10);
		gameState.setPlayerVictoryPoints(1, 10);

		//CHECKING THE FIELD IS CORRECT
		assert(gameState.getPlayerCardsOnDiscs(0)[0] == Card.SCAENICUS);
		assert(gameState.getPlayerCardsOnDiscs(0)[2] == Card.PRAETORIANUS);
		assert(gameState.getPlayerCardsOnDiscs(0)[3] == Card.LEGIONARIUS);
		assert(gameState.getPlayerCardsOnDiscs(0)[6] == Card.HARUSPEX);

		assert(gameState.getPlayerCardsOnDiscs(1)[1] == Card.LEGAT);
		assert(gameState.getPlayerCardsOnDiscs(1)[2] == Card.CENTURIO);
		assert(gameState.getPlayerCardsOnDiscs(1)[3] == Card.CONSUL);
		assert(gameState.getPlayerCardsOnDiscs(1)[4] == Card.GLADIATOR);

		gameState.setWhoseTurn(0);
		gameState.setActionDice(new int[] {1, 3, 3});

		//BEGINNING PLAYER 1'S TURN

		//TAKING THE PRAETOR FROM THE DECK
		move.activateCardsDisc(1, Card.PRAETORIANUS);
		assert(gameState.getPlayerHand(0).contains(Card.PRAETORIANUS));

		//TAKING MONEY
		move.activateMoneyDisc(3);

		//BLOCKING DICE DISC 1
		PraetorianusActivator pa = (PraetorianusActivator)move.chooseCardToActivate(3);
		pa.chooseDiceDisc(1);
		pa.complete();

		move.endTurn();

		//BEGINNING PLAYER 2'S TURN

		gameState.setActionDice(new int[] {1 , 1, 3});

		//TAKING THE TEMPLUM FROM THE DECK
		move.activateCardsDisc(1, Card.LEGAT);
		assert(gameState.getPlayerHand(1).contains(Card.LEGAT));


		//SHOULD NOW BE ABLE TO PLAY THE LEGAT
		move.placeCard(Card.LEGAT, 1);
		//SHOULD BE DOWN
		assert(gameState.getPlayerCardsOnDiscs(1)[0] == Card.LEGAT);

		//TRY TO GAIN VP VIA LEGAT
		int initialVP = gameState.getPlayerVictoryPoints(1);
		LegatActivator la = (LegatActivator)move.chooseCardToActivate(1);
		la.complete();
		//SHOULD NOT GAIN ANY VP BECAUSE ITS BLOCKED
		assert(gameState.getPlayerVictoryPoints(1) == initialVP);

		//ATTACK THE PRAETOR
		CenturioActivator ca = (CenturioActivator)move.chooseCardToActivate(3);
		ca.giveAttackDieRoll(3);
		ca.chooseCenturioAddActionDie(false);
		ca.complete();
		//SHOULD NOT KILL THE PRAETOR
		assert(gameState.getPlayerCardsOnDiscs(0)[2] == Card.PRAETORIANUS);

		move.endTurn();

		//BEGINNING PLAYER 1'S TURN
		gameState.setActionDice(new int[] {1, 3, 4});

		//TAKING MONEY
		move.activateMoneyDisc(1);

		//SHOULD NOW HAVE ENOUGH TO PLAY OVER LEGIONARIUS
		move.placeCard(Card.PRAETORIANUS, 4);
		assert(gameState.getPlayerCardsOnDiscs(0)[3] == Card.PRAETORIANUS);

		//BLOCK EFFECTS OF GLADIATOR
		pa = (PraetorianusActivator)move.chooseCardToActivate(3);
		pa.chooseDiceDisc(5);
		pa.complete();

		//BLOCK EFECTS OF LEGAT
		PraetorianusActivator pa2 = (PraetorianusActivator)move.chooseCardToActivate(4);
		pa2.chooseDiceDisc(2);
		pa2.complete();

		move.endTurn();

		//BEGINNING PLAYER 2'S TURN
		gameState.setActionDice(new int[] {2, 3, 5});

		//TRY TO GAIN VP VIA LEGAT
		initialVP = gameState.getPlayerVictoryPoints(1);
		LegatActivator la2 = (LegatActivator)move.chooseCardToActivate(2);
		la2.complete();
		//SHOULD NOT HAVE GAINED ANY VP
		assert(gameState.getPlayerVictoryPoints(1) == initialVP);

		//TRY TO KICK THE PRAETOR ON DICE DISC 3
		//EXCEPT ITS EFFECTS SHOULD BE BLOCKED
		GladiatorActivator ga = (GladiatorActivator)move.chooseCardToActivate(5);
		ga.chooseDiceDisc(3);
		ga.complete();
		//SHOULD NOT HAVE BEEN KICKED
		assert(gameState.getPlayerCardsOnDiscs(0)[2] == Card.PRAETORIANUS);

		//NEVER GIVE UP
		//ATTACK THE PRAETOR INSTEAD
		ca = (CenturioActivator)move.chooseCardToActivate(3);
		ca.giveAttackDieRoll(4);
		ca.chooseCenturioAddActionDie(false);
		ca.complete();
		//SHOULD KILL THE PRAETOR
		assert(gameState.getPlayerCardsOnDiscs(0)[2] == Card.NOT_A_CARD);

	}

}
