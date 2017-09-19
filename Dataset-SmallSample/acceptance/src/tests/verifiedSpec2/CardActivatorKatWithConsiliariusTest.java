package tests.verifiedSpec2;

import framework.Rules;
import framework.Test;
import framework.cards.Card;
import framework.interfaces.GameState;
import framework.interfaces.MoveMaker;
import framework.interfaces.activators.*;

/**
 * 
 * Test the card Kat with Consiliarius
 * 
 * @author Wengfai (Steven) LOU & Yuyuan (Ben) HUANG
 *
 * Editing by Luke Harrison
 */

public class CardActivatorKatWithConsiliariusTest extends Test {

	private final int PLAYER_1 = 0;
	private final int PLAYER_2 = 1;

	@Override
	public String getShortDescription() {
		return "Test the card Kat with Consiliarius";
	}

	@Override
	public void run(GameState gameState, MoveMaker move) throws AssertionError,
			UnsupportedOperationException, IllegalArgumentException {
		
		// setting the initial state
		gameState.setWhoseTurn(PLAYER_1);
		gameState.setPlayerVictoryPoints(PLAYER_1, 17);
		gameState.setPlayerVictoryPoints(PLAYER_2, 17);
		gameState.setPlayerCardsOnDiscs(PLAYER_1,
				new Card[] {
					Card.NOT_A_CARD,
					Card.SICARIUS,
					Card.SICARIUS,
					Card.LEGIONARIUS,
					Card.NOT_A_CARD,
					Card.VELITES,
					Card.NOT_A_CARD,
				});
		
		gameState.setPlayerCardsOnDiscs(PLAYER_2,
				new Card[] {
					Card.CONSILIARIUS,
					Card.NOT_A_CARD,
					Card.NOT_A_CARD,
					Card.KAT,
					Card.NOT_A_CARD,
					Card.NOT_A_CARD,
					Card.NOT_A_CARD,
				});
		
		gameState.setActionDice(new int[] {4,4,4});
		
		// Kat's lives become 6 after thrice attacks
		for (int i = gameState.getActionDice().length; i > 0 ; i--) {
			assert(gameState.getActionDice().length == i);
			LegionariusActivator legionarius = (LegionariusActivator) move.chooseCardToActivate(Rules.DICE_DISC_4);
			legionarius.giveAttackDieRoll(6);
			legionarius.complete();
			assert(gameState.getActionDice().length == i-1);
		}
		move.endTurn();
      gameState.setActionDice(new int[] {6,6,6});
		move.endTurn();
		gameState.setActionDice(new int[] {6,6,6});
		// Kat's lives become 3 after thrice attacks
		for (int i = gameState.getActionDice().length; i > 0 ; i--) {
			assert(gameState.getActionDice().length == i);
			VelitesActivator velites = (VelitesActivator) move.chooseCardToActivate(Rules.DICE_DISC_6);
			velites.chooseDiceDisc(Rules.DICE_DISC_4);
			velites.giveAttackDieRoll(6);
			velites.complete();
			assert(gameState.getActionDice().length == i-1);
		}
		
		// Now becomes player2's turn, Coniliarius will be activated and move the Kat
		// Kat's lives should not be restored
		move.endTurn();
		
		gameState.setActionDice(new int[] {1,1,1});
		ConsiliariusActivator consiliarius = (ConsiliariusActivator) move.chooseCardToActivate(Rules.DICE_DISC_1);
		consiliarius.placeCard(Card.KAT, Rules.DICE_DISC_2);
		consiliarius.placeCard(Card.CONSILIARIUS, Rules.DICE_DISC_1);
		consiliarius.complete();
		
		// check the effect of consiliarius, Kat should be at disc 2
		Card[] player2Field = gameState.getPlayerCardsOnDiscs(PLAYER_2);
		assert(player2Field[Rules.DICE_DISC_2-1] == Card.KAT);
		assert(player2Field[Rules.DICE_DISC_4-1] == Card.NOT_A_CARD);
		
		// back to player1's turn
		move.endTurn();
		
		gameState.setActionDice(new int[] {6,2,3});
		
		// Kat's lives become 2
		SicariusActivator sicarius = (SicariusActivator) move.chooseCardToActivate(Rules.DICE_DISC_2);
		sicarius.chooseDiceDisc(Rules.DICE_DISC_2);
		sicarius.complete();
		
		// Kat's lives become 1
		sicarius = (SicariusActivator) move.chooseCardToActivate(Rules.DICE_DISC_3);
		sicarius.chooseDiceDisc(Rules.DICE_DISC_2);
		sicarius.complete();
		
		Card[] player1Field = gameState.getPlayerCardsOnDiscs(PLAYER_1);
		assert(player1Field[Rules.DICE_DISC_2-1] == Card.NOT_A_CARD);
		assert(player1Field[Rules.DICE_DISC_3-1] == Card.NOT_A_CARD);
		
		// this should kill the Kat
		VelitesActivator velites = (VelitesActivator) move.chooseCardToActivate(Rules.DICE_DISC_6);
		velites.chooseDiceDisc(Rules.DICE_DISC_2);
		velites.giveAttackDieRoll(6);
		velites.complete();
		
		player2Field = gameState.getPlayerCardsOnDiscs(PLAYER_2);
		assert(player2Field[Rules.DICE_DISC_2-1] == Card.NOT_A_CARD);
	}
}
