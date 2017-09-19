package tests.verifiedSpec2;

import java.util.ArrayList;
import java.util.Arrays;

import framework.Rules;
import framework.Test;
import framework.cards.Card;
import framework.interfaces.GameState;
import framework.interfaces.MoveMaker;
import framework.interfaces.activators.ForumActivator;
import framework.interfaces.activators.TelephoneBoxActivator;

/*
 * @author Kenneth Wong
 * @author Luke Harrison
 */
public class CardActivatorTelephoneBoxTemplumTest extends Test {

	@Override
	public String getShortDescription() {
		return "Test that when templum is removed, time paradox occurs only when it's meant to";
	}

	@Override
	public void run(GameState gameState, MoveMaker move) throws AssertionError,
			UnsupportedOperationException, IllegalArgumentException {
		/*
		 * 1: P1 has Forum, Templum, Telephone Box on board (and other irrelevant stuff)
		 * 2: P1 activates Forum, disregards Templum
		 * 3: P2 does nothing
		 * 4: P1 plays <anything> on top of Templum, sends it back (overwriting Templum)
		 * 5: <nothing interesting happens>
		 * 6: P1 plays Templum back on top
		 * 7: P2 does nothing
		 * 8: P1 activates Forum, uses Templum
		 * 9: P2 does nothing
		 * 10: P1 plays <anything> on top of Templum, sends it back (overwriting Templum)
		 * ???
		 * Hilarity ensues
		 */

		gameState.setDeck(Arrays.asList(new Card[]{Card.LEGAT}));
		gameState.setDiscard(new ArrayList<Card>());
		gameState.setPlayerCardsOnDiscs(0, new Card[]
				{
					Card.TEMPLUM,
					Card.FORUM,
					Card.TELEPHONEBOX,
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
		
		gameState.setPlayerHand(0, Arrays.asList(new Card[]{Card.LEGAT, Card.LEGAT, Card.TEMPLUM}));
		gameState.setPlayerHand(1, new ArrayList<Card>());
		
		// 1:
		gameState.setWhoseTurn(0);
		gameState.setActionDice(new int[]{2, 2, 2});
		
		// 2:
		ForumActivator fa = (ForumActivator)move.chooseCardToActivate(Rules.DICE_DISC_2);
		fa.chooseActionDice(2);
		fa.chooseActivateTemplum(false);
		fa.complete();
		
		// 3:
		move.endTurn();
		move.endTurn();
		gameState.setActionDice(new int[]{2, 3, 6});
		
		// 4:
		move.placeCard(Card.LEGAT, Rules.DICE_DISC_1);
		TelephoneBoxActivator tba = (TelephoneBoxActivator)move.chooseCardToActivate(Rules.DICE_DISC_3);
		tba.chooseDiceDisc(Rules.DICE_DISC_1);
		tba.setSecondDiceUsed(6);
		tba.shouldMoveForwardInTime(false);
		tba.complete();
		
		// 5:
		assert(!gameState.isGameCompleted());
		
		// 6:
		move.placeCard(Card.TEMPLUM, Rules.DICE_DISC_1);
		
		// 7:
		move.endTurn();
		move.endTurn();
		gameState.setActionDice(new int[]{2, 2, 2});
		
		// 8:
		fa = (ForumActivator)move.chooseCardToActivate(Rules.DICE_DISC_2);
		fa.chooseActionDice(2);
		fa.chooseActivateTemplum(true);
		fa.chooseActivateTemplum(2);
		fa.complete();
		
		// 9:
		move.endTurn();
		move.endTurn();
		gameState.setActionDice(new int[]{2, 3, 2});
		
		// 10:
		move.placeCard(Card.LEGAT, Rules.DICE_DISC_1);
		tba = (TelephoneBoxActivator)move.chooseCardToActivate(Rules.DICE_DISC_3);
		tba.chooseDiceDisc(Rules.DICE_DISC_1);
		tba.setSecondDiceUsed(2);
		tba.shouldMoveForwardInTime(false);
		tba.complete();
		
		// Hilarity ensues
		assert(gameState.isGameCompleted());
		assert(gameState.getPlayerVictoryPoints(0) == 0);
		assert(gameState.getPlayerVictoryPoints(1) != 0);
	}

}
