package tests.verifiedChar;

import java.util.ArrayList;
import java.util.Collection;

import framework.Rules;
import framework.Test;
import framework.cards.Card;
import framework.interfaces.GameState;
import framework.interfaces.MoveMaker;
import framework.interfaces.activators.LegionariusActivator;
import framework.interfaces.activators.PraetorianusActivator;

/**
 * 
 * Test the functionality of Praetorianus
 * 
 * @author Chris FONG
 *
 */

public class CardActivatorPraetorianusBasicTest extends Test {

	private final int PLAYER_1 = 0;
	private final int PLAYER_2 = 1;
	
	private final int PRAETORIANUS_COST = 4;
	private final int PRAETORIANUS_DEFENCE = 4;
	
    @Override
    public String getShortDescription() {
        return "Test the functionality of Praetorianus";
    }

    @Override
    public void run(GameState gameState, MoveMaker move) throws AssertionError,
            UnsupportedOperationException, IllegalArgumentException {
      if (1==1) {
         throw new IllegalArgumentException();
      }        
        
    	emptyFields(gameState);
    	
    	gameState.setPlayerCardsOnDiscs(PLAYER_1, 
    					new Card[] {
    			
    						Card.NOT_A_CARD,
    						Card.PRAETORIANUS,
    						Card.NOT_A_CARD,
    						Card.NOT_A_CARD,
    						Card.NOT_A_CARD,
    						Card.NOT_A_CARD,
    						Card.NOT_A_CARD,
    	
    					});
    	
    	gameState.setPlayerCardsOnDiscs(PLAYER_2, 
				new Card[] {
		
					Card.NOT_A_CARD,
					Card.LEGIONARIUS,
					Card.NOT_A_CARD,
					Card.NOT_A_CARD,
					Card.NOT_A_CARD,
					Card.NOT_A_CARD,
					Card.NOT_A_CARD,

				});

    	gameState.setWhoseTurn(PLAYER_2);
    	gameState.setActionDice(new int[]{2,2,2});
    	
    	assert(gameState.getPlayerCardsOnDiscs(PLAYER_1)[1] == Card.PRAETORIANUS);
    	LegionariusActivator theEnemy = (LegionariusActivator) move.chooseCardToActivate(Rules.DICE_DISC_2);
    	theEnemy.giveAttackDieRoll(PRAETORIANUS_DEFENCE - 1);
    	theEnemy.complete();
    	assert(gameState.getPlayerCardsOnDiscs(PLAYER_1)[1] == Card.PRAETORIANUS);
    	
    	theEnemy = (LegionariusActivator) move.chooseCardToActivate(Rules.DICE_DISC_2);
    	theEnemy.giveAttackDieRoll(PRAETORIANUS_DEFENCE);
    	theEnemy.complete();
    	assert(gameState.getPlayerCardsOnDiscs(PLAYER_1)[1] == Card.NOT_A_CARD);
    	
    	move.endTurn();
    	
    	gameState.setActionDice(new int[]{2,2,2});
    	gameState.setPlayerSestertii(PLAYER_1, PRAETORIANUS_COST);
    	
    	Collection<Card> hand = new ArrayList<Card>();
    	hand.add(Card.PRAETORIANUS);
    	
    	gameState.setPlayerHand(PLAYER_1,hand);
    	
    	assert(gameState.getPlayerSestertii(PLAYER_1) == PRAETORIANUS_COST);
    	move.placeCard(Card.PRAETORIANUS, Rules.DICE_DISC_2);
    	assert(gameState.getPlayerSestertii(PLAYER_1) == 0);
    	
    	PraetorianusActivator predator = (PraetorianusActivator) move.chooseCardToActivate(Rules.DICE_DISC_2);
    	predator.chooseDiceDisc(Rules.DICE_DISC_2);
    	predator.complete();
    	
    	move.endTurn();
    	
    	gameState.setActionDice(new int[]{2,2,2});
    	
    	assert(gameState.getPlayerCardsOnDiscs(PLAYER_1)[1] == Card.PRAETORIANUS);
    	theEnemy = (LegionariusActivator) move.chooseCardToActivate(Rules.DICE_DISC_2);
    	theEnemy.giveAttackDieRoll(PRAETORIANUS_DEFENCE);
    	theEnemy.complete();
    	Card[] cards = gameState.getPlayerCardsOnDiscs(PLAYER_1);
    	//assert(gameState.getPlayerCardsOnDiscs(PLAYER_1)[1] == Card.PRAETORIANUS);
    	
    }
    
    private void emptyFields(GameState gameState) {
        Card[] emptyField = new Card[Rules.NUM_DICE_DISCS];

        for (int i = 0 ; i < Rules.NUM_DICE_DISCS; i++) {
            emptyField[i] = Card.NOT_A_CARD;
        }

        for (int i = 0 ; i < Rules.NUM_PLAYERS; i++) {
            gameState.setPlayerCardsOnDiscs(i,emptyField);
        }

    }

}
