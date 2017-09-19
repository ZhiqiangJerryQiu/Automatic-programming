package tests.verifiedSpec2;

import java.util.ArrayList;

import framework.Rules;
import framework.Test;
import framework.cards.Card;
import framework.interfaces.GameState;
import framework.interfaces.MoveMaker;
import framework.interfaces.activators.NeroActivator;
import framework.interfaces.activators.ScaenicusActivator;
import framework.interfaces.activators.TelephoneBoxActivator;

/**
 * @author Luke Harrison
 * @author Kenneth Wong
 * 
 * Tests that something coping nero still dies even if there is no valid target, with a TIME MACHINE
 */

public class TelephoneBoxCopyNeroAndStillDieTest extends Test {
   private final int PLAYER_1 = 0;
   private final int PLAYER_2 = 1;
   @Override
   public String getShortDescription() {
      return "Tests coping nero is bad when you go back in time";
   }

   @Override
   public void run(GameState gameState, MoveMaker move) throws AssertionError,
         UnsupportedOperationException, IllegalArgumentException {
      Card[][] playerFields = new Card[Rules.NUM_PLAYERS][Rules.NUM_DICE_DISCS];
      playerFields[PLAYER_1] = new Card[] {
            Card.TELEPHONEBOX,
            Card.TURRIS,
            Card.SICARIUS,
            Card.SICARIUS,
            Card.SICARIUS,
            Card.SICARIUS,
            Card.SICARIUS
      };

      playerFields[PLAYER_2] = new Card[] {
            Card.NERO,
            Card.SCAENICUS,
            Card.LEGIONARIUS,
            Card.LEGIONARIUS,
            Card.LEGIONARIUS,
            Card.LEGIONARIUS,
            Card.LEGIONARIUS
      };
      
      ArrayList<Card> player1Hand = new ArrayList<Card>();
      player1Hand.add(Card.KAT); // Player 1 has a kat! That will win him the game!


      gameState.setWhoseTurn(PLAYER_2);
      gameState.setPlayerCardsOnDiscs(PLAYER_1,playerFields[PLAYER_1]);
      gameState.setPlayerCardsOnDiscs(PLAYER_2,playerFields[PLAYER_2]);
      gameState.setPlayerHand(PLAYER_1, player1Hand);
      gameState.setPlayerHand(PLAYER_2, new ArrayList<Card>());
      gameState.setPlayerVictoryPoints(PLAYER_1, 10);
      gameState.setPlayerVictoryPoints(PLAYER_2, 10);
      gameState.setPlayerSestertii(PLAYER_1, 10);
      gameState.setPlayerSestertii(PLAYER_2, 10);

      // Turn 1: (Player 2 kills the turris with a scanicused nero, then passes)
      gameState.setActionDice(new int[] {2,3,4});
      ScaenicusActivator joker = (ScaenicusActivator) move.chooseCardToActivate(Rules.DICE_DISC_2); // Why so serious?
      NeroActivator bomb = (NeroActivator) joker.getScaenicusMimicTarget(Rules.DICE_DISC_1);
      bomb.chooseDiceDisc(Rules.DICE_DISC_2); // BOOOOOOOOM
      bomb.complete();
      joker.complete();
      assert (gameState.getPlayerCardsOnDiscs(PLAYER_1)[Rules.DICE_DISC_2 - 1] == Card.NOT_A_CARD);
      assert (gameState.getPlayerCardsOnDiscs(PLAYER_2)[Rules.DICE_DISC_2 - 1] == Card.NOT_A_CARD);
      assert (gameState.getPlayerCardsOnDiscs(PLAYER_2)[Rules.DICE_DISC_1 - 1] == Card.NERO);
      move.endTurn();
      
      // Turn 2: (Player 1, having seen the threat, heads back in time to stop it in it's tracks)
      gameState.setActionDice(new int[] {1,1,1});
      // First, play a Kat (Batman), which will save the day!
      move.placeCard(Card.KAT, Rules.DICE_DISC_2);
      assert (gameState.getPlayerCardsOnDiscs(PLAYER_1)[Rules.DICE_DISC_2 - 1] == Card.KAT);
      TelephoneBoxActivator tardis = (TelephoneBoxActivator) move.chooseCardToActivate(Rules.DICE_DISC_1);
      tardis.chooseDiceDisc(Rules.DICE_DISC_2);
      tardis.setSecondDiceUsed(1);
      tardis.shouldMoveForwardInTime(false);
      tardis.complete();
      // Now the plan has been stopped! Time to check that everything is cool
      assert (gameState.getPlayerCardsOnDiscs(PLAYER_1)[Rules.DICE_DISC_2 - 1] == Card.KAT);
      assert (gameState.getPlayerCardsOnDiscs(PLAYER_1)[Rules.DICE_DISC_1 - 1] == Card.TELEPHONEBOX);
      assert (gameState.getPlayerCardsOnDiscs(PLAYER_2)[Rules.DICE_DISC_2 - 1] == Card.NOT_A_CARD);
      assert (gameState.getPlayerCardsOnDiscs(PLAYER_2)[Rules.DICE_DISC_1 - 1] == Card.NERO);
   }

}
