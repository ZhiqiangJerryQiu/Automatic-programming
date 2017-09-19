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
 * Activates the time machine a lot, to test if you simulate everymove over and over again with it
 */

public class TelephoneBoxLotsOfActivationsTest extends Test {
   private final int PLAYER_1 = 0;
   private final int PLAYER_2 = 1;
   @Override
   public String getShortDescription() {
      return "Using the power of exponenation, tests whether you are implementing it efficiently";
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
      Card[] toAdd = new Card[]{Card.AESCULAPINUM, Card.ARCHITECTUS, Card.BASILICA, Card.CENTURIO, Card.CONSILIARIUS, Card.CONSUL};
      for (int i = 0; i < toAdd.length; i++) {
         for (int j = 0; j < Rules.NUM_DICE_DISCS - 1; j++) {
            player1Hand.add(toAdd[i]);
         }
      }


      gameState.setWhoseTurn(PLAYER_2);
      gameState.setPlayerCardsOnDiscs(PLAYER_1,playerFields[PLAYER_1]);
      gameState.setPlayerCardsOnDiscs(PLAYER_2,playerFields[PLAYER_2]);
      gameState.setPlayerHand(PLAYER_1, player1Hand);
      gameState.setPlayerHand(PLAYER_2, new ArrayList<Card>());
      gameState.setPlayerVictoryPoints(PLAYER_1, 10);
      gameState.setPlayerVictoryPoints(PLAYER_2, 10);
      gameState.setPlayerSestertii(PLAYER_1, 10 * player1Hand.size());
      gameState.setPlayerSestertii(PLAYER_2, 0);
      
      for (int i = 0; i < toAdd.length; i++) {
         for (int j = 0; j < Rules.NUM_DICE_DISCS - 1; j++) {
            // First, pass a go for the other dude
            gameState.setActionDice(new int[]{1,1,1});
            move.endTurn();
            
            // Now, play done a card
            gameState.setActionDice(new int[]{1,1,1});
            move.placeCard(toAdd[i], j + 2);
            assert (gameState.getPlayerCardsOnDiscs(PLAYER_1)[j+1]==toAdd[i]);
            // And send it back to the beginning of the previous player's turn
            TelephoneBoxActivator tardis = (TelephoneBoxActivator) move.chooseCardToActivate(Rules.DICE_DISC_1);
            tardis.chooseDiceDisc(j + 2);
            tardis.setSecondDiceUsed(1);
            tardis.shouldMoveForwardInTime(false);
            tardis.complete();
            assert (gameState.getPlayerCardsOnDiscs(PLAYER_1)[j+1]==toAdd[i]);
            move.endTurn();
         }
      }
      // Cool, they pass :)

   }

}
