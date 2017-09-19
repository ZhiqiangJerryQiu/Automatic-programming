package tests.verifiedChar;

import java.util.LinkedList;

import framework.Test;
import framework.Rules;
import framework.cards.Card;
import framework.interfaces.GameState;
import framework.interfaces.MoveMaker;
import framework.interfaces.activators.VelitesActivator;

/**
 *
 * Test the basic functionality of Velites
 *
 * @author Calvin Tam
 * @author Nicholas Higgins
 *
 */

public class CardActivatorBVelitesBasicTest extends Test {

    @Override
    public String getShortDescription() {
        return "Testing the basic mechanics of Velites";
    }

    @Override
    public void run(GameState gameState, MoveMaker move) throws AssertionError,
            UnsupportedOperationException, IllegalArgumentException {
    	
    	      if (1==1) {
         throw new IllegalArgumentException();
      }        

    	// This test is assuming there are only 2 players
      Card[] playerField_0 = {
    		  Card.NOT_A_CARD,
    		  Card.CENTURIO,
    		  Card.CENTURIO,
    		  Card.MERCATOR,
    		  Card.ARCHITECTUS,
    		  Card.NERO,
    		  Card.HARUSPEX
      };
      gameState.setPlayerCardsOnDiscs(0, playerField_0);

      Card[] playerField_1 = {
    		  Card.LEGAT,
    		  Card.SICARIUS,
    		  Card.LEGIONARIUS,
    		  Card.CONSUL,
    		  Card.ARCHITECTUS,
    		  Card.GLADIATOR,
    		  Card.HARUSPEX
      };
      gameState.setPlayerCardsOnDiscs(1, playerField_1);

      gameState.setPlayerSestertii(0, 50);
      gameState.setPlayerSestertii(1, 50);
      gameState.setPlayerVictoryPoints(0, 10);
      gameState.setPlayerVictoryPoints(1, 10);

      gameState.setWhoseTurn(0);

      LinkedList<Card>hand = new LinkedList<Card>();
      hand.add(Card.VELITES);
      gameState.setPlayerHand(0, hand);

      hand = new LinkedList<Card>();
      hand.add(Card.SICARIUS);
      gameState.setPlayerHand(1, hand);

      // Player 0's move
      gameState.setActionDice(new int[] {1,2,3});
      move.placeCard(Card.VELITES, Rules.DICE_DISC_1);

      assert(gameState.getPlayerHand(0).size() == 0);

      Card[] temp;
      temp = gameState.getPlayerCardsOnDiscs(0);
      assert(temp[0] == Card.VELITES);

      assert(!gameState.isGameCompleted());

      move.endTurn();

      //Player 1's move
      move.placeCard(Card.SICARIUS, Rules.DICE_DISC_2);

      temp = gameState.getPlayerCardsOnDiscs(Rules.DICE_DISC_1);
      assert(temp[1] == Card.SICARIUS);

      move.endTurn();

      //Player 0's move
      gameState.setActionDice(new int[] {1,2,1});

      //Testing that the attack did not defeat Sicarius
      VelitesActivator activator = (VelitesActivator) move.chooseCardToActivate(Rules.DICE_DISC_1);
      activator.giveAttackDieRoll(1);
      activator.chooseDiceDisc(Rules.DICE_DISC_2);
      activator.complete();
      temp = gameState.getPlayerCardsOnDiscs(1);
      assert(temp[1] == Card.SICARIUS);

      //Testing the corner case when the attack value is the same as defence value
      activator = (VelitesActivator) move.chooseCardToActivate(Rules.DICE_DISC_1);
      activator.giveAttackDieRoll(2);
      activator.chooseDiceDisc(Rules.DICE_DISC_2);
      activator.complete();
      temp = gameState.getPlayerCardsOnDiscs(1);
      assert(temp[1] == Card.NOT_A_CARD);
      assert(!gameState.isGameCompleted());

      move.endTurn();


      //Lay more cards down
      //Player 1's turn
      hand = new LinkedList<Card>();
      hand.add(Card.ARCHITECTUS);
      hand.add(Card.BASILICA);
      gameState.setPlayerHand(1, hand);
      move.placeCard(Card.ARCHITECTUS, Rules.BRIBE_DISC);
      move.placeCard(Card.BASILICA, Rules.DICE_DISC_3);
      temp = gameState.getPlayerCardsOnDiscs(1);
      assert(temp[2] == Card.BASILICA);
      assert(temp[6] == Card.ARCHITECTUS);
      move.endTurn();

      //Player 0's turn -  and now test that when attack value is greater
      //The card is also defeated
      gameState.setActionDice(new int[] {1,1,1});
      activator = (VelitesActivator)move.chooseCardToActivate(Rules.DICE_DISC_1);
      activator.giveAttackDieRoll(6);
      activator.chooseDiceDisc(Rules.BRIBE_DISC);
      activator.complete();
      temp = gameState.getPlayerCardsOnDiscs(1);
      assert(temp[2] == Card.BASILICA);
      assert(temp[6] == Card.NOT_A_CARD);

      // Making sure that Velites cannot attack a building
      // Invalid - testing this like this isn't correct
      //activator = (VelitesActivator)move.chooseCardToActivate(Rules.DICE_DISC_1);
      //activator.giveAttackDieRoll(6);
      //activator.chooseDiceDisc(Rules.DICE_DISC_3);
      //activator.complete();
      temp = gameState.getPlayerCardsOnDiscs(1);
      assert(temp[2] == Card.BASILICA);
    }
}
