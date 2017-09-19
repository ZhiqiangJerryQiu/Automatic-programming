package tests.verifiedBuilding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import framework.Rules;
import framework.Test;
import framework.cards.Card;
import framework.interfaces.GameState;
import framework.interfaces.MoveMaker;
import framework.interfaces.activators.*;

/**
 * @author: Anne
 * @author: Wen Di
 * Basic test for Scaenicus
 * Tests copying Nero, Consul and Centurio
 */

public class CardActivatorScaenicusBZBasicTest extends Test {

	private final int PLAYER_1 = 0;
	private final int PLAYER_2 = 1;

	private Card[][] playerFields;
	private List<Card>[] playerHands;
	private GameState gameState;
	private MoveMaker move;

	public String getShortDescription() {
		return "Testing the basic functionality of Scaenicus";
	}

	public void run(GameState gameState, MoveMaker move) throws AssertionError,
	UnsupportedOperationException, IllegalArgumentException {
		this.gameState = gameState;
		this.move = move;
		this.playerHands = (ArrayList<Card>[]) new ArrayList[Rules.NUM_PLAYERS];
		this.playerHands[PLAYER_1] = new ArrayList<Card>();
		this.playerHands[PLAYER_2] = new ArrayList<Card>();
		this.playerFields = new Card[Rules.NUM_PLAYERS][Rules.NUM_DICE_DISCS];

		//INITIALISE GAME STATE
		
		this.gameState.setPlayerSestertii(PLAYER_1, 8 + 8 + 3 + 9);
		this.gameState.setPlayerSestertii(PLAYER_2, 5 + 5 + 5 + 5);
		
		//	Hands
		playerHands[PLAYER_1].add(Card.SCAENICUS);
		playerHands[PLAYER_1].add(Card.NERO);
		playerHands[PLAYER_1].add(Card.CONSUL);
		playerHands[PLAYER_1].add(Card.CENTURIO);

		playerHands[PLAYER_2].add(Card.FORUM);
		playerHands[PLAYER_2].add(Card.FORUM);
		playerHands[PLAYER_1].add(Card.TRIBUNUSPLEBIS);
		playerHands[PLAYER_1].add(Card.TRIBUNUSPLEBIS);

		transferInitialHandsToState();
		assertHands();

		//	Field
		playerFields[PLAYER_1] =
				new Card[] {
				Card.NOT_A_CARD,
				Card.SCAENICUS,
				Card.NERO,
				Card.CONSUL,
				Card.CENTURIO,
				Card.NOT_A_CARD,
				Card.NOT_A_CARD
		};

		playerFields[PLAYER_2] =
				new Card[] {
				Card.NOT_A_CARD,
				Card.FORUM,
				Card.FORUM,
				Card.NOT_A_CARD,
				Card.TRIBUNUSPLEBIS,
				Card.TRIBUNUSPLEBIS,
				Card.NOT_A_CARD
		};

		transferInitialFieldsToState();
		assertFields();

		//START THE GAME: TURN 0
		gameState.setWhoseTurn(PLAYER_1);
		gameState.setActionDice(new int[] {2, 6, 4});

		//SCAENICUS COPIES NERO
		//	Activate Scaenicus
		ScaenicusActivator scaenicusActivator = (ScaenicusActivator) move.chooseCardToActivate(2);
		//	Copy Nero
		NeroActivator neroActivator = (NeroActivator) scaenicusActivator.getScaenicusMimicTarget(3);
		//	Nero attacks the forum on disc 3
		neroActivator.chooseDiceDisc(Rules.DICE_DISC_3);

		neroActivator.complete();
		scaenicusActivator.complete();

		//The forum on disc 3 should be gone, Scaenicus should be gone (Nero destroys itself)
		playerFields[PLAYER_1] =
				new Card[] {
				Card.NOT_A_CARD,
				Card.NOT_A_CARD,
				Card.NERO,
				Card.CONSUL,
				Card.CENTURIO,
				Card.NOT_A_CARD,
				Card.NOT_A_CARD
		};

		playerFields[PLAYER_2] =
				new Card[] {
				Card.NOT_A_CARD,
				Card.FORUM,
				Card.NOT_A_CARD,
				Card.NOT_A_CARD,
				Card.TRIBUNUSPLEBIS,
				Card.TRIBUNUSPLEBIS,
				Card.NOT_A_CARD
		};

		assertFields();

		//LAY NEW SCAENICUS FOR USE
		playerFields[PLAYER_1] =
				new Card[] {
				Card.NOT_A_CARD,
				Card.NOT_A_CARD,
				Card.NERO,
				Card.CONSUL,
				Card.CENTURIO,
				Card.SCAENICUS,
				Card.NOT_A_CARD
		};

		transferFieldsToState();
		assertFields();

		//SCAENICUS COPIES CONSUL
		//	Before activation, we have 2 dice left
		assert(gameState.getActionDice().length == 2);

		//	Activate Scaenicus
		scaenicusActivator = (ScaenicusActivator) move.chooseCardToActivate(6);
		//	Copy Consul
		ConsulActivator consulActivator = (ConsulActivator) scaenicusActivator.getScaenicusMimicTarget(4);
		//	Choose to decrease dice with value 4
		consulActivator.chooseWhichDiceChanges(4);
		consulActivator.chooseConsulChangeAmount(-1);

		consulActivator.complete();
		scaenicusActivator.complete();

		//	We have 1 dice left, which is (4 - 1 = 3)
		assert(gameState.getActionDice().length == 1);
		assert(gameState.getActionDice()[0] == 3);

		//	The field shouldn't have changed
		playerFields[PLAYER_1] =
				new Card[] {
				Card.NOT_A_CARD,
				Card.NOT_A_CARD,
				Card.NERO,
				Card.CONSUL,
				Card.CENTURIO,
				Card.SCAENICUS,
				Card.NOT_A_CARD
		};

		playerFields[PLAYER_2] =
				new Card[] {
				Card.NOT_A_CARD,
				Card.FORUM,
				Card.NOT_A_CARD,
				Card.NOT_A_CARD,
				Card.TRIBUNUSPLEBIS,
				Card.TRIBUNUSPLEBIS,
				Card.NOT_A_CARD
		};

		assertFields();

		//TURN 2: the field shouldn't have changed
		move.endTurn();
		move.endTurn();
		gameState.setActionDice(new int[] {6, 2, 3});

		playerFields[PLAYER_1] =
				new Card[] {
				Card.NOT_A_CARD,
				Card.NOT_A_CARD,
				Card.NERO,
				Card.CONSUL,
				Card.CENTURIO,
				Card.SCAENICUS,
				Card.NOT_A_CARD
		};

		playerFields[PLAYER_2] =
				new Card[] {
				Card.NOT_A_CARD,
				Card.FORUM,
				Card.NOT_A_CARD,
				Card.NOT_A_CARD,
				Card.TRIBUNUSPLEBIS,
				Card.TRIBUNUSPLEBIS,
				Card.NOT_A_CARD
		};

		assertFields();

		//SCAENICUS COPIES CENTURIO
		//	Activate Scaenicus
		scaenicusActivator = (ScaenicusActivator) move.chooseCardToActivate(6);
		//	Copy Centurio
		CenturioActivator centurioActivator = (CenturioActivator) scaenicusActivator.getScaenicusMimicTarget(5);

		//	Set the battle die
		centurioActivator.giveAttackDieRoll(5);
		centurioActivator.chooseCenturioAddActionDie(false);

		centurioActivator.complete();
		scaenicusActivator.complete();

		//	The opposing Tribunus Plebis should be destroyed
		playerFields[PLAYER_1] =
				new Card[] {
				Card.NOT_A_CARD,
				Card.NOT_A_CARD,
				Card.NERO,
				Card.CONSUL,
				Card.CENTURIO,
				Card.SCAENICUS,
				Card.NOT_A_CARD
		};

		playerFields[PLAYER_2] =
				new Card[] {
				Card.NOT_A_CARD,
				Card.FORUM,
				Card.NOT_A_CARD,
				Card.NOT_A_CARD,
				Card.TRIBUNUSPLEBIS,
				Card.NOT_A_CARD,
				Card.NOT_A_CARD
		};

		assertFields();
	}

