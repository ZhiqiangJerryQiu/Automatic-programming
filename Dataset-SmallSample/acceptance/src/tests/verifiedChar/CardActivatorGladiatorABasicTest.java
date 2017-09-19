package tests.verifiedChar;

import java.util.LinkedList;
import java.util.List;

import framework.Rules;
import framework.Test;
import framework.cards.Card;
import framework.interfaces.GameState;
import framework.interfaces.MoveMaker;
import framework.interfaces.activators.GladiatorActivator;

/**
 *
 * Test the basic functionality of Gladiator
 *
 * @author Damon Stacey
 *
 */

public class CardActivatorGladiatorABasicTest extends Test {

    @Override
    public String getShortDescription() {
        return "Test the basic functionality of Gladiator";
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
         hand.add(Card.GLADIATOR);
         hand.add(Card.TEMPLUM);
         hand.add(Card.ARCHITECTUS);
         gameState.setPlayerHand(i, hand);
      }
      
      gameState.setActionDice(new int[] {3,2,3});

      move.placeCard(Card.FORUM, Rules.DICE_DISC_2);
      move.placeCard(Card.GLADIATOR, Rules.DICE_DISC_3);

      assert(gameState.getPlayerSestertii(0) == 100 - 11);
      assert(gameState.getPlayerSestertii(1) == 100);
      assert(gameState.getPlayerHand(0).size() == 2);
      assert(gameState.getPlayerHand(0).contains(Card.ARCHITECTUS));
      Card[] field;
      field = gameState.getPlayerCardsOnDiscs(0);
      assert(field[1] == Card.FORUM);
      
      assert(gameState.getPoolVictoryPoints() == 36 - 15*Rules.NUM_PLAYERS);
      assert(!gameState.isGameCompleted());

      move.endTurn();
      move.placeCard(Card.ARCHITECTUS, Rules.DICE_DISC_1);
      move.placeCard(Card.GLADIATOR, Rules.DICE_DISC_2);

      move.endTurn();

      gameState.setActionDice(new int[] {3,3,3});
      assert(!gameState.getPlayerHand(1).contains(Card.ARCHITECTUS));
      assert(!gameState.getPlayerHand(1).contains(Card.GLADIATOR));

      GladiatorActivator activator = (GladiatorActivator) move.chooseCardToActivate(3);
      activator.chooseDiceDisc(1);
      activator.complete();
      field = gameState.getPlayerCardsOnDiscs(1);
      assert(field[0] == Card.NOT_A_CARD);
      assert(field[1] == Card.GLADIATOR);
      assert(gameState.getPlayerHand(0).contains(Card.ARCHITECTUS));
      assert(gameState.getActionDice().length == 2);
      assert(gameState.getActionDice()[0] == 3);
      assert(gameState.getActionDice()[1] == 3);
      
      assert(!gameState.getPlayerHand(1).contains(Card.GLADIATOR));

      activator = (GladiatorActivator) move.chooseCardToActivate(3);
      activator.chooseDiceDisc(2);
      activator.complete();
      field = gameState.getPlayerCardsOnDiscs(1);
      assert(field[0] == Card.NOT_A_CARD);
      assert(field[1] == Card.NOT_A_CARD);
      assert(gameState.getPlayerHand(0).contains(Card.ARCHITECTUS));
      assert(gameState.getPlayerHand(1).contains(Card.GLADIATOR));
      assert(gameState.getActionDice().length == 1);
      assert(gameState.getActionDice()[0] == 3);
      assert(!gameState.isGameCompleted());
    }
}
