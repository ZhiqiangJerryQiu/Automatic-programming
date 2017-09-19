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
 * Test the basic functionality of Basilica
 *
 * @author Karla Burnett
 *
 */

public class CardActivatorBasilicaABasicTest extends Test {

    @Override
    public String getShortDescription() {
        return "Test the basic functionality of Basilica";
    }

    @Override
    public void run(GameState gameState, MoveMaker move) throws AssertionError,
            UnsupportedOperationException, IllegalArgumentException {
      gameState.setWhoseTurn(0);

      // Have all dice discs on the first player's side empty, and all dice
      // discs on the second player's side full of Basilicas
      Card[] discs = new Card[Rules.NUM_DICE_DISCS];
      for (int i = 0; i < Rules.NUM_DICE_DISCS; i++) {
         discs[i] = Card.NOT_A_CARD;
      }
      gameState.setPlayerCardsOnDiscs(0, discs);

      for (int i = 0; i < Rules.NUM_DICE_DISCS; i++) {
         discs[i] = Card.BASILICA;
      }
      gameState.setPlayerCardsOnDiscs(1, discs);

      // Initialise both players with 100 sestertii and 15 VP
      gameState.setPlayerSestertii(0, 100);
      gameState.setPlayerSestertii(1, 100);
      gameState.setPlayerVictoryPoints(0, 10);
      gameState.setPlayerVictoryPoints(1, 10);

      List<Card> hand = new ArrayList<Card>();
      hand.add(Card.FORUM);
      hand.add(Card.FORUM);
      hand.add(Card.FORUM);
      hand.add(Card.BASILICA);
      hand.add(Card.BASILICA);
      gameState.setPlayerHand(0, hand);

      // ---- NO BASILICA ----

      // Lay a forum on the first dice disc and check that sestertii and hand
      // update correctly
      gameState.setActionDice(new int[] {1, 5, 1});

      move.placeCard(Card.FORUM, Rules.DICE_DISC_1);
      assert(gameState.getPlayerSestertii(0) == 100 - 5);
      assert(gameState.getPlayerSestertii(1) == 100);
      assert(gameState.getPlayerVictoryPoints(0) == 10);
      assert(gameState.getPlayerVictoryPoints(1) == 10);
      assert(gameState.getPlayerHand(0).size() == 4);

      // Activate the Forum with a 5
      ForumActivator activator = (ForumActivator) move.chooseCardToActivate(1);
      activator.chooseActionDice(5);
      activator.complete();

      // Check that the correct number of VP are added
      assert(gameState.getPlayerVictoryPoints(0) == 10 + 5);
      assert(gameState.getPlayerVictoryPoints(1) == 10);

      // Start a new turn for this player (checking that they have lost the
      // correct number of VP).
      // The first player loses 6 VP because 6 of their card slots are empty,
      // the second player loses none
      move.endTurn();
      move.endTurn();
      assert(gameState.getPlayerVictoryPoints(0) == 10 + 5 - 6);
      assert(gameState.getPlayerVictoryPoints(1) == 10);

      // ---- BASILICA TO THE LEFT ----

      // Set the action dice for this turn
      gameState.setActionDice(new int[] {2, 6, 1});

      // Lay a Basilica over the existing Forum and lay a Forum on dice disc 2
      move.placeCard(Card.BASILICA, Rules.DICE_DISC_1);
      move.placeCard(Card.FORUM, Rules.DICE_DISC_2);

      // Check that the correct number of sestertii were lost
      assert(gameState.getPlayerSestertii(0) == 100 - 5 - 5 - 6);
      assert(gameState.getPlayerSestertii(1) == 100);
      assert(gameState.getPlayerHand(0).size() == 2);

      // Reset the victory points
      assert(gameState.getPlayerVictoryPoints(0) == 9);
      assert(gameState.getPlayerVictoryPoints(1) == 10);

      // Activate the forum with the 6
      activator = (ForumActivator) move.chooseCardToActivate(2);
      activator.chooseActionDice(6);
      activator.chooseActivateTemplum(false);
      activator.complete();

      // Check that the correct number of VPs were gained (6 for the Forum,
      // 2 for the Basilica)
      assert(gameState.getPlayerVictoryPoints(0) == 9 + 6 + 2);
      assert(gameState.getPlayerVictoryPoints(1) == 10);

      // Start a new turn for this player (checking that they have lost the
      // correct number of VP)
      // This time the first player only loses 5 VP because of the Basilica
      // on disc 2
      move.endTurn();
      move.endTurn();
      assert(gameState.getPlayerVictoryPoints(0) == 17 - 5);
      assert(gameState.getPlayerVictoryPoints(1) == 10);

      // ---- BASILICAS ON BOTH SIDES -----

      // Set the action dice for this turn
      gameState.setActionDice(new int[] {2, 1, 1});

      // Lay a Basilica on dice disc 3
      move.placeCard(Card.BASILICA, Rules.DICE_DISC_3);

      // Check that the correct number of sestertii were lost
      assert(gameState.getPlayerSestertii(0) == 100 - 5 - 5 - 6 - 6);
      assert(gameState.getPlayerSestertii(1) == 100);
      assert(gameState.getPlayerVictoryPoints(0) == 17 - 5);
      assert(gameState.getPlayerVictoryPoints(1) == 10);
      assert(gameState.getPlayerHand(0).size() == 1);

      // Activate the forum with the 1
      activator = (ForumActivator) move.chooseCardToActivate(2);
      activator.chooseActionDice(1);
      activator.complete();

      // Check that the correct number of VPs were gained (1 for the Forum,
      // 2 for each Basilica)
      assert(gameState.getPlayerVictoryPoints(0) == 12 + 1 + 2 + 2);
      assert(gameState.getPlayerVictoryPoints(1) == 10);

      // Start a new turn for this player (checking that they have lost the
      // correct number of VP)
      // This time only 4 VP are lost by player 1
      move.endTurn();
      move.endTurn();
      assert(gameState.getPlayerVictoryPoints(0) == 17 - 4);
      assert(gameState.getPlayerVictoryPoints(1) == 10);

      // ---- BASILICA ON THE RIGHT ----

      // Set the action dice for this turn
      gameState.setActionDice(new int[] {2, 4, 1});

      // Lay a Forum over the Basilica on dice disc 1
      move.placeCard(Card.FORUM, Rules.DICE_DISC_1);

      // Check that the correct number of sestertii were lost
      assert(gameState.getPlayerSestertii(0) == 100 - 5 - 5 - 6 - 6 - 5);
      assert(gameState.getPlayerSestertii(1) == 100);
      assert(gameState.getPlayerVictoryPoints(0) == 17 - 4);
      assert(gameState.getPlayerVictoryPoints(1) == 10);
      assert(gameState.getPlayerHand(0).size() == 0);

      // Activate the forum with the 4
      activator = (ForumActivator) move.chooseCardToActivate(2);
      activator.chooseActionDice(4);
      activator.complete();

      // Check that the correct number of VPs were gained (4 for the Forum,
      // 2 for each Basilica)
      assert(gameState.getPlayerVictoryPoints(0) == 13 + 4 + 2);
      assert(gameState.getPlayerVictoryPoints(1) == 10);

      // Start a new turn for this player (checking that they have lost the
      // correct number of VP)
      // This time only 4 VP are lost by player 1
      move.endTurn();
      move.endTurn();
      assert(gameState.getPlayerVictoryPoints(0) == 19 - 4);
      assert(gameState.getPlayerVictoryPoints(1) == 10);

      // ---- BASILICA ON THE FIELD, NOT ADJACENT TO FORUM ----

      // Set the action dice for this turn
      gameState.setActionDice(new int[] {1, 2, 1});

      // Activate the forum on disc 1 with the 2
      activator = (ForumActivator) move.chooseCardToActivate(1);
      activator.chooseActionDice(2);
      activator.complete();

      // Check that the correct number of VPs were gained (2 for the Forum)
      assert(gameState.getPlayerVictoryPoints(0) == 15 + 2);
      assert(gameState.getPlayerVictoryPoints(1) == 10);
    }
}
