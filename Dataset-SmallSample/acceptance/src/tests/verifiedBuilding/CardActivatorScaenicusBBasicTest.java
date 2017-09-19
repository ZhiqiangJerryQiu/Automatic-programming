package tests.verifiedBuilding;

import java.util.Arrays;
import framework.Rules;
import framework.Test;
import framework.cards.Card;
import framework.interfaces.GameState;
import framework.interfaces.MoveMaker;
import framework.interfaces.activators.*;

/**
 * @author: Anne & Wen Di
 * Basic test for Scaenicus
 * Tests copying Nero, Consul and Centurio
 */

public class CardActivatorScaenicusBBasicTest extends Test {

	private final int PLAYER_1 = 0;
	private final int PLAYER_2 = 1;
	private Card[][] playerFields;
	private GameState gameState;

	public String getShortDescription() {
		return "Testing the basic functionality of Scaenicus";
	}

	public void run(GameState gameState, MoveMaker move) throws AssertionError,
	UnsupportedOperationException, IllegalArgumentException {
      if (1==1) {
         throw new IllegalArgumentException();
      }        


		this.gameState = gameState;

		/*
            Starting Process:
		 * Initialises Field to empty fields
		 */

		initialisePlayerFields ();
		transferFieldsToState();
		assertFields();

		/*
		 * Lay down cards
		 */
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


		transferFieldsToState();
		assertFields();

		/*
		 * Set action die
		 */
		gameState.setActionDice(new int[] {2, 6, 4});

		/*
		 * Activate Scaenicus: copy Nero
		 */

		gameState.setWhoseTurn(PLAYER_1);

		//Activate Scaenicus
		ScaenicusActivator scaenicusActivator = (ScaenicusActivator) move.chooseCardToActivate(2);
		//Copy Nero
		NeroActivator neroActivator = (NeroActivator) scaenicusActivator.getScaenicusMimicTarget(3);
		//Nero attacks the forum on disc 3
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

		/*
		 * Lay down a new Scaenicus for use
		 */
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

		transferFieldsToState();
		assertFields();

		/*
		 * Activate Scaenicus: copy Consul
		 */

		//Before activation, we have 2 dice left
		assert(gameState.getActionDice().length == 2);

		//Activate Scaenicus
		scaenicusActivator = (ScaenicusActivator) move.chooseCardToActivate(6);
		//Copy Consul
		ConsulActivator consulActivator = (ConsulActivator) scaenicusActivator.getScaenicusMimicTarget(4);
		//Choose to decrease dice with value 4
		consulActivator.chooseWhichDiceChanges(4);
		consulActivator.chooseConsulChangeAmount(-1);

		consulActivator.complete();
		scaenicusActivator.complete();

		//We have 1 dice left, which is (4 - 1 = 3)
		assert(gameState.getActionDice().length == 1);
		assert(gameState.getActionDice()[0] == 3);

		/*
		 * The field shouldnt have changed
		 */
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

		// Go to the next turn, the field shouldnt have changed
		gameState.setWhoseTurn(PLAYER_2);
		gameState.setWhoseTurn(PLAYER_1);

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

		/*
		 * Set action die
		 */
		 gameState.setActionDice(new int[] {6, 2, 3});

		/*
		 * Activate Scaenicus: copy Centurio
		 */

		 //Activate Scaenicus
		 scaenicusActivator = (ScaenicusActivator) move.chooseCardToActivate(6);
		 //Copy Centurio
		 CenturioActivator centurioActivator = (CenturioActivator) scaenicusActivator.getScaenicusMimicTarget(5);

		 //Set the battle die
		 centurioActivator.giveAttackDieRoll(5);
                 centurioActivator.chooseCenturioAddActionDie(false);

		 centurioActivator.complete();
		 scaenicusActivator.complete();

		 //The opposing Tribunus Plebis should be destroyed
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

	private void initialisePlayerFields () {

		playerFields = new Card[Rules.NUM_PLAYERS][Rules.NUM_DICE_DISCS];

		for (int i = 0; i < Rules.NUM_PLAYERS; i++) {
			for (int j = 0; j < Rules.NUM_DICE_DISCS; j++) {
				playerFields[i][j] = Card.NOT_A_CARD;
			}
		}

	}

	private void transferFieldsToState() {
		for (int i = 0; i < Rules.NUM_PLAYERS; i++) {
			this.gameState.setPlayerCardsOnDiscs(i,playerFields[i]);
		}

	}

	private void assertFields() {
		for (int i = 0; i < Rules.NUM_PLAYERS; i++) {
			assert(Arrays.equals(gameState.getPlayerCardsOnDiscs(i),playerFields[i]));
		}

	}

}
