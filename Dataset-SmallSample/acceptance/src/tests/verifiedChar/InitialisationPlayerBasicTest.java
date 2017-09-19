package tests.verifiedChar;

import java.util.LinkedList;

import framework.Rules;
import framework.Test;
import framework.cards.Card;
import framework.interfaces.GameState;
import framework.interfaces.MoveMaker;
/**
 * Testing the basic mechanics of victory point addition and removal.
 * @author Damon (Stacey damon.stacey)
 */
public class InitialisationPlayerBasicTest extends Test {

   @Override
   public String getShortDescription() {
      return "Checking all players can be set up as needed..";
   }

   @Override
   public void run(GameState gameState, MoveMaker move)
                                          throws AssertionError,
                                          UnsupportedOperationException,
                                          IllegalArgumentException {

      for (int i = 0; i < Rules.NUM_PLAYERS; i++) {
         gameState.setWhoseTurn(i);
         assert(gameState.getWhoseTurn() == i);
         gameState.setPlayerHand(i, new LinkedList<Card>());
         gameState.setPlayerSestertii (i, 0);
         gameState.setPlayerVictoryPoints (i, (Rules.GAME_VICTORY_POINTS - 16)/2);
      }
      gameState.setWhoseTurn(0);
      for (int i = 0; i < Rules.NUM_PLAYERS; i++) {
         assert(gameState.getPoolVictoryPoints() == Rules.GAME_VICTORY_POINTS - ((Rules.GAME_VICTORY_POINTS - 16)/2)*Rules.NUM_PLAYERS);
         assert(gameState.getPlayerHand(i).size() == 0);
         assert(gameState.getPlayerSestertii(i) == 0);
         assert(gameState.getPlayerVictoryPoints(i) == (Rules.GAME_VICTORY_POINTS - 16)/2);
      }
   }
}
