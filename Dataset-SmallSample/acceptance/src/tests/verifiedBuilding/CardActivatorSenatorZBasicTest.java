package tests.verifiedBuilding;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import framework.Test;
import framework.cards.Card;
import framework.interfaces.GameState;
import framework.interfaces.MoveMaker;
import framework.interfaces.activators.*;

/**
 * Testing the basic mechanics of Senator.
 *
 * @author Karla Burnett (karla.burnett)
 * @author Lauren Spooner (lauren.spooner)
 */
public class CardActivatorSenatorZBasicTest extends Test {

    @Override
    public String getShortDescription() {
        return "Checking the basic mechanics of Senator";
    }

    @Override
    public void run(GameState gameState, MoveMaker move)
                                          throws AssertionError,
                                          UnsupportedOperationException,
                                          IllegalArgumentException {

    	Card[] field = new Card[7];
    	for(int i = 0; i < field.length; i++){
    		field[i] = Card.NOT_A_CARD;
    	}
    	gameState.setPlayerCardsOnDiscs(0, field);

    	List<Card> discard = new ArrayList<Card>();
    	gameState.setDiscard(discard);

        // Set up the player stats
        gameState.setPlayerVictoryPoints(0, 10);
        gameState.setPlayerVictoryPoints(1, 10);
        gameState.setPlayerSestertii(0, 3);
        gameState.setPlayerSestertii(1, 0);

        // Set up the game state for the test
        gameState.setWhoseTurn(0);
        gameState.setActionDice(new int [] {3, 3, 4});
        Collection<Card> hand = new ArrayList<Card> ();
        Collections.addAll(hand, Card.SENATOR,
                                 Card.VELITES,
                                 Card.PRAETORIANUS,
                                 Card.ESSEDUM,
                                 Card.CENTURIO,
                                 Card.SICARIUS,
                                 Card.ARCHITECTUS,
                                 Card.BASILICA);
        gameState.setPlayerHand(0, hand);

        // Place the Tribunus Plebis on disc 3 and activate it
        move.placeCard(Card.SENATOR, 3);

        // Check that player has lost the necessary sestertii from laying
        // these cards
        assert(gameState.getPlayerVictoryPoints(0) == 10);
        assert(gameState.getPlayerVictoryPoints(1) == 10);
        assert(gameState.getPlayerSestertii(0) == 0);
        assert(gameState.getPlayerSestertii(1) == 0);

        field = gameState.getPlayerCardsOnDiscs(0);
        assert(field[2] == Card.SENATOR);

        SenatorActivator s = (SenatorActivator)move.chooseCardToActivate(3);
        s.layCard(Card.VELITES, 4);
        s.layCard(Card.PRAETORIANUS, 7);
        s.complete();

        assert(gameState.getPlayerVictoryPoints(0) == 10);
        assert(gameState.getPlayerVictoryPoints(1) == 10);
        assert(gameState.getPlayerSestertii(0) == 0);
        assert(gameState.getPlayerSestertii(1) == 0);

        field = gameState.getPlayerCardsOnDiscs(0);
        assert(field[2] == Card.SENATOR);
        assert(field[3] == Card.VELITES);
        assert(field[6] == Card.PRAETORIANUS);

        hand = gameState.getPlayerHand(0);
        assert(!hand.contains(Card.VELITES));
        assert(!hand.contains(Card.PRAETORIANUS));

        int[] dice = gameState.getActionDice();
        assert(dice.length == 2);

        s = (SenatorActivator)move.chooseCardToActivate(3);
        s.layCard(Card.SICARIUS, 6);
        // s.layCard(Card.BASILICA, 4); Invalid. Removed. Can't lay buildings.
        s.complete();

        assert(gameState.getPlayerVictoryPoints(0) == 10);
        assert(gameState.getPlayerVictoryPoints(1) == 10);
        assert(gameState.getPlayerSestertii(0) == 0);
        assert(gameState.getPlayerSestertii(1) == 0);

        field = gameState.getPlayerCardsOnDiscs(0);
        assert(field[2] == Card.SENATOR);
        assert(field[3] == Card.VELITES);
        assert(field[5] == Card.SICARIUS);
        assert(field[6] == Card.PRAETORIANUS);

        hand = gameState.getPlayerHand(0);
        assert(!hand.contains(Card.SICARIUS));
        assert(hand.contains(Card.BASILICA));

    }
}
