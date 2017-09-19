package tests.verifiedChar;

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
 * @author Karla Burnett (karla.burnett)
 *
 */

public class CardActivatorCenturioCBasicTest extends Test {

    @Override
    public String getShortDescription() {
        return "Test the dice removal functionality of Centurio";
    }

    @Override
    public void run(GameState gameState, MoveMaker move) throws AssertionError,
            UnsupportedOperationException, IllegalArgumentException {

      // Set up the game state such that it is filled with Centurios
      gameState.setWhoseTurn(0);
      Card discs[] = new Card[Rules.NUM_DICE_DISCS];
      for (int i = 0; i < Rules.NUM_DICE_DISCS; i++) {
         discs[i] = Card.CENTURIO;
      }

      gameState.setPlayerCardsOnDiscs(0, discs);
      gameState.setPlayerCardsOnDiscs(1, discs);

      // Activate the Centurio such that it loses without using another die
      gameState.setActionDice(new int[] {1, 1, 1});

      CenturioActivator activator = (CenturioActivator) move.chooseCardToActivate(1);
      activator.giveAttackDieRoll(4);
      activator.chooseCenturioAddActionDie(false);
      activator.complete();

      // Check that one die was removed
      assert(gameState.getActionDice().length == 2);
      assert(gameState.getPlayerCardsOnDiscs(0)[0] == Card.CENTURIO);

      // This time activate the Centurio such that it just wins by using the
      // second die
      activator = (CenturioActivator) move.chooseCardToActivate(1);
      activator.giveAttackDieRoll(4);
      activator.chooseCenturioAddActionDie(true);
      activator.chooseActionDice(1);
      activator.complete();

      // Check that two dice were removed, and that the opposing card was destroyed
      assert(gameState.getActionDice().length == 0);
      assert(gameState.getPlayerCardsOnDiscs(1)[0] == Card.NOT_A_CARD);
   }
}
