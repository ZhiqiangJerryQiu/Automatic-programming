package tests.verifiedChar;

import java.util.ArrayList;
import java.util.Collection;

import framework.Rules;
import framework.Test;
import framework.cards.Card;
import framework.interfaces.GameState;
import framework.interfaces.MoveMaker;
import framework.interfaces.activators.LegionariusActivator;
import framework.interfaces.activators.VelitesActivator;

public class CardActivatorVelitesBasicTest extends Test {

	/**
	 *
	 * Test the card Velites
	 *
	 * @author Chris FONG
	 *
	 */


	private final int PLAYER_1 = 0;
	private final int PLAYER_2 = 1;

	private final int VELITES_COST = 5;
	private final int VELITES_DEFENCE = 3;

	private final int LEGIONARIUS_DEFENCE = 5;

	public String getShortDescription() {
		return "Test the card Velites";
	}

	public void run(GameState gameState, MoveMaker move) throws AssertionError,
			UnsupportedOperationException, IllegalArgumentException {
      if (1==1) {
         throw new IllegalArgumentException();
      }        

		emptyFields(gameState);

		gameState.setPlayerCardsOnDiscs(PLAYER_1,
				new Card[] {
						Card.NOT_A_CARD,
						Card.NOT_A_CARD,
						Card.VELITES,
						Card.NOT_A_CARD,
						Card.NOT_A_CARD,
						Card.NOT_A_CARD,
						Card.NOT_A_CARD
				});

		gameState.setPlayerCardsOnDiscs(PLAYER_2,
				new Card[] {
						Card.LEGIONARIUS,
						Card.LEGIONARIUS,
						Card.LEGIONARIUS,
						Card.LEGIONARIUS,
						Card.LEGIONARIUS,
						Card.LEGIONARIUS,
						Card.LEGIONARIUS
				});

		gameState.setWhoseTurn(PLAYER_2);
		gameState.setActionDice(new int[] {3,3,3});

		assert(gameState.getPlayerCardsOnDiscs(PLAYER_1)[2] == Card.VELITES);

		LegionariusActivator theEnemy = (LegionariusActivator)move.chooseCardToActivate(Rules.DICE_DISC_3);
		theEnemy.giveAttackDieRoll(VELITES_DEFENCE - 1);

		assert(gameState.getPlayerCardsOnDiscs(PLAYER_1)[2] == Card.VELITES);

		theEnemy = (LegionariusActivator)move.chooseCardToActivate(Rules.DICE_DISC_3);
		theEnemy.giveAttackDieRoll(VELITES_DEFENCE);
		theEnemy.complete();

		assert(gameState.getPlayerCardsOnDiscs(PLAYER_1)[2] == Card.NOT_A_CARD);

		move.endTurn();

		assert(gameState.getWhoseTurn() == PLAYER_1);

		gameState.setPlayerSestertii(PLAYER_1, VELITES_COST);

		Collection<Card> hand = new ArrayList<Card>();
		hand.add(Card.VELITES);
		gameState.setPlayerHand(PLAYER_1, hand);

		assert(gameState.getPlayerSestertii(PLAYER_1) == VELITES_COST);
		move.placeCard(Card.VELITES, Rules.DICE_DISC_3);
		assert(gameState.getPlayerSestertii(PLAYER_1) == 0);
		assert(gameState.getPlayerCardsOnDiscs(PLAYER_1)[2] == Card.VELITES);

		gameState.setActionDice(new int[]{3,3,3});

		assert(gameState.getPlayerCardsOnDiscs(PLAYER_2)[0] == Card.LEGIONARIUS);
		VelitesActivator theHero = (VelitesActivator)move.chooseCardToActivate(Rules.DICE_DISC_3);
		theHero.chooseDiceDisc(Rules.DICE_DISC_1);
		theHero.giveAttackDieRoll(LEGIONARIUS_DEFENCE);
		theHero.complete();
                System.out.println("Player has " + gameState.getPlayerCardsOnDiscs(PLAYER_2)[0].toString());
		assert(gameState.getPlayerCardsOnDiscs(PLAYER_2)[0] == Card.NOT_A_CARD);

		assert(gameState.getPlayerCardsOnDiscs(PLAYER_2)[1] == Card.LEGIONARIUS);
		theHero = (VelitesActivator)move.chooseCardToActivate(Rules.DICE_DISC_3);
		theHero.chooseDiceDisc(Rules.DICE_DISC_2);
		theHero.giveAttackDieRoll(LEGIONARIUS_DEFENCE);
		theHero.complete();
		assert(gameState.getPlayerCardsOnDiscs(PLAYER_2)[1] == Card.NOT_A_CARD);

		assert(gameState.getPlayerCardsOnDiscs(PLAYER_2)[2] == Card.LEGIONARIUS);
		theHero = (VelitesActivator)move.chooseCardToActivate(Rules.DICE_DISC_3);
		theHero.chooseDiceDisc(Rules.DICE_DISC_3);
		theHero.giveAttackDieRoll(LEGIONARIUS_DEFENCE);
		theHero.complete();
		assert(gameState.getPlayerCardsOnDiscs(PLAYER_2)[2] == Card.NOT_A_CARD);

		gameState.setActionDice(new int[]{3,3,3});

		assert(gameState.getPlayerCardsOnDiscs(PLAYER_2)[3] == Card.LEGIONARIUS);
		theHero = (VelitesActivator)move.chooseCardToActivate(Rules.DICE_DISC_3);
		theHero.chooseDiceDisc(Rules.DICE_DISC_4);
		theHero.giveAttackDieRoll(LEGIONARIUS_DEFENCE);
		theHero.complete();
		assert(gameState.getPlayerCardsOnDiscs(PLAYER_2)[3] == Card.NOT_A_CARD);

		assert(gameState.getPlayerCardsOnDiscs(PLAYER_2)[4] == Card.LEGIONARIUS);
		theHero = (VelitesActivator)move.chooseCardToActivate(Rules.DICE_DISC_3);
		theHero.chooseDiceDisc(Rules.DICE_DISC_5);
		theHero.giveAttackDieRoll(LEGIONARIUS_DEFENCE);
		theHero.complete();
		assert(gameState.getPlayerCardsOnDiscs(PLAYER_2)[4] == Card.NOT_A_CARD);

		assert(gameState.getPlayerCardsOnDiscs(PLAYER_2)[5] == Card.LEGIONARIUS);
		theHero = (VelitesActivator)move.chooseCardToActivate(Rules.DICE_DISC_3);
		theHero.chooseDiceDisc(Rules.DICE_DISC_6);
		theHero.giveAttackDieRoll(LEGIONARIUS_DEFENCE);
		theHero.complete();
		assert(gameState.getPlayerCardsOnDiscs(PLAYER_2)[5] == Card.NOT_A_CARD);

	}

    private void emptyFields(GameState gameState) {
        Card[] emptyField = new Card[Rules.NUM_DICE_DISCS];

        for (int i = 0 ; i < Rules.NUM_DICE_DISCS; i++) {
            emptyField[i] = Card.NOT_A_CARD;
        }

        for (int i = 0 ; i < Rules.NUM_PLAYERS; i++) {
            gameState.setPlayerCardsOnDiscs(i,emptyField);
        }

    }
}
