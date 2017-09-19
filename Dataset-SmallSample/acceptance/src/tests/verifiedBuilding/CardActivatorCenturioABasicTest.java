package tests.verifiedBuilding;

import java.util.LinkedList;
import java.util.List;

import framework.Rules;
import framework.Test;
import framework.cards.Card;
import framework.interfaces.GameState;
import framework.interfaces.MoveMaker;
import framework.interfaces.activators.CenturioActivator;

/**
 *
 * Test the basic functionality of Centurio
 *
 * @author Damon Stacey
 *
 */

public class CardActivatorCenturioABasicTest extends Test {

    @Override
    public String getShortDescription() {
        return "Test the basic functionality of Centurio";
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
         hand.add(Card.CENTURIO);
         hand.add(Card.CENTURIO);
         hand.add(Card.ARCHITECTUS);
         gameState.setPlayerHand(i, hand);
      }
      
      gameState.setActionDice(new int[] {3,2,3});

      move.placeCard(Card.CENTURIO, Rules.DICE_DISC_1);
      move.placeCard(Card.CENTURIO, Rules.DICE_DISC_2);

      assert(gameState.getPlayerSestertii(0) == 100 - 9*2);
      assert(gameState.getPlayerSestertii(1) == 100);
      assert(gameState.getPlayerHand(0).size() == 1);
      assert(gameState.getPlayerHand(0).contains(Card.ARCHITECTUS));
      Card[] field;
      field = gameState.getPlayerCardsOnDiscs(0);
      assert(field[0] == Card.CENTURIO);
      assert(field[1] == Card.CENTURIO);
      
      assert(gameState.getPoolVictoryPoints() == 36 - 15*Rules.NUM_PLAYERS);
      assert(!gameState.isGameCompleted());
      
      move.endTurn();
      
      move.placeCard(Card.ARCHITECTUS, Rules.DICE_DISC_1);
      move.placeCard(Card.CENTURIO, Rules.DICE_DISC_2);
 
      field = gameState.getPlayerCardsOnDiscs(1);
      assert(field[0] == Card.ARCHITECTUS);
      assert(field[1] == Card.CENTURIO);
 
      move.endTurn();
      gameState.setActionDice(new int[] {1,2,1});

      CenturioActivator activator = (CenturioActivator) move.chooseCardToActivate(1);
      activator.giveAttackDieRoll(1);
      activator.chooseCenturioAddActionDie(false);
      activator.complete();
      field = gameState.getPlayerCardsOnDiscs(1);
      assert(field[0] == Card.ARCHITECTUS);
      assert(field[1] == Card.CENTURIO);

      activator = (CenturioActivator) move.chooseCardToActivate(1);
      activator.giveAttackDieRoll(6);
      activator.chooseCenturioAddActionDie(false);
      activator.complete();
      field = gameState.getPlayerCardsOnDiscs(1);
      assert(field[0] == Card.NOT_A_CARD);
      assert(field[1] == Card.CENTURIO);

      activator = (CenturioActivator) move.chooseCardToActivate(2);
      activator.giveAttackDieRoll(6);
      activator.chooseCenturioAddActionDie(false);
      activator.complete();
      field = gameState.getPlayerCardsOnDiscs(1);
      assert(field[0] == Card.NOT_A_CARD);
      assert(field[1] == Card.NOT_A_CARD);
      assert(field[2] == Card.NOT_A_CARD);
      assert(field[3] == Card.NOT_A_CARD);
      assert(field[4] == Card.NOT_A_CARD);
      assert(field[5] == Card.NOT_A_CARD);
      assert(field[6] == Card.NOT_A_CARD);
      assert(!gameState.isGameCompleted());

    }
}
