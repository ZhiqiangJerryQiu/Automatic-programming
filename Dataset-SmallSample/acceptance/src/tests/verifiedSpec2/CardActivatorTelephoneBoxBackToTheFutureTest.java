package tests.verifiedSpec2;

import framework.Rules;
import framework.Test;
import framework.cards.Card;
import framework.interfaces.GameState;
import framework.interfaces.MoveMaker;
import framework.interfaces.activators.LegionariusActivator;
import framework.interfaces.activators.TelephoneBoxActivator;

import java.util.ArrayList;
import java.util.List;

/**
 * TelephoneboxTest
 * 
 * @author Chris Fong
 *
 */

public class CardActivatorTelephoneBoxBackToTheFutureTest extends Test {

	 private final int PLAYER_1 = 0;
  	 private final int PLAYER_2 = 1;

    public String getShortDescription() {
        return "Tests the basic functionability of Time Machine";
    }

    public void run(GameState gameState, MoveMaker move) throws AssertionError,
            UnsupportedOperationException, IllegalArgumentException {

        //Turn #1
        List<Card> deck = new ArrayList<Card>();
        deck.add(Card.ESSEDUM);
        deck.add(Card.TURRIS);
        gameState.setDeck(deck);
        deck = null;

        gameState.setPlayerSestertii(PLAYER_1,100);

        gameState.setWhoseTurn(PLAYER_1);
    	gameState.setPlayerCardsOnDiscs(PLAYER_1, 
    				new Card[] {
    						Card.TELEPHONEBOX,
    						Card.NOT_A_CARD,
                            Card.MERCATOR,
                            Card.BASILICA,
                            Card.BASILICA,
                            Card.BASILICA,
                            Card.BASILICA
    				});
    	
    	gameState.setPlayerCardsOnDiscs(PLAYER_2,
                    new Card[]{
                            Card.TURRIS,
                            Card.TURRIS,
                            Card.LEGIONARIUS,
                            Card.TURRIS,
                            Card.TURRIS,
                            Card.TURRIS,
                            Card.TURRIS,
                    });

        gameState.setPlayerVictoryPoints(PLAYER_1, 16);
        gameState.setPlayerVictoryPoints(PLAYER_2,16);
        assert(gameState.getPlayerVictoryPoints(PLAYER_1) == 16);
        assert(gameState.getPlayerVictoryPoints(PLAYER_2) == 16);
        gameState.setActionDice(new int[] {1,1,1});

        move.endTurn();  
        
        //Turn #2

        assert(gameState.getPlayerVictoryPoints(PLAYER_2) == 16);
        gameState.setActionDice(new int[] {1,1,1});

        move.endTurn();  
        
        //Turn #3

        assert(gameState.getPlayerVictoryPoints(PLAYER_1) == 15);
        gameState.setActionDice(new int[] {1,1,1});

        move.activateCardsDisc(1,Card.ESSEDUM);
        move.placeCard(Card.ESSEDUM, Rules.DICE_DISC_2);

        assert(gameState.getPlayerCardsOnDiscs(PLAYER_1)[1] == Card.ESSEDUM);

        assert(gameState.getPlayerVictoryPoints(PLAYER_1) == 15);

        TelephoneBoxActivator drWho = (TelephoneBoxActivator) move.chooseCardToActivate(Rules.DICE_DISC_1);
        drWho.setSecondDiceUsed(1);
        drWho.shouldMoveForwardInTime(false);
        drWho.chooseDiceDisc(Rules.DICE_DISC_2);
        drWho.complete();

        assert(gameState.getPlayerCardsOnDiscs(PLAYER_1)[1] == Card.ESSEDUM);
        assert(gameState.getPlayerVictoryPoints(PLAYER_1) == 16);

        move.endTurn();  
        
        //Turn #4

        gameState.setActionDice(new int[] {3,3,3});

        assert(gameState.getPlayerCardsOnDiscs(PLAYER_1)[2] ==  Card.MERCATOR);
        LegionariusActivator theEnemy = (LegionariusActivator) move.chooseCardToActivate(Rules.DICE_DISC_3);
        theEnemy.giveAttackDieRoll(2);
        theEnemy.complete();
        assert(gameState.getPlayerCardsOnDiscs(PLAYER_1)[2] == Card.NOT_A_CARD);

        move.endTurn();  //Turn #5

        assert(gameState.getPlayerVictoryPoints(PLAYER_1) == 15);
        gameState.setActionDice(new int[] {1,1,1});
        move.activateCardsDisc(1,Card.TURRIS);
        assert(gameState.getPlayerHand(PLAYER_1).contains(Card.TURRIS));

        move.placeCard(Card.TURRIS,Rules.DICE_DISC_4);

        move.endTurn();  //Turn #6

        gameState.setActionDice(new int[] {1,1,1});

        move.endTurn();  //Turn #7 - Skip back to Player 1

        gameState.setActionDice(new int[]{1,3,4});
        assert(gameState.getPlayerVictoryPoints(PLAYER_1) == 14);

        drWho = (TelephoneBoxActivator)move.chooseCardToActivate(Rules.DICE_DISC_1);
        drWho.setSecondDiceUsed(3);
        drWho.shouldMoveForwardInTime(false);
        drWho.chooseDiceDisc(Rules.DICE_DISC_4);
        drWho.complete();

        assert(gameState.getPlayerCardsOnDiscs(PLAYER_1)[2] == Card.MERCATOR);
        assert(gameState.getPlayerVictoryPoints(PLAYER_1) == 16);

    }
}
