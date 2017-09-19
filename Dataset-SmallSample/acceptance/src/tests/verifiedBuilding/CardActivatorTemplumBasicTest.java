package tests.verifiedBuilding;

import java.util.ArrayList;
import java.util.List;

import framework.Rules;
import framework.Test;
import framework.cards.Card;
import framework.interfaces.GameState;
import framework.interfaces.MoveMaker;
import framework.interfaces.activators.ForumActivator;

/**
 *
 * Test the basic functionality of Templum
 *
 * @author Karla Burnett
 * @author Junjie CHEN
 *
 */

public class CardActivatorTemplumBasicTest extends Test {

    @Override
    public String getShortDescription() {
        return "Test the basic functionality of Templum";
    }

    @Override
    public void run(GameState gameState, MoveMaker move) throws AssertionError,
            UnsupportedOperationException, IllegalArgumentException {
      gameState.setWhoseTurn(0);
      if (1==1) {
         throw new IllegalArgumentException();
      }        
      
      // Have all dice discs on the first player's side empty, and all dice
      // discs on the second player's side full of Templums
      Card[] discs = new Card[Rules.NUM_DICE_DISCS];
      for (int i = 0; i < Rules.NUM_DICE_DISCS; i++) {
         discs[i] = Card.NOT_A_CARD;
      }
      gameState.setPlayerCardsOnDiscs(0, discs);
      
      for (int i = 0; i < Rules.NUM_DICE_DISCS; i++) {
         discs[i] = Card.TEMPLUM;
      }
      gameState.setPlayerCardsOnDiscs(1, discs);
      
      // Initialise both players with 100 sestertii and 15 VP
      gameState.setPlayerSestertii(0, 100);
      gameState.setPlayerSestertii(1, 100);
      gameState.setPlayerVictoryPoints(0, 15);
      gameState.setPlayerVictoryPoints(1, 15);
      
      List<Card> hand = new ArrayList<Card>();
      hand.add(Card.FORUM);
      hand.add(Card.FORUM);
      hand.add(Card.FORUM);
      hand.add(Card.TEMPLUM);
      hand.add(Card.TEMPLUM);
      gameState.setPlayerHand(0, hand);
      
      // ---- NO TEMPLUM ----
      
      // Lay a forum on the first dice disc and check that sestertii and hand
      // update correctly
      move.placeCard(Card.FORUM, Rules.DICE_DISC_1);
      assert(gameState.getPlayerSestertii(0) == 100 - 5);
      assert(gameState.getPlayerSestertii(1) == 100);
      assert(gameState.getPlayerVictoryPoints(0) == 15);
      assert(gameState.getPlayerVictoryPoints(1) == 15);
      assert(gameState.getPlayerHand(0).size() == 4);
      
      // Activate the Forum with a 5
      gameState.setActionDice(new int[] {1, 5, 1});
      ForumActivator activator = (ForumActivator) move.chooseCardToActivate(1);
      activator.chooseActivateTemplum(false);
      activator.chooseActionDice(5);
      activator.complete();
      
      // Check that the correct number of VP are added
      assert(gameState.getPlayerVictoryPoints(0) == 15 + 5);
      assert(gameState.getPlayerVictoryPoints(1) == 15);
      
      // Start a new turn for this player (checking that they have lost the 
      // correct number of VP).
      // The first player loses 6 VP because 6 of their card slots are empty,
      // the second player loses none
      move.endTurn();
      move.endTurn();
      assert(gameState.getPlayerVictoryPoints(0) == 15 + 5 - 6);
      assert(gameState.getPlayerVictoryPoints(1) == 15);
      
      // ---- TEMPLUM TO THE LEFT ----
      
      // Set the action dice for this turn
      gameState.setActionDice(new int[] {2, 5, 1});
      
      // Lay a Templum over the existing Forum and lay a Forum on dice disc 2
      move.placeCard(Card.TEMPLUM, Rules.DICE_DISC_1);
      move.placeCard(Card.FORUM, Rules.DICE_DISC_2);
      
      // Check that the correct number of sestertii were lost
      assert(gameState.getPlayerSestertii(0) == 100 - 5 - 5 - 2);
      assert(gameState.getPlayerSestertii(1) == 100);
      assert(gameState.getPlayerVictoryPoints(0) == 15 + 5 - 6);
      assert(gameState.getPlayerVictoryPoints(1) == 15);
      assert(gameState.getPlayerHand(0).size() == 2);
      
      // Activate the forum with the 6
      activator = (ForumActivator) move.chooseCardToActivate(2);
      activator.chooseActivateTemplum(true);
      activator.chooseActivateTemplum(1);
      activator.chooseActionDice(5);
      activator.complete();
      
      // Check that the correct number of VPs were gained (5 for the Forum,
      // 1 for the Templum) 
      assert(gameState.getPlayerVictoryPoints(0) == 14 + 5 + 1);
      assert(gameState.getPlayerVictoryPoints(1) == 15);
      
      // Start a new turn for this player (checking that they have lost the 
      // correct number of VP)
      // This time the first player only loses 5 VP because of the Templum
      // on disc 2
      move.endTurn();
      move.endTurn();
      assert(gameState.getPlayerVictoryPoints(0) == 20 - 5);
      assert(gameState.getPlayerVictoryPoints(1) == 15);
      
      // ---- TEMPLUMS ON BOTH SIDES -----
      
      // Set the action dice for this turn
      gameState.setActionDice(new int[] {2, 1, 3});
      
      // Lay a Templum on dice disc 3
      move.placeCard(Card.TEMPLUM, Rules.DICE_DISC_3);
      
      // Check that the correct number of sestertii were lost
      assert(gameState.getPlayerSestertii(0) == 100 - 5 - 5 - 2 - 2);
      assert(gameState.getPlayerSestertii(1) == 100);
      assert(gameState.getPlayerVictoryPoints(0) == 20 - 5);
      assert(gameState.getPlayerVictoryPoints(1) == 15);
      assert(gameState.getPlayerHand(0).size() == 1);
      
      // Activate the forum with the 1
      activator = (ForumActivator) move.chooseCardToActivate(2);
      activator.chooseActivateTemplum(true);
      activator.chooseActivateTemplum(3);
      activator.chooseActionDice(1);
      activator.complete();
      
      // Check that the correct number of VPs were gained (1 for the Forum,
      // 3 for the Templum)
      assert(gameState.getPlayerVictoryPoints(0) == 15 + 1 + 3);
      assert(gameState.getPlayerVictoryPoints(1) == 15);
      
      // Start a new turn for this player (checking that they have lost the 
      // correct number of VP)
      // This time only 4 VP are lost by player 1
      move.endTurn();
      move.endTurn();
      assert(gameState.getPlayerVictoryPoints(0) == 19 - 4);
      assert(gameState.getPlayerVictoryPoints(1) == 15);
      
      // ---- TEMPLUM ON THE RIGHT ----
      
      // Set the action dice for this turn
      gameState.setActionDice(new int[] {2, 4, 2});
      
      // Lay a Forum over the Templum on dice disc 1
      move.placeCard(Card.FORUM, Rules.DICE_DISC_1);
      
      // Check that the correct number of sestertii were lost
      assert(gameState.getPlayerSestertii(0) == 100 - 5 - 5 - 2 - 2 - 5);
      assert(gameState.getPlayerSestertii(1) == 100);
      assert(gameState.getPlayerVictoryPoints(0) == 19 - 4);
      assert(gameState.getPlayerVictoryPoints(1) == 15);
      assert(gameState.getPlayerHand(0).size() == 0);
      
      // Activate the forum with the 4
      activator = (ForumActivator) move.chooseCardToActivate(2);
      activator.chooseActivateTemplum(true);
      activator.chooseActivateTemplum(2);
      activator.chooseActionDice(4);
      activator.complete();
      
      // Check that the correct number of VPs were gained (4 for the Forum,
      // 2 for the Templum)
      assert(gameState.getPlayerVictoryPoints(0) == 15 + 4 + 2);
      assert(gameState.getPlayerVictoryPoints(1) == 15);
    }
}
