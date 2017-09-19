package tests.verifiedSpec2;

import java.util.List;
import java.util.LinkedList;

import framework.Test;
import framework.Rules;
import framework.cards.Card;
import framework.interfaces.GameState;
import framework.interfaces.MoveMaker;
import framework.interfaces.activators.*;
/**
 *
 * Test the basic functionality of Kat and Grim Reaper
 *
 * @author Damon Stacey
 * @author Troy Lenger
 * @author Sam Ipsen
 *
 */

public class CardActivatorKatAndGrimReaperTest extends Test {

	//////////////////////////////////////////////////
	//////////////////////////////////////////////////
	///// Set 'debug' to TRUE for debug printing /////
	//////////////////////////////////////////////////
	//////////////////////////////////////////////////
	boolean debug = false;
	
	private void db (String input) {
		if (debug) {
			System.out.println(input);
		}
	}
	
    @Override
    public String getShortDescription() {
        return "Test the functionality of Kat and Grim Reaper";
    }

    @Override
    public void run(GameState gameState, MoveMaker move) throws AssertionError,
            UnsupportedOperationException, IllegalArgumentException {
      if (1==1) {
         throw new IllegalArgumentException();
      }        

      
      List<Card> deck = new LinkedList<Card>();
      gameState.setDiscard(deck);

      Card[] discs = new Card[Rules.NUM_DICE_DISCS];
      for (int i = 0; i < Rules.NUM_DICE_DISCS; i++) {
         discs[i] = Card.NOT_A_CARD;
      }
      for (int i = 0; i < Rules.NUM_PLAYERS; i++) {
         gameState.setPlayerCardsOnDiscs(i, discs);
      }
      List<Card> discard = new LinkedList<Card>();
      discard.add(Card.AESCULAPINUM);
      discard.add(Card.BASILICA);
      discard.add(Card.CONSILIARIUS);
      discard.add(Card.CONSUL);
      discard.add(Card.ESSEDUM);
      discard.add(Card.FORUM);
      discard.add(Card.GLADIATOR);
      discard.add(Card.HARUSPEX);
      discard.add(Card.LEGAT);
      discard.add(Card.LEGIONARIUS);
      gameState.setDiscard(discard);
      discard = gameState.getDiscard();
      
      // PLAYER 0
      gameState.setWhoseTurn(0);
      List<Card> hand;
      for (int i = 0; i < Rules.NUM_PLAYERS; i++) {
         gameState.setPlayerSestertii(i, 100);
         gameState.setPlayerVictoryPoints(i, 15);
         hand = new LinkedList<Card>();
         hand.add(Card.KAT);
         hand.add(Card.CENTURIO);
         hand.add(Card.ARCHITECTUS);
         hand.add(Card.GRIMREAPER);
         gameState.setPlayerHand(i, hand);
      }

      gameState.setActionDice(new int[] {3,2,3});
      
      move.placeCard(Card.CENTURIO, Rules.DICE_DISC_2);
      
      db("\n\tASSERT: gameState.getPlayerSestertii(1) = " + gameState.getPlayerSestertii(1) + " (Should be 100)");
      assert(gameState.getPlayerSestertii(1) == 100);
      
      db("\tASSERT: gameState.getPlayerHand(0).contains(Card.ARCHITECTUS) = " 
    		  + gameState.getPlayerHand(0).contains(Card.ARCHITECTUS) + " (Should be TRUE)");
      assert(gameState.getPlayerHand(0).contains(Card.ARCHITECTUS));
      
      Card[] field;
      field = gameState.getPlayerCardsOnDiscs(0);
      
      db("\tASSERT: field[1].toString() = " + field[1].toString() + " (Should be CENTURIO)");
      assert(field[1] == Card.CENTURIO);
      
      db("\tASSERT: gameState.getPoolVictoryPoints() = " + gameState.getPoolVictoryPoints() + " (Should be 6)");
      assert(gameState.getPoolVictoryPoints() == Rules.GAME_VICTORY_POINTS - 15*Rules.NUM_PLAYERS);
      
      db("\tASSERT: gameState.isGameCompleted() = " + gameState.isGameCompleted() + " (Should be FALSE)");
      assert(!gameState.isGameCompleted());

      gameState.setActionDice(new int[] {6,2,5});
      move.endTurn();
      
      // PLAYER 1
      move.placeCard(Card.KAT, Rules.DICE_DISC_2);
      db("\n- Player 0 placed their KAT on DISC 2");
      move.placeCard(Card.GRIMREAPER, Rules.DICE_DISC_3);
      db("- Player 0 placed their GRIM REAPER on DISC 3\n");
      
      gameState.setActionDice(new int[] {3,2,3});
      
      move.endTurn();
      
      // PLAYER 0
      int KAT_NUM_LIVES = 9;
      
      // Attack player 1's KAT 9 times
      db("- Player 0 attacks player 1's KAT 9 times...");
      for (int i=0; i<KAT_NUM_LIVES; i++) {
    	  
    	  gameState.setActionDice(new int[] {2,2,2});
    	  
          field = gameState.getPlayerCardsOnDiscs(1);
          assert(field[1] == Card.KAT);

          CenturioActivator activator = (CenturioActivator) move.chooseCardToActivate(2);
          activator.giveAttackDieRoll(6);
          activator.chooseCenturioAddActionDie(false);
          activator.complete();
          
          db("\t--> Player 1's KAT should have " + (8-i) + " lives remaining");
      }
      
      db("\n- Player 1's KAT should now be killed and sent back to their HAND\n");
      
      field = gameState.getPlayerCardsOnDiscs(1);
      db("\tASSERT: field[1].toString() = " + field[1].toString() + " (Should have NOT_A_CARD)");
      assert(field[1] == Card.NOT_A_CARD);
      
      // KAT should be sent to HAND
      db("\tASSERT: gameState.getDiscard().contains(Card.KAT) = " + gameState.getDiscard().contains(Card.KAT) + " (Should be FALSE)");
      assert(!gameState.getDiscard().contains(Card.KAT));		
      
      db("\tASSERT: gameState.getPlayerHand(1).contains(Card.KAT) = " + gameState.getPlayerHand(1).contains(Card.KAT) + " (Should be TRUE)");
      assert(gameState.getPlayerHand(1).contains(Card.KAT));
      
      move.endTurn();
      
      // PLAYER 1
      
      // Place the KAT back on the board and remove the grim reaper
      move.placeCard(Card.KAT, Rules.DICE_DISC_2);
      db("\n- Player 1 placed their KAT on DISC 2");
      move.placeCard(Card.ARCHITECTUS, Rules.DICE_DISC_3);
      db("- Player 1 placed their ARCHITECTUS on DISC 3\n");
      
      field = gameState.getPlayerCardsOnDiscs(1);
      db("\tASSERT: field[1].toString() = " + field[1].toString() + " (Should have KAT)");
      assert(field[1] == Card.KAT);
      
      db("\tASSERT: field[2].toString() = " + field[2].toString() + " (Should have ARCHITECTUS)");
      assert(field[2] == Card.ARCHITECTUS);
      
      db("\tASSERT: gameState.getDiscard().contains(Card.GRIMREAPER) = " + gameState.getDiscard().contains(Card.GRIMREAPER) + " (Should be TRUE)");
      assert(gameState.getDiscard().contains(Card.GRIMREAPER));
      
      move.endTurn();
      
      // PLAYER 0
      
      // Attack player 1's KAT 9 times
      db("\n- Player 0 attacks player 1's KAT 9 times...");
      for (int i=0; i<KAT_NUM_LIVES; i++) {
    	  
    	  gameState.setActionDice(new int[] {2,2,2});
    	  
          field = gameState.getPlayerCardsOnDiscs(1);
          assert(field[1] == Card.KAT);

          CenturioActivator activator = (CenturioActivator) move.chooseCardToActivate(2);
          activator.giveAttackDieRoll(6);
          activator.chooseCenturioAddActionDie(false);
          activator.complete();
          
          db("\t--> Player 1's KAT should have " + (8-i) + " lives remaining");
      }
      
      db("\n- Player 1's KAT should now be killed and sent back to the GRAVEYARD\n");
      
      field = gameState.getPlayerCardsOnDiscs(1);
      db("\tASSERT: field[1].toString() = " + field[1].toString() + " (Should have NOT_A_CARD)");
      assert(field[1] == Card.NOT_A_CARD);
      
      // KAT should be sent to GRAVEYARD
      db("\tASSERT: gameState.getDiscard().contains(Card.KAT) = " + gameState.getDiscard().contains(Card.KAT) + " (Should be TRUE)");
      assert(gameState.getDiscard().contains(Card.KAT));		
      
      db("\tASSERT: gameState.getPlayerHand(1).contains(Card.KAT) = " + gameState.getPlayerHand(1).contains(Card.KAT) + " (Should be FALSE)");
      for (int i = 0; i < gameState.getPlayerHand(1).size(); i++) 
         System.out.println(((List)gameState.getPlayerHand(1)).get(i));
         
      assert(!gameState.getPlayerHand(1).contains(Card.KAT));
      
      
    }
}
