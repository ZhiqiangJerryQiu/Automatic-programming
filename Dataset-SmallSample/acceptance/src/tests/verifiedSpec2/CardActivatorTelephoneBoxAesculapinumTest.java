package tests.verifiedSpec2;

import java.util.ArrayList;
import java.util.Arrays;

import framework.Rules;
import framework.Test;
import framework.cards.Card;
import framework.interfaces.GameState;
import framework.interfaces.MoveMaker;
import framework.interfaces.activators.AesculapinumActivator;
import framework.interfaces.activators.LegionariusActivator;
import framework.interfaces.activators.TelephoneBoxActivator;

/*
 * @author Kenneth Wong
 * @author Luke Harrison
 */

public class CardActivatorTelephoneBoxAesculapinumTest extends Test {

	@Override
	public String getShortDescription() {
		return "Test that a card not being in discard pile results in Aesculapinum time paradox";
	}

	@Override
	public void run(GameState gameState, MoveMaker move) throws AssertionError,
			UnsupportedOperationException, IllegalArgumentException {
		// TODO Auto-generated method stub
		
		/*
		 * 1: P1 has Aesculapinum, Tribunus Plebis, Telephone Box on board (and other irrelevant stuff)
		 * 2: P2 attacks Tribunus Plebis with Legionarius, successful kill
		 * 3: P1 activates Aesculapinum to get a Tribunus Plebis, then plays Turris and time travels it back
		 * ???
		 * Hilarity ensues (and by that I mean time paradox)
		 */

		gameState.setDeck(Arrays.asList(new Card[]{Card.LEGAT}));
		gameState.setDiscard(new ArrayList<Card>());
		gameState.setPlayerCardsOnDiscs(0, new Card[]
				{
					Card.AESCULAPINUM,
					Card.TRIBUNUSPLEBIS,
					Card.TELEPHONEBOX,
					Card.LEGAT,
					Card.LEGAT,
					Card.LEGAT,
					Card.LEGAT,
				});
		gameState.setPlayerCardsOnDiscs(1, new Card[]
				{
					Card.LEGAT,
					Card.LEGIONARIUS,
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
		
		gameState.setPlayerHand(0, Arrays.asList(new Card[]{Card.TURRIS}));
		gameState.setPlayerHand(1, new ArrayList<Card>());
		
		// 1:
		gameState.setWhoseTurn(0);
		move.endTurn();
		gameState.setActionDice(new int[]{2, 2, 2});
		
		// 2:
		LegionariusActivator la = (LegionariusActivator)move.chooseCardToActivate(Rules.DICE_DISC_2);
		// juuust enough to kill Tribunus Plebis
		la.giveAttackDieRoll(5);
		la.complete();
		assert(gameState.getDiscard().contains(Card.TRIBUNUSPLEBIS));
		assert(gameState.getDiscard().size() == 1);
		move.endTurn();
		gameState.setActionDice(new int[]{1, 3, 6});
		assert(!gameState.isGameCompleted());
		
		// 3:
		AesculapinumActivator aa = (AesculapinumActivator)move.chooseCardToActivate(Rules.DICE_DISC_1);
		// there should only be one card there anyway
		aa.chooseCardFromPile(0);
		aa.complete();

		assert(gameState.getPlayerHand(0).contains(Card.TRIBUNUSPLEBIS));
		assert(gameState.getPlayerHand(0).size() == 2);
		
		move.placeCard(Card.TURRIS, Rules.DICE_DISC_4);
		
		TelephoneBoxActivator tba = (TelephoneBoxActivator)move.chooseCardToActivate(Rules.DICE_DISC_3);
		tba.chooseDiceDisc(Rules.DICE_DISC_4);
		tba.setSecondDiceUsed(6);
		tba.shouldMoveForwardInTime(false);
		tba.complete();
		
		// Hilarity ensues
		assert(gameState.isGameCompleted());
		assert(gameState.getPlayerVictoryPoints(0) == 0);
		assert(gameState.getPlayerVictoryPoints(1) != 0);
	}

}
