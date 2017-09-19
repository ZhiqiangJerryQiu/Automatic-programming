package tests.verifiedBuilding;

import java.util.ArrayList;
import java.util.Collection;

import framework.Rules;
import framework.Test;
import framework.cards.Card;
import framework.interfaces.GameState;
import framework.interfaces.MoveMaker;
import framework.interfaces.activators.OnagerActivator;

/**
 * Tests the basic mechanics of the Onager card
 * @author Nicholas Higgins (nicholas.higgins)
 * @author Calvin Tam (calvin.tam)
 *
 */
public class CardActivatorOnagerABasicTest extends Test {

	@Override
	public String getShortDescription() {
		return "Tests the basic mechanics of the Onager card";
	}

	@Override
	public void run(GameState gameState, MoveMaker move) throws AssertionError,
			UnsupportedOperationException, IllegalArgumentException {

		// Set up the player stats
        gameState.setPlayerVictoryPoints(0, 10);
        gameState.setPlayerVictoryPoints(1, 10);
        gameState.setPlayerSestertii(0, 5);
        gameState.setPlayerSestertii(1, 0);

        // Set up the game state for the test
        gameState.setWhoseTurn(0);
        gameState.setActionDice(new int [] {4, 4, 4});

        Card [] opponentSide = {Card.FORUM,
                Card.SICARIUS,
                Card.NOT_A_CARD,
                Card.BASILICA,
                Card.NOT_A_CARD,
                Card.MERCATUS,
                Card.FORUM};

    	 // Place 5 cards on the opponent's side with no forums
        gameState.setPlayerCardsOnDiscs(1, opponentSide);

        Collection<Card> hand = new ArrayList<Card>();
        hand.add(Card.ONAGER);
        gameState.setPlayerHand(0, hand);

        // Place the Mercatus on disc 4 and retrieve its activator
        move.placeCard(Card.ONAGER, 4);
        OnagerActivator ma = (OnagerActivator) move.chooseCardToActivate(4);

        //**********************test1*************************//
        //Testing that it doesn't attack a character card like Sicarius

        opponentSide = gameState.getPlayerCardsOnDiscs(1);
        assert(opponentSide[1] == Card.SICARIUS);

        //***********************test2************************//
        //Testing that it can attack a building if it has a sufficient attack roll
        ma.chooseDiceDisc(Rules.DICE_DISC_1);
        ma.giveAttackDieRoll(6);
        ma.complete();

        opponentSide = gameState.getPlayerCardsOnDiscs(1);
        assert(opponentSide[0] == Card.NOT_A_CARD);

        //***********************test3************************//
        //Testing an attack against a building fails if it hasn't got a sufficient attack roll
        ma = (OnagerActivator) move.chooseCardToActivate(4);
        ma.chooseDiceDisc(Rules.DICE_DISC_6);
        ma.giveAttackDieRoll(2);
        ma.complete();

        opponentSide = gameState.getPlayerCardsOnDiscs(1);
        assert(opponentSide[5] == Card.MERCATUS);

        //***********************test4************************//
        //Testing that it can attack a building if it has an attack roll
        //even to that of the defence of the attacked card
        ma = (OnagerActivator) move.chooseCardToActivate(4);
        ma.chooseDiceDisc(Rules.DICE_DISC_6);
        ma.giveAttackDieRoll(3);
        ma.complete();

        opponentSide = gameState.getPlayerCardsOnDiscs(1);
        assert(opponentSide[5] == Card.NOT_A_CARD);

        //***********************end************************//
        //Testing that nothing odd has occured to the players during this
        //period of testing

        assert(gameState.getPlayerVictoryPoints(0) == 10);
        assert(gameState.getPlayerVictoryPoints(1) == 10);
        assert(gameState.getPlayerSestertii(0) == 0);
        assert(gameState.getPlayerSestertii(1) == 0);


	}

}
