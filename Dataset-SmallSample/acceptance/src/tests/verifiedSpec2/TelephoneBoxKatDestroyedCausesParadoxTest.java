package tests.verifiedSpec2;

import java.util.ArrayList;

import framework.Rules;
import framework.Test;
import framework.cards.Card;
import framework.interfaces.GameState;
import framework.interfaces.MoveMaker;
import framework.interfaces.activators.TelephoneBoxActivator;
import framework.interfaces.activators.VelitesActivator;

/**
 * @author Luke Harrison
 * @author Kenneth Wong
 * 
 * Tests whether a TP occurs when a Kat is destroyed, where it didn't originally
 */

public class TelephoneBoxKatDestroyedCausesParadoxTest extends Test {
   private final int PLAYER_1 = 0;
   private final int PLAYER_2 = 1;
   @Override
   public String getShortDescription() {
      return "Tests whether a TP occurs when a Kat is destroyed in a replay";
   }

   @Override
   public void run(GameState gameState, MoveMaker move) throws AssertionError,
         UnsupportedOperationException, IllegalArgumentException {
      Card[][] playerFields = new Card[Rules.NUM_PLAYERS][Rules.NUM_DICE_DISCS];
      playerFields[PLAYER_1] = new Card[] {
            Card.TELEPHONEBOX,
            Card.KAT,
            Card.TURRIS,
            Card.SICARIUS,
            Card.SICARIUS,
            Card.SICARIUS,
            Card.SICARIUS
      };

      playerFields[PLAYER_2] = new Card[] {
            Card.VELITES,
            Card.VELITES,
            Card.VELITES,
            Card.LEGIONARIUS,
            Card.LEGIONARIUS,
            Card.LEGIONARIUS,
            Card.LEGIONARIUS
      };
      
      ArrayList<Card> player1Hand = new ArrayList<Card>();
      player1Hand.add(Card.SICARIUS);
      gameState.setDeck(new ArrayList<Card>());
      gameState.setDiscard(new ArrayList<Card>());
      gameState.setWhoseTurn(PLAYER_2);
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
      
      // Turn 3 Player 2
      gameState.setActionDice(new int[]{1, 2, 3}); 
      VelitesActivator attacker = (VelitesActivator) move.chooseCardToActivate(Rules.DICE_DISC_1);
      attacker.chooseDiceDisc(Rules.DICE_DISC_2);
      attacker.giveAttackDieRoll(1);
      attacker.complete(); // Attacked 1
      
      attacker = (VelitesActivator) move.chooseCardToActivate(Rules.DICE_DISC_2);
      attacker.chooseDiceDisc(Rules.DICE_DISC_2);
      attacker.giveAttackDieRoll(1);
      attacker.complete(); // Attacked 2
      
      attacker = (VelitesActivator) move.chooseCardToActivate(Rules.DICE_DISC_3);
      attacker.chooseDiceDisc(Rules.DICE_DISC_2);
      attacker.giveAttackDieRoll(1);
      attacker.complete(); // Attacked 3
      
      move.endTurn(); 
      
      // Turn 4
      gameState.setActionDice(new int[]{1, 1, 1});
      move.endTurn();
      
      // Turn 5
      gameState.setActionDice(new int[]{1, 2, 3}); 
      attacker = (VelitesActivator) move.chooseCardToActivate(Rules.DICE_DISC_1);
      attacker.chooseDiceDisc(Rules.DICE_DISC_2);
      attacker.giveAttackDieRoll(1);
      attacker.complete(); // Attacked 4
      
      attacker = (VelitesActivator) move.chooseCardToActivate(Rules.DICE_DISC_2);
      attacker.chooseDiceDisc(Rules.DICE_DISC_2);
      attacker.giveAttackDieRoll(1);
      attacker.complete(); // Attacked 5
      
      attacker = (VelitesActivator) move.chooseCardToActivate(Rules.DICE_DISC_3);
      attacker.chooseDiceDisc(Rules.DICE_DISC_2);
      attacker.giveAttackDieRoll(1);
      attacker.complete(); // Attacked 6
      
      move.endTurn();
      
      // Turn 6
      gameState.setActionDice(new int[]{1, 1, 1});
      move.endTurn();
      
      // Turn 7
      gameState.setActionDice(new int[]{1, 2, 3}); 
      attacker = (VelitesActivator) move.chooseCardToActivate(Rules.DICE_DISC_1);
      attacker.chooseDiceDisc(Rules.DICE_DISC_2);
      attacker.giveAttackDieRoll(1);
      attacker.complete(); // Attacked 7
      
      attacker = (VelitesActivator) move.chooseCardToActivate(Rules.DICE_DISC_2);
      attacker.chooseDiceDisc(Rules.DICE_DISC_2);
      attacker.giveAttackDieRoll(1);
      attacker.complete(); // Attacked 8
      
      attacker = (VelitesActivator) move.chooseCardToActivate(Rules.DICE_DISC_3);
      attacker.chooseDiceDisc(Rules.DICE_DISC_2);
      attacker.giveAttackDieRoll(1);
      attacker.complete(); // Attacked 9
      
      move.endTurn();
      assert (gameState.getPlayerCardsOnDiscs(PLAYER_1)[Rules.DICE_DISC_2 - 1] == Card.KAT);
      // Turn 8
      // Kat will have been activated at the start of this turn
      gameState.setActionDice(new int[]{1, 5, 1});
      assert (gameState.getPlayerCardsOnDiscs(PLAYER_1)[Rules.DICE_DISC_3 - 1] == Card.TURRIS);
      move.placeCard(Card.SICARIUS, Rules.DICE_DISC_3); // Placed over the Turris
      assert (gameState.getPlayerCardsOnDiscs(PLAYER_1)[Rules.DICE_DISC_3 - 1] == Card.SICARIUS);
      
      TelephoneBoxActivator tardis = (TelephoneBoxActivator) move.chooseCardToActivate(Rules.DICE_DISC_1);
      tardis.chooseDiceDisc(Rules.DICE_DISC_3);
      tardis.shouldMoveForwardInTime(false);
      tardis.setSecondDiceUsed(5);
      tardis.complete(); // The Kat is no longer protected
      assert (gameState.getPlayerCardsOnDiscs(PLAYER_1)[Rules.DICE_DISC_2 - 1] == Card.NOT_A_CARD);
      assert (gameState.isGameCompleted());
   }

}
