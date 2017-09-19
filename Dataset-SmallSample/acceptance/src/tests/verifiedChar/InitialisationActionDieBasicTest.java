package tests.verifiedChar;

import framework.Test;
import framework.interfaces.GameState;
import framework.interfaces.MoveMaker;
/**
 * Testing the basic mechanics of action dice (get and set).
 * @author Damon (Stacey damon.stacey)
 */
public class InitialisationActionDieBasicTest extends Test {

   @Override
   public String getShortDescription() {
      return "Checking all action dice can be set and recieved accurately..";
   }

   @Override
   public void run(GameState gameState, MoveMaker move)
                                          throws AssertionError,
                                          UnsupportedOperationException,
                                          IllegalArgumentException {
      for (int i = 1; i <= 6; i++) {   
         for (int j = 1; j <= 6; j++) {   
            for (int k = 1; k <= 6; k++) {   
               int[] dice = new int[] {i, j, k};
               gameState.setActionDice(dice);
               assert(gameState.getActionDice().length == dice.length);     
            }
         }
      }      
   }
}
