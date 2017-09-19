package tests.verifiedBuilding;

import java.util.LinkedList;
import java.util.List;

import framework.Rules;
import framework.Test;
import framework.cards.Card;
import framework.interfaces.GameState;
import framework.interfaces.MoveMaker;
import framework.interfaces.activators.MercatorActivator;

/**
 *
 * Test the basic functionality of Aesculapinum
 *
 * @author Damon Stacey
 *
 */

public class CardActivatorMercatorABasicTest extends Test {

    @Override
    public String getShortDescription() {
        return "Test the basic functionality of Mercator";
    }

    @Override
    public void run(GameState gameState, MoveMaker move) throws AssertionError,
            UnsupportedOperationException, IllegalArgumentException {
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
      discard.add(Card.CENTURIO);
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
      
      gameState.setWhoseTurn(0);
      List<Card> hand;
      for (int i = 0; i < Rules.NUM_PLAYERS; i++) {
         gameState.setPlayerSestertii(i, 100);
         gameState.setPlayerVictoryPoints(i, 2);
         hand = new LinkedList<Card>();
         hand.add(Card.MERCATOR);
         hand.add(Card.ARCHITECTUS);
         gameState.setPlayerHand(i, hand);
      }
      
      gameState.setPlayerVictoryPoints(1, 6);
      
      gameState.setActionDice(new int[] {1,1,1});

      move.placeCard(Card.MERCATOR, Rules.DICE_DISC_1);

        //the correct amount of money should be deducted
        //game removed from hand and get placed on the field
      assert(gameState.getPlayerHand(0).size() == 1);
      assert(!gameState.getPlayerHand(0).contains(Card.MERCATOR));
      Card[] field;
      field = gameState.getPlayerCardsOnDiscs(0);
      assert(field[0] == Card.MERCATOR);
      
      assert(gameState.getPoolVictoryPoints() == 36 - 2 - 6);
      assert(!gameState.isGameCompleted());
      
      MercatorActivator activator = (MercatorActivator) move.chooseCardToActivate(Rules.DICE_DISC_1);
 
      activator.chooseMercatorBuyNum(1);
      activator.complete();
      assert(gameState.getPlayerVictoryPoints(0) == 2 + 1);
      assert(gameState.getPlayerSestertii(0) == 100 - 2*1 - 7);
      assert(gameState.getPlayerVictoryPoints(1) == 6 - 1);
      assert(gameState.getPlayerSestertii(1) == 100 + 2*1);
      assert(gameState.getPoolVictoryPoints() == 36 - 2 - 6);
      
      activator = (MercatorActivator) move.chooseCardToActivate(Rules.DICE_DISC_1);
 
      activator.chooseMercatorBuyNum(5);
      activator.complete();
      assert(gameState.getPlayerVictoryPoints(0) == 2 + (1+5));
      assert(gameState.getPlayerSestertii(0) == 100 - 2*(1+5) - 7); 
      assert(gameState.getPlayerVictoryPoints(1) == 0);
      assert(gameState.getPlayerSestertii(1) == 100 + 2*(1+5));
      assert(gameState.getPoolVictoryPoints() == 36 - 2 - 6);

      assert(gameState.getActionDice().length == 1);
      assert(gameState.isGameCompleted());

    }
}

