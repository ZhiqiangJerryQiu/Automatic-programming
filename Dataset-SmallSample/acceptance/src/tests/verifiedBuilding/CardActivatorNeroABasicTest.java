package tests.verifiedBuilding;

import java.util.ArrayList;
import java.util.Collection;

import framework.Test;
import framework.Rules;
import framework.cards.Card;
import framework.interfaces.GameState;
import framework.interfaces.MoveMaker;
import framework.interfaces.activators.NeroActivator;

/**
 * 
 * Test the functionality of Nero
 * 
 * @author Karla Burnett
 *
 */

public class CardActivatorNeroABasicTest extends Test {

    @Override
    public String getShortDescription() {
        return "Test the basic functionality of Nero";
    }

    @Override
    public void run(GameState gameState, MoveMaker move) throws AssertionError,
            UnsupportedOperationException, IllegalArgumentException {
        
        // Set up the game state with the board as follows:
        //                [1]                //
        // Nero           [2] Forum          //
        // Nero           [3] Templum        //
        //                [4]                //
        //                [5]                //
        //                [6]                //
        //                [7]                //
        gameState.setWhoseTurn(0);
        
        Card [] fieldPlayer0 = new Card[Rules.NUM_DICE_DISCS];
        Card [] fieldPlayer1 = new Card[Rules.NUM_DICE_DISCS];
        for (int i = 0; i < Rules.NUM_DICE_DISCS; i++) {
           fieldPlayer0[i] = Card.NOT_A_CARD;
           fieldPlayer1[i] = Card.NOT_A_CARD;
        }
        
        fieldPlayer0[1] = Card.NERO;
        fieldPlayer0[2] = Card.NERO;
        fieldPlayer1[1] = Card.FORUM;
        fieldPlayer1[2] = Card.TEMPLUM;
        
        gameState.setPlayerCardsOnDiscs(0, fieldPlayer0);
        gameState.setPlayerCardsOnDiscs(1, fieldPlayer1);
        gameState.setPlayerVictoryPoints(0, 18);
        gameState.setPlayerVictoryPoints(1, 17);
        
        // Set up the game state so that a Nero can be laid
        Collection<Card> hand = new ArrayList<Card>();
        hand.add(Card.NERO);
        gameState.setPlayerHand(0, hand);
        
        gameState.setPlayerSestertii(0, 10);
        gameState.setPlayerSestertii(1, 5);
        
        // Lay a Nero on disc 5 and check that the game state changes accordingly
        move.placeCard(Card.NERO, Rules.DICE_DISC_5);
        
        assert(gameState.getPlayerSestertii(0) == 2);
        assert(gameState.getPlayerSestertii(1) == 5);
        assert(gameState.getPlayerHand(0).size() == 0);
        
        fieldPlayer0 = gameState.getPlayerCardsOnDiscs(0);
        fieldPlayer1 = gameState.getPlayerCardsOnDiscs(1);
        
        for (int i = 0; i < fieldPlayer0.length; i++) {
           if (i == 1) {
              assert(fieldPlayer0[i] == Card.NERO);
              assert(fieldPlayer1[i] == Card.FORUM);
           } else if (i == 2) {
              assert(fieldPlayer0[i] == Card.NERO);
              assert(fieldPlayer1[i] == Card.TEMPLUM);
           } else if (i == 4) {
              assert(fieldPlayer0[i] == Card.NERO);
              assert(fieldPlayer1[i] == Card.NOT_A_CARD);
           } else {
              assert(fieldPlayer0[i] == Card.NOT_A_CARD);
              assert(fieldPlayer1[i] == Card.NOT_A_CARD);
           }
        }
        
        move.endTurn();
        move.endTurn();
        // ---------------------------------------------------------------------
        
        // Set up the action dice such that the first two Neros can be activated
        gameState.setActionDice(new int[] {2,3,3});

        // Activate the Nero on dice disc 2, attacking the Templum on disc 3
        NeroActivator activator = (NeroActivator) move.chooseCardToActivate(Rules.DICE_DISC_2);
        activator.chooseDiceDisc(Rules.DICE_DISC_3);
        activator.complete();
        
        // Check that the game state is correct after this
        assert(gameState.getPlayerSestertii(0) == 2);
        assert(gameState.getPlayerSestertii(1) == 5);
        assert(gameState.getPlayerHand(0).size() == 0);
        
        fieldPlayer0 = gameState.getPlayerCardsOnDiscs(0);
        fieldPlayer1 = gameState.getPlayerCardsOnDiscs(1);
        
        for (int i = 0; i < fieldPlayer0.length; i++) {
           if (i == 1) {
              assert(fieldPlayer0[i] == Card.NOT_A_CARD);
              assert(fieldPlayer1[i] == Card.FORUM);
           } else if (i == 2) {
              assert(fieldPlayer0[i] == Card.NERO);
              assert(fieldPlayer1[i] == Card.NOT_A_CARD);
           } else if (i == 4) {
              assert(fieldPlayer0[i] == Card.NERO);
              assert(fieldPlayer1[i] == Card.NOT_A_CARD);
           } else {
              assert(fieldPlayer0[i] == Card.NOT_A_CARD);
              assert(fieldPlayer1[i] == Card.NOT_A_CARD);
           }
        }
        
        // Now activate the other Nero, this time targetting the Forum
        activator = (NeroActivator) move.chooseCardToActivate(Rules.DICE_DISC_3);
        activator.chooseDiceDisc(Rules.DICE_DISC_2);
        activator.complete();
        
        // Check that the game state is correct after this
        assert(gameState.getPlayerSestertii(0) == 2);
        assert(gameState.getPlayerSestertii(1) == 5);
        assert(gameState.getPlayerHand(0).size() == 0);
        
        fieldPlayer0 = gameState.getPlayerCardsOnDiscs(0);
        fieldPlayer1 = gameState.getPlayerCardsOnDiscs(1);
        
        for (int i = 0; i < fieldPlayer0.length; i++) {
           if (i == 1) {
              assert(fieldPlayer0[i] == Card.NOT_A_CARD);
              assert(fieldPlayer1[i] == Card.NOT_A_CARD);
           } else if (i == 2) {
              assert(fieldPlayer0[i] == Card.NOT_A_CARD);
              assert(fieldPlayer1[i] == Card.NOT_A_CARD);
           } else if (i == 4) {
              assert(fieldPlayer0[i] == Card.NERO);
              assert(fieldPlayer1[i] == Card.NOT_A_CARD);
           } else {
              assert(fieldPlayer0[i] == Card.NOT_A_CARD);
              assert(fieldPlayer1[i] == Card.NOT_A_CARD);
           }
        }
    }
}
