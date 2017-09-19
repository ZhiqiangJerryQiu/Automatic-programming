package tests.verifiedSpec2;

import java.util.ArrayList;
import java.util.Arrays;

import framework.Rules;
import framework.Test;
import framework.cards.Card;
import framework.interfaces.GameState;
import framework.interfaces.MoveMaker;
import framework.interfaces.activators.TelephoneBoxActivator;
import framework.interfaces.activators.TribunusPlebisActivator;

/*
 * @author Kenneth Wong
 * @author Luke Harrison
 */
public class CardActivatorTelephoneBoxProperTimeParadoxMeasuresTest extends Test {

	@Override
	public String getShortDescription() {
		return "Test that a time paradox leads to the correct effects";
	}

	@Override
	public void run(GameState gameState, MoveMaker move) throws AssertionError,
			UnsupportedOperationException, IllegalArgumentException {
		/*
		 * P1 has Tribunus Plebis and Telephone box on the board (and other irrelevant stuff)
		 * 1: P1 activates Tribunus Plebis
		 * 2: P2 does nothing
		 * 3: P1 plays Legat over Tribunus Plebis, sends it back to beginning of game
		 * ????
		 * Hilarity ensues
		 */

		gameState.setDeck(Arrays.asList(new Card[]{Card.LEGAT}));
		gameState.setDiscard(new ArrayList<Card>());
		gameState.setPlayerCardsOnDiscs(0, new Card[]
				{
					Card.TRIBUNUSPLEBIS,
					Card.TELEPHONEBOX,
					Card.LEGAT,
					Card.LEGAT,
					Card.LEGAT,
					Card.LEGAT,
					Card.LEGAT,
				});
		gameState.setPlayerCardsOnDiscs(1, new Card[]
				{
					Card.LEGAT,
					Card.LEGAT,
					Card.LEGAT,
					Card.LEGAT,
					Card.LEGAT,
					Card.LEGAT,
					Card.LEGAT,
				});
		gameState.setPlayerVictoryPoints(0, 10);
		gameState.setPlayerVictoryPoints(1, 10);

		gameState.setPlayerSestertii(0, 100);
		gameState.setPlayerSestertii(1, 100);

		// to be used to check hand contents later
		gameState.setPlayerHand(0, Arrays.asList(new Card[]{Card.LEGAT, Card.CONSUL}));
		gameState.setPlayerHand(1, Arrays.asList(new Card[]{Card.FORUM}));
		
		gameState.setWhoseTurn(0);
		//1:
		gameState.setActionDice(new int[]{1, 1, 1});
		
		TribunusPlebisActivator tpa = (TribunusPlebisActivator)move.chooseCardToActivate(Rules.DICE_DISC_1);
		tpa.complete();
		
		move.endTurn();
		
		//2:
		move.endTurn();
		
		//3:
		gameState.setActionDice(new int[]{1, 2, 6});
		
		move.placeCard(Card.LEGAT, Rules.DICE_DISC_1);
		
		TelephoneBoxActivator tba = (TelephoneBoxActivator)move.chooseCardToActivate(Rules.DICE_DISC_2);
		tba.chooseDiceDisc(Rules.DICE_DISC_1);
		tba.shouldMoveForwardInTime(false);
		tba.setSecondDiceUsed(6);
		tba.complete();
		
		//Hilarity please!		
		assert(gameState.isGameCompleted());
		assert(gameState.getPlayerVictoryPoints(0) == 0);
		for (int i = 0; i < Rules.NUM_DICE_DISCS; i++) {
			assert(gameState.getPlayerCardsOnDiscs(0)[i] == Card.NOT_A_CARD);
			assert(gameState.getPlayerCardsOnDiscs(1)[i] == Card.NOT_A_CARD);
		}
	}

}
