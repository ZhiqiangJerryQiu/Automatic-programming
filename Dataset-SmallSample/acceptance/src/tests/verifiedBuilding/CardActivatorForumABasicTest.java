package tests.verifiedBuilding;

import java.util.LinkedList;
import java.util.List;

import framework.Rules;
import framework.Test;
import framework.cards.Card;
import framework.interfaces.GameState;
import framework.interfaces.MoveMaker;
import framework.interfaces.activators.ForumActivator;

/**
 *
 * Test the basic functionality of Forum
 *
 * @author Damon Stacey
 *
 */

public class CardActivatorForumABasicTest extends Test {

    @Override
    public String getShortDescription() {
        return "Test the basic functionality of Forum";
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
         hand.add(Card.BASILICA);
         hand.add(Card.TEMPLUM);
         hand.add(Card.ARCHITECTUS);
         gameState.setPlayerHand(i, hand);
      }

      gameState.setActionDice(new int[] {3,2,3});

      move.placeCard(Card.FORUM, Rules.DICE_DISC_2);
      move.placeCard(Card.ARCHITECTUS, Rules.DICE_DISC_3);

      assert(gameState.getPlayerSestertii(0) == 100 - 5 - 3);
      assert(gameState.getPlayerSestertii(1) == 100);
      assert(gameState.getPlayerHand(0).size() == 2);
      assert(!gameState.getPlayerHand(0).contains(Card.ARCHITECTUS));
      Card[] field;
      field = gameState.getPlayerCardsOnDiscs(0);
      assert(field[1] == Card.FORUM);

      assert(gameState.getPoolVictoryPoints() == 36 - 15*Rules.NUM_PLAYERS);
      assert(!gameState.isGameCompleted());

      gameState.setActionDice(new int[] {6,2,5});

      ForumActivator activator = (ForumActivator) move.chooseCardToActivate(2);
      activator.chooseActionDice(5);
      activator.complete();
      assert(gameState.getPlayerVictoryPoints(0) == 15+5);
      assert(gameState.getPlayerVictoryPoints(1) == 15);
      assert(gameState.getActionDice().length == 1);
      move.endTurn();
      move.endTurn();

      assert(gameState.getPlayerVictoryPoints(0) == 15+5-5);
      assert(gameState.getPlayerVictoryPoints(1) == 15-7);
      move.placeCard(Card.BASILICA, Rules.DICE_DISC_3);
      move.placeCard(Card.TEMPLUM, Rules.DICE_DISC_1);

      gameState.setActionDice(new int[] {6,2,5});
      activator = (ForumActivator) move.chooseCardToActivate(2);
      activator.chooseActionDice(5);
      activator.chooseActivateTemplum(true);
      activator.chooseActivateTemplum(6);
      activator.complete();
      assert(gameState.getPlayerVictoryPoints(0) == 15+5+6+2);
      assert(gameState.getPlayerVictoryPoints(1) == 15-7);
      assert(gameState.isGameCompleted());

    }
}
