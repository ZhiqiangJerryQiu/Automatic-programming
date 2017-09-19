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
 * Tests that the time machine doesn't do anything if you send the same card back as was there previously
 */


public class TelephoneBoxNoEffectWhenSameNameTest extends Test {
   private final int PLAYER_1 = 0;
   private final int PLAYER_2 = 1;
   @Override
   public String getShortDescription() {
      return "Tests moving a card backwards in time over the same card doesn't do anything";
   }

   @Override
   public void run(GameState gameState, MoveMaker move) throws AssertionError,
         UnsupportedOperationException, IllegalArgumentException {
      Card[][] playerFields = new Card[Rules.NUM_PLAYERS][Rules.NUM_DICE_DISCS];
      playerFields[PLAYER_1] = new Card[] {
            Card.TELEPHONEBOX,
            Card.SICARIUS,
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
      gameState.setActionDice(new int[]{1, 1, 1});
      move.endTurn();
      
      // Turn 3
      gameState.setActionDice(new int[]{1, 1, 1});
      // Send the sicarius back a turn
      TelephoneBoxActivator tardis = (TelephoneBoxActivator) move.chooseCardToActivate(Rules.DICE_DISC_1);
      tardis.chooseDiceDisc(Rules.DICE_DISC_2);
      tardis.shouldMoveForwardInTime(false);
      tardis.setSecondDiceUsed(1);
      tardis.complete();
      assert (gameState.getActionDice().length == 1);
      assert (gameState.getDiscard().size() == 0);
      assert (gameState.getPlayerCardsOnDiscs(PLAYER_1)[Rules.DICE_DISC_2 - 1] == Card.SICARIUS);
      move.endTurn();
      
      // Turn 4
      gameState.setActionDice(new int[]{1, 1, 1});
      move.endTurn();
      
      // Turn 5
      gameState.setActionDice(new int[]{1, 3, 1});
      move.placeCard(Card.SICARIUS, Rules.DICE_DISC_2);
      assert (gameState.getPlayerSestertii(PLAYER_1) == 10 - 9);
      assert (gameState.getDiscard().size() == 1);
      assert (gameState.getDiscard().contains(Card.SICARIUS));
      assert (gameState.getPlayerCardsOnDiscs(PLAYER_1)[Rules.DICE_DISC_2 - 1] == Card.SICARIUS);
      tardis = (TelephoneBoxActivator) move.chooseCardToActivate(Rules.DICE_DISC_1);
      tardis.chooseDiceDisc(Rules.DICE_DISC_2);
      tardis.shouldMoveForwardInTime(false);
      tardis.setSecondDiceUsed(3);
      tardis.complete();
      assert (gameState.getPlayerSestertii(PLAYER_1) == 10 - 9);
      assert (gameState.getDiscard().size() == 1);
      assert (gameState.getDiscard().contains(Card.SICARIUS));
      assert (gameState.getPlayerCardsOnDiscs(PLAYER_1)[Rules.DICE_DISC_2 - 1] == Card.SICARIUS);
      assert (gameState.getActionDice().length == 1);
   }

}
