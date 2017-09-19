package tests.verifiedBuilding;

import java.util.LinkedList;
import java.util.List;

import framework.Rules;
import framework.Test;
import framework.cards.Card;
import framework.interfaces.GameState;
import framework.interfaces.MoveMaker;
import framework.interfaces.activators.MercatusActivator;

/**
 *
 * Test the basic functionality of Mercatus
 *
 * @author Damon Stacey
 *
 */

public class CardActivatorMercatusABasicTest extends Test {

    @Override
    public String getShortDescription() {
        return "Test the basic functionality of Mercatus";
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
         gameState.setPlayerVictoryPoints(i, 15);
         hand = new LinkedList<Card>();
         hand.add(Card.FORUM);
         hand.add(Card.FORUM);
         hand.add(Card.FORUM);
         hand.add(Card.FORUM);
         hand.add(Card.FORUM);
         hand.add(Card.FORUM);
         hand.add(Card.FORUM);
         hand.add(Card.FORUM);
         hand.add(Card.MERCATUS);
         gameState.setPlayerHand(i, hand);
      }
      
      gameState.setActionDice(new int[] {3,2,3});

      move.placeCard(Card.FORUM, Rules.DICE_DISC_1);
      move.placeCard(Card.FORUM, Rules.DICE_DISC_2);
      move.placeCard(Card.FORUM, Rules.DICE_DISC_3);
      move.endTurn();
      gameState.setActionDice(new int[] {1,1,1});
      assert(gameState.getPlayerSestertii(1) == 100);
      assert(gameState.getPlayerHand(0).size() == 6);
      assert(gameState.getPlayerHand(0).contains(Card.MERCATUS));

      Card[] field;
      field = gameState.getPlayerCardsOnDiscs(0);
      assert(field[0] == Card.FORUM);
      assert(field[1] == Card.FORUM);
      assert(field[2] == Card.FORUM);
      
      assert(gameState.getPoolVictoryPoints() == 36 - 15*Rules.NUM_PLAYERS + 7);
      assert(!gameState.isGameCompleted());
      move.placeCard(Card.MERCATUS, Rules.DICE_DISC_1);
      move.placeCard(Card.FORUM, Rules.DICE_DISC_2);
      move.placeCard(Card.FORUM, Rules.DICE_DISC_3);
      move.placeCard(Card.FORUM, Rules.DICE_DISC_4);
      move.placeCard(Card.FORUM, Rules.DICE_DISC_5);
      move.placeCard(Card.FORUM, Rules.DICE_DISC_6);
      move.placeCard(Card.FORUM, Rules.BRIBE_DISC);

      assert(gameState.getActionDice().length == 3);
       
      MercatusActivator activator = (MercatusActivator) move.chooseCardToActivate(Rules.DICE_DISC_1);
      activator.complete();
      assert(gameState.getActionDice().length == 2);
      assert(gameState.getPlayerVictoryPoints(1) == 15 - 7 + 3);
      assert(gameState.getPlayerVictoryPoints(0) == 15 - 3);
      assert(gameState.getActionDice().length == 2);
      move.endTurn();
      gameState.setActionDice(new int[] {1,1,1});
      move.placeCard(Card.MERCATUS, Rules.DICE_DISC_1);
      
        
      activator = (MercatusActivator) move.chooseCardToActivate(Rules.DICE_DISC_1);
      activator.complete();
      assert(gameState.getPlayerVictoryPoints(1) == 15-7+3-6);
      assert(gameState.getPlayerVictoryPoints(0) == 15-3-4+6);
      assert(gameState.getActionDice().length == 2);      
      assert(!gameState.isGameCompleted());

    }
}
