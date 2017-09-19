package tests.verifiedSpec2;

import java.util.ArrayList;

import framework.Rules;
import framework.Test;
import framework.cards.Card;
import framework.interfaces.GameState;
import framework.interfaces.MoveMaker;
import framework.interfaces.activators.TelephoneBoxActivator;

/**
 * @author Luke Harrison
 * @author Kenneth Wong
 * 
 * Tests whether a TP occurs when a Kat is overwritten
 */

public class TelephoneBoxKatOverwrittenCausesParadoxTest extends Test {
   private final int PLAYER_1 = 0;
   private final int PLAYER_2 = 1;
   @Override
   public String getShortDescription() {
      return "Tests whether a TP occurs when a Kat is overwritten";
   }

   @Override
   public void run(GameState gameState, MoveMaker move) throws AssertionError,
         UnsupportedOperationException, IllegalArgumentException {
      Card[][] playerFields = new Card[Rules.NUM_PLAYERS][Rules.NUM_DICE_DISCS];
      playerFields[PLAYER_1] = new Card[] {
            Card.TELEPHONEBOX,
            Card.KAT,
            Card.SICARIUS,
            Card.SICARIUS,
            Card.SICARIUS,
            Card.SICARIUS,
            Card.SICARIUS
      };

      playerFields[PLAYER_2] = new Card[] {
            Card.LEGIONARIUS,
            Card.LEGIONARIUS,
            Card.LEGIONARIUS,
            Card.LEGIONARIUS,
            Card.LEGIONARIUS,
            Card.LEGIONARIUS,
            Card.LEGIONARIUS
      };
      
      ArrayList<Card> player1Hand = new ArrayList<Card>();
      player1Hand.add(Card.SICARIUS);
      gameState.setDeck(new ArrayList<Card>());
      gameState.setDiscard(new ArrayList<Card>());
      gameState.setWhoseTurn(PLAYER_1);
      gameState.setPlayerCardsOnDiscs(PLAYER_1,playerFields[PLAYER_1]);
      gameState.setPlayerCardsOnDiscs(PLAYER_2,playerFields[PLAYER_2]);
      gameState.setPlayerHand(PLAYER_1, player1Hand);
      gameState.setPlayerHand(PLAYER_2, new ArrayList<Card>());
      gameState.setPlayerVictoryPoints(PLAYER_1, 10);
      gameState.setPlayerVictoryPoints(PLAYER_2, 10);
      gameState.setPlayerSestertii(PLAYER_1, 10);
      gameState.setPlayerSestertii(PLAYER_2, 10);
      
      // Turn 1
      gameState.setActionDice(new int[]{1, 1, 1});
      move.endTurn();
      
      // Turn 2
      gameState.setActionDice(new int[]{1, 1, 1}); // Kat would have been activated at the start of this turn
      move.endTurn();
      
      // Turn 3
      gameState.setActionDice(new int[]{1, 1, 1}); 
      move.placeCard(Card.SICARIUS, Rules.DICE_DISC_2);
      TelephoneBoxActivator tardis = (TelephoneBoxActivator) move.chooseCardToActivate(Rules.DICE_DISC_1);
      tardis.chooseDiceDisc(Rules.DICE_DISC_2);
      tardis.shouldMoveForwardInTime(false);
      tardis.setSecondDiceUsed(1);
      tardis.complete();
      assert (gameState.isGameCompleted());
   }

}
