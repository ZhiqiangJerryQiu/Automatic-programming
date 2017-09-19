package tests.verifiedSpec2;

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
 * Test the basic functionality of Grimreaper
 *
 * @author Damon Stacey
 *
 */

public class CardActivatorGrimreaperABasicTest extends Test {

    @Override
    public String getShortDescription() {
        return "Test the basic functionality of Grimreaper";
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
      
      gameState.setWhoseTurn(0);
      List<Card> hand;
      for (int i = 0; i < Rules.NUM_PLAYERS; i++) {
         gameState.setPlayerSestertii(i, 100);
         gameState.setPlayerVictoryPoints(i, 15);
         hand = new LinkedList<Card>();
         hand.add(Card.GRIMREAPER);
         hand.add(Card.CENTURIO);
         hand.add(Card.CENTURIO);
         hand.add(Card.ARCHITECTUS);
         gameState.setPlayerHand(i, hand);
      }
      
      gameState.setActionDice(new int[] {3,2,3});

      move.placeCard(Card.CENTURIO, Rules.DICE_DISC_2);
      move.placeCard(Card.CENTURIO, Rules.DICE_DISC_3);

      assert(gameState.getPlayerSestertii(1) == 100);
      assert(gameState.getPlayerHand(0).contains(Card.ARCHITECTUS));
      Card[] field;
      field = gameState.getPlayerCardsOnDiscs(0);
      assert(field[1] == Card.CENTURIO);
      assert(field[2] == Card.CENTURIO);
      
      assert(gameState.getPoolVictoryPoints() == 36 - 15*Rules.NUM_PLAYERS);
      assert(!gameState.isGameCompleted());

      gameState.setActionDice(new int[] {6,2,5});
      move.endTurn();
      move.placeCard(Card.GRIMREAPER, Rules.DICE_DISC_2);
      move.placeCard(Card.CENTURIO, Rules.DICE_DISC_3);

      
      move.endTurn();
      gameState.setActionDice(new int[] {3,2,3});

      field = gameState.getPlayerCardsOnDiscs(1);
      assert(field[1] == Card.GRIMREAPER);
      assert(field[2] == Card.CENTURIO);

      CenturioActivator activator = (CenturioActivator) move.chooseCardToActivate(3);
      activator.giveAttackDieRoll(6);
      activator.chooseCenturioAddActionDie(false);
      activator.complete();      

      field = gameState.getPlayerCardsOnDiscs(1);
      assert(field[1] == Card.GRIMREAPER);
      assert(field[2] == Card.NOT_A_CARD);
      assert(!gameState.getDiscard().contains(Card.CENTURIO));
      assert(gameState.getPlayerHand(1).contains(Card.CENTURIO));

      activator = (CenturioActivator) move.chooseCardToActivate(2);
      activator.giveAttackDieRoll(6);
      activator.chooseCenturioAddActionDie(false);
      activator.complete();      

      field = gameState.getPlayerCardsOnDiscs(1);      
      assert(field[1] == Card.NOT_A_CARD);
      assert(field[2] == Card.NOT_A_CARD);
      assert(gameState.getDiscard().contains(Card.GRIMREAPER));
      assert(!gameState.getPlayerHand(1).contains(Card.GRIMREAPER));
      assert(!gameState.isGameCompleted());
      
    }
}
