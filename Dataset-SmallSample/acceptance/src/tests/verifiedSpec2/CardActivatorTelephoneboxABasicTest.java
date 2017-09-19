package tests.verifiedSpec2;

import java.util.LinkedList;
import java.util.List;

import framework.Rules;
import framework.Test;
import framework.cards.Card;
import framework.interfaces.GameState;
import framework.interfaces.MoveMaker;
import framework.interfaces.activators.TelephoneBoxActivator;

/**
 *
 * Test the basic functionality of TelephoneBox
 *
 * @author Damon Stacey
 *
 */

public class CardActivatorTelephoneboxABasicTest extends Test {

    @Override
    public String getShortDescription() {
        return "Test the basic functionality of TelephoneBox";
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
         hand.add(Card.TELEPHONEBOX);
         hand.add(Card.TELEPHONEBOX);
         hand.add(Card.TELEPHONEBOX);
         hand.add(Card.TELEPHONEBOX);
         hand.add(Card.TELEPHONEBOX);
         hand.add(Card.TELEPHONEBOX);
         hand.add(Card.TELEPHONEBOX);
         hand.add(Card.TELEPHONEBOX);
         hand.add(Card.MERCATUS);
         gameState.setPlayerHand(i, hand);
      }
      
      gameState.setActionDice(new int[] {3,2,3});

      move.placeCard(Card.TELEPHONEBOX, Rules.DICE_DISC_1);
      move.placeCard(Card.TELEPHONEBOX, Rules.DICE_DISC_2);
      move.placeCard(Card.MERCATUS, Rules.DICE_DISC_3);

      assert(gameState.getPlayerHand(0).contains(Card.TELEPHONEBOX));

      Card[] field;
      field = gameState.getPlayerCardsOnDiscs(0);
      assert(field[0] == Card.TELEPHONEBOX);
      assert(field[1] == Card.TELEPHONEBOX);
      assert(field[2] == Card.MERCATUS);
      
      assert(gameState.getPoolVictoryPoints() == 36 - 15*Rules.NUM_PLAYERS);
      assert(!gameState.isGameCompleted());

      gameState.setActionDice(new int[] {1,2,1});
      assert(gameState.getActionDice().length == 3);
       
      TelephoneBoxActivator activator = (TelephoneBoxActivator) move.chooseCardToActivate(Rules.DICE_DISC_1);
      activator.chooseDiceDisc(Rules.DICE_DISC_3);
      activator.shouldMoveForwardInTime(true);
      activator.setSecondDiceUsed(2);
      activator.complete();

      field = gameState.getPlayerCardsOnDiscs(0);      
      assert(field[0] == Card.TELEPHONEBOX);
      assert(field[1] == Card.TELEPHONEBOX);
      assert(field[2] == Card.NOT_A_CARD);
      
      assert(gameState.getActionDice().length == 1);
      assert(!gameState.isGameCompleted());

      move.endTurn();
      field = gameState.getPlayerCardsOnDiscs(0);

      assert(field[0] == Card.TELEPHONEBOX);
      assert(field[1] == Card.TELEPHONEBOX);
      assert(field[2] == Card.NOT_A_CARD);
      
      move.endTurn();

      field = gameState.getPlayerCardsOnDiscs(0);
      assert(field[0] == Card.TELEPHONEBOX);
      assert(field[1] == Card.TELEPHONEBOX);
      assert(field[2] == Card.MERCATUS);
      assert(!gameState.isGameCompleted());
   }
}