	private void transferInitialFieldsToState() {
		for (int i = 0; i < Rules.NUM_PLAYERS; i++) {
			this.gameState.setPlayerCardsOnDiscs(i,playerFields[i]);
		}
	}

	private void transferFieldsToState() {
		for (int j = 0; j < Rules.NUM_DICE_DISCS; j++) {
			if (playerFields[gameState.getWhoseTurn()][j] != Card.NOT_A_CARD) {
				move.placeCard(playerFields[gameState.getWhoseTurn()][j], j+1);					
			}
		}
	}

	private void transferInitialHandsToState() {
		for (int i = 0; i < Rules.NUM_PLAYERS; i++) {
			this.gameState.setPlayerHand(i, playerHands[i]);
		}
	}

	private void assertHands() {
		for (int i = 0; i < Rules.NUM_PLAYERS; i++) {
			assert(gameState.getPlayerHand(i).containsAll(playerHands[i]));
		}
	}

	private void assertFields() {
		for (int i = 0; i < Rules.NUM_PLAYERS; i++) {
			assert(Arrays.equals(gameState.getPlayerCardsOnDiscs(i),playerFields[i]));
		}
	}
	
	private static String padRight(String s, int width) {
	     return String.format("%1$-" + width + "s", s);  
	}

	private static String padLeft(String s, int width) {
	    return String.format("%1$#" + width + "s", s);  
	}
	
	/*
	 * Prints out all the fields for debugging
	 */
	private void printFields() {
		System.out.println(padLeft("Yours | Expected",30));
		for (int i = 0; i < Rules.NUM_PLAYERS; i++) {
			System.out.println(padLeft("---Player " + (i+1) + "---",27));
			for (int j = 0; j < Rules.NUM_DICE_DISCS; j++) {
				Card yours = gameState.getPlayerCardsOnDiscs(i)[j];
				Card test = playerFields[i][j];
				System.out.print (padRight (yours.toString(), 20));
				if (yours != test) {
					System.out.print("*");
				} else {
					System.out.print ("|");					
				}
				System.out.print (padLeft (test.toString(), 20));
				System.out.println();
			}
		}

	}
}
