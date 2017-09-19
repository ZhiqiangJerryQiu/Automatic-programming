package tests.verifiedBuilding;

import java.util.ArrayList;
import java.util.List;

import framework.Rules;
import framework.Test;
import framework.cards.Card;
import framework.interfaces.GameState;
import framework.interfaces.MoveMaker;
import framework.interfaces.activators.HaruspexActivator;
import framework.interfaces.activators.LegionariusActivator;

/**
 * 
 * Test the card Haruspex
 * 
 * @author Chris FONG
 *
 */

public class CardActivatorHaruspexBasicTest extends Test {

	private final int PLAYER_1 = 0;
	private final int PLAYER_2 = 1;
	
	private final int HARUSPEX_COST = 4;
	private final int HARUSPEX_DEFENCE = 3;

	
    @Override
    public String getShortDescription() {
        return "Test the card Haruspex";

    }

    @Override
    public void run(GameState gameState, MoveMaker move) throws AssertionError,
            UnsupportedOperationException, IllegalArgumentException {
      if (1==1) {
         throw new IllegalArgumentException();
      }        
        
    	/*

			Initialises the field to:
			         			 1           2         		3            4            5             6           7
            	Player 1:	<NOT_A_CARD>, <HARUSPEX>  ,<NOT_A_CARD>,<NOT_A_CARD>,<NOT_A_CARD>,<NOT_A_CARD>,<NOT_A_CARD>
				Player 2:	<NOT_A_CARD>,<LEGIONARIUS>,<NOT_A_CARD>,<NOT_A_CARD>,<NOT_A_CARD>,<NOT_A_CARD>,<NOT_A_CARD>

    	 */
    	
    	
    	gameState.setPlayerCardsOnDiscs(PLAYER_1,
    				new Card[] {
	    					Card.NOT_A_CARD,
	    					Card.HARUSPEX,
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
    	
    	/*
    	 
    	 	Attacks the Haruspex with a Legionarius using:
    	 			3 - 1 = 2 [ Shouldn't kill ]
    	 			3 - 0 = 3 [ Should kill ]
    	 			
    	 	Tests:
    	 		*Haruspex defence = 3
    	 
    	*/
    	
    	gameState.setWhoseTurn(PLAYER_2);
    	gameState.setActionDice(new int[]{2,2,2});
    	
    	assert(gameState.getPlayerCardsOnDiscs(PLAYER_1)[1] == Card.HARUSPEX);
    	
    	LegionariusActivator theEnemy = (LegionariusActivator) move.chooseCardToActivate(Rules.DICE_DISC_2);
    	theEnemy.giveAttackDieRoll(HARUSPEX_DEFENCE - 1);
    	theEnemy.complete();
    	
    	assert(gameState.getPlayerCardsOnDiscs(PLAYER_1)[1] == Card.HARUSPEX);
    	
    	theEnemy = (LegionariusActivator) move.chooseCardToActivate(Rules.DICE_DISC_2);
    	theEnemy.giveAttackDieRoll(HARUSPEX_DEFENCE);
    	theEnemy.complete();
    	
    	assert(gameState.getPlayerCardsOnDiscs(PLAYER_1)[1] == Card.NOT_A_CARD);
    	
    	move.endTurn();
    	
    	/*
   	 
	 		Lays the Haruspex
	 	
	 		Tests:
	 			*Haruspex cost = 4
	 
    	 */
    	
    	List<Card> hand = new ArrayList<Card>();
    	hand.add(Card.HARUSPEX);
    	
    	gameState.setActionDice(new int[] {2,2,2});
    	gameState.setPlayerHand(PLAYER_1, hand);
    	gameState.setPlayerSestertii(PLAYER_1, HARUSPEX_COST);
    
    	assert(gameState.getPlayerSestertii(PLAYER_1) == HARUSPEX_COST);
    	move.placeCard(Card.HARUSPEX, Rules.DICE_DISC_2);
    	assert(gameState.getPlayerSestertii(PLAYER_1) == 0);

    	assert(gameState.getPlayerHand(PLAYER_1).isEmpty());
    	
    	/*
      	 
	 		Activates the Haruspex and takes Centurio then Basilica
	 	
	 		Tests:
	 			*The chosen cards are now longer in the deck
	 			*The chosen cards are now in the hand
	 
    	 */
    	
    	List<Card> deck = new ArrayList<Card>();
    	deck.add(Card.BASILICA);
    	deck.add(Card.CENTURIO);
    	
    	gameState.setDeck(deck);
    
    	HaruspexActivator theHero = (HaruspexActivator) move.chooseCardToActivate(Rules.DICE_DISC_2);
    	theHero.chooseCardFromPile(getIndexFromPile(Card.CENTURIO, gameState.getDeck()));
    	theHero.complete();
    
    	assert(!gameState.getPlayerHand(PLAYER_1).isEmpty());
    	
    	assert(gameState.getPlayerHand(PLAYER_1).contains(Card.CENTURIO));
    	assert(!gameState.getDeck().contains(Card.CENTURIO));
    	
    	theHero = (HaruspexActivator) move.chooseCardToActivate(Rules.DICE_DISC_2);
    	theHero.chooseCardFromPile(getIndexFromPile(Card.BASILICA, gameState.getDeck()));
    	theHero.complete();
    
    	assert(gameState.getPlayerHand(PLAYER_1).contains(Card.BASILICA));
    	assert(!gameState.getDeck().contains(Card.BASILICA));
    	
    }

}
