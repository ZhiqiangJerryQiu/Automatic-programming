package tests.verifiedChar;

import java.util.LinkedList;
import java.util.List;

import framework.Rules;
import framework.Test;
import framework.cards.Card;
import framework.interfaces.GameState;
import framework.interfaces.MoveMaker;
import framework.interfaces.activators.ConsulActivator;

/**
 *
 * Test the basic functionality of Consul
 *
 * @author Damon Stacey
 *
 */

public class CardActivatorConsulABasicTest extends Test {

    @Override
    public String getShortDescription() {
        return "Test the basic functionality of Consul";
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
         hand.add(Card.CONSUL);
         hand.add(Card.FORUM);
         hand.add(Card.FORUM);
         gameState.setPlayerHand(i, hand);
      }
      
      gameState.setActionDice(new int[] {1,2,1});

      move.placeCard(Card.CONSUL, Rules.DICE_DISC_1);

      assert(gameState.getPlayerSestertii(1) == 100);
      assert(gameState.getPlayerHand(0).size() == 2);
      assert(!gameState.getPlayerHand(0).contains(Card.ARCHITECTUS));
      Card[] field;
      field = gameState.getPlayerCardsOnDiscs(0);
      assert(field[0] == Card.CONSUL);
      assert(field[1] == Card.NOT_A_CARD);
      assert(field[2] == Card.NOT_A_CARD);
      
      assert(gameState.getPoolVictoryPoints() == 36 - 15*Rules.NUM_PLAYERS);
      assert(!gameState.isGameCompleted());
      
      ConsulActivator activator = (ConsulActivator) move.chooseCardToActivate(1);
   
      activator.chooseWhichDiceChanges(2);
      activator.chooseConsulChangeAmount(-1);
      activator.complete();
      
      assert(gameState.getActionDice().length == 2);
      assert(gameState.getActionDice()[0] == 1);
      assert(gameState.getActionDice()[1] == 1);
      activator = (ConsulActivator) move.chooseCardToActivate(1);
   
      activator.chooseWhichDiceChanges(1);
      activator.chooseConsulChangeAmount(1);
      activator.complete();
      
      assert(gameState.getActionDice().length == 1);
      assert(gameState.getActionDice()[0] == 2);

      assert(gameState.getPoolVictoryPoints() == 36 - 15*Rules.NUM_PLAYERS);
      assert(!gameState.isGameCompleted());

    }
}
