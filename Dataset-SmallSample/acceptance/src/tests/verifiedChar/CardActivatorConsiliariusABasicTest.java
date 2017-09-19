package tests.verifiedChar;

import java.util.LinkedList;
import java.util.List;

import framework.Rules;
import framework.Test;
import framework.cards.Card;
import framework.interfaces.GameState;
import framework.interfaces.MoveMaker;
import framework.interfaces.activators.ConsiliariusActivator;

/**
 *
 * Test the basic functionality of Consiliarius
 *
 * @author Damon Stacey
 *
 */

public class CardActivatorConsiliariusABasicTest extends Test {

    @Override
    public String getShortDescription() {
        return "Test the basic functionality of Consiliarius";
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
      discard.add(Card.HARUSPEX);
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
         hand.add(Card.CONSILIARIUS);
         hand.add(Card.HARUSPEX);
         hand.add(Card.HARUSPEX);
         gameState.setPlayerHand(i, hand);
      }

      gameState.setActionDice(new int[] {1,1,1});

      move.placeCard(Card.CONSILIARIUS, Rules.DICE_DISC_1);
      move.placeCard(Card.HARUSPEX, Rules.DICE_DISC_2);
      move.placeCard(Card.HARUSPEX, Rules.DICE_DISC_3);

      assert(gameState.getPlayerSestertii(1) == 100);
      assert(gameState.getPlayerHand(0).size() == 0);
      assert(!gameState.getPlayerHand(0).contains(Card.ARCHITECTUS));
      Card[] field;
      field = gameState.getPlayerCardsOnDiscs(0);
      assert(field[0] == Card.CONSILIARIUS);
      assert(field[1] == Card.HARUSPEX);
      assert(field[2] == Card.HARUSPEX);

      assert(gameState.getPoolVictoryPoints() == 36 - 15*Rules.NUM_PLAYERS);
      assert(!gameState.isGameCompleted());

      ConsiliariusActivator activator = (ConsiliariusActivator) move.chooseCardToActivate(1);
      activator.placeCard(Card.CONSILIARIUS, Rules.DICE_DISC_1);
      activator.placeCard (Card.HARUSPEX, Rules.BRIBE_DISC);
      activator.placeCard (Card.HARUSPEX, Rules.DICE_DISC_4);
      activator.complete();

      field = gameState.getPlayerCardsOnDiscs(0);
      assert(field[0] == Card.CONSILIARIUS);
      assert(field[3] == Card.HARUSPEX);
      assert(field[6] == Card.HARUSPEX);

      activator = (ConsiliariusActivator) move.chooseCardToActivate(1);
      activator.placeCard(Card.CONSILIARIUS, Rules.DICE_DISC_1);
      activator.placeCard (Card.HARUSPEX, Rules.DICE_DISC_3);
      activator.placeCard (Card.HARUSPEX, Rules.DICE_DISC_5);
      activator.complete();

      field = gameState.getPlayerCardsOnDiscs(0);
      assert(field[0] == Card.CONSILIARIUS);
      assert(field[2] == Card.HARUSPEX);
      assert(field[4] == Card.HARUSPEX);

      activator = (ConsiliariusActivator) move.chooseCardToActivate(1);
      activator.placeCard(Card.CONSILIARIUS, Rules.DICE_DISC_1);
      activator.placeCard (Card.HARUSPEX, Rules.DICE_DISC_3);
      activator.placeCard (Card.HARUSPEX, Rules.BRIBE_DISC);
      activator.complete();

      field = gameState.getPlayerCardsOnDiscs(0);
      assert(field[0] == Card.CONSILIARIUS);
      assert(field[1] == Card.NOT_A_CARD);
      assert(field[2] == Card.HARUSPEX);
      assert(field[3] == Card.NOT_A_CARD);
      assert(field[4] == Card.NOT_A_CARD);
      assert(field[5] == Card.NOT_A_CARD);
      assert(field[6] == Card.HARUSPEX);
      assert(gameState.getPoolVictoryPoints() == 36 - 15*Rules.NUM_PLAYERS);
      assert(!gameState.isGameCompleted());
    }
}
