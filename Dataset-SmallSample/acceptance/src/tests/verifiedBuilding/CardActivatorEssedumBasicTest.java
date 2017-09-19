package tests.verifiedBuilding;

import java.util.Collection;
import java.util.LinkedList;

import framework.Rules;
import framework.Test;
import framework.cards.Card;
import framework.interfaces.GameState;
import framework.interfaces.MoveMaker;
import framework.interfaces.activators.EssedumActivator;
import framework.interfaces.activators.LegionariusActivator;

/**
 * Created with IntelliJ IDEA.
 * @author chrisfong
 * Date: 9/05/12
 * Time: 11:54 AM
 * To change this template use File | Settings | File Templates.
 */
public class CardActivatorEssedumBasicTest extends Test {

    private final int PLAYER_1 = 0;
    private final int PLAYER_2 = 1;

    private final int ESSEDUM_COST = 6;
    private final int ESSEDUM_DEFENCE = 3;
    
    private final int LEGIONARIUS_DEFENCE = 5;
    
    @Override
    public String getShortDescription() {
        return "Test the card Essedum";
    }

    @Override
    public void run(GameState gameState, MoveMaker move) throws AssertionError,
            UnsupportedOperationException, IllegalArgumentException {
        if (1==1) {
            throw new IllegalArgumentException();
        }

        /*
        Set Player 1's field to:

                 1            2             3           4            5             6           7
           <NOT_A_CARD>,<ESSEDUM>,<LEGIONARIUS>,<NOT_A_CARD>,<NOT_A_CARD>,<NOT_A_CARD>,<NOT_A_CARD>

         */
    	
    	gameState.setPlayerCardsOnDiscs(PLAYER_1,
    			new Card[] {
    				Card.NOT_A_CARD,
    				Card.ESSEDUM,
    				Card.LEGIONARIUS,
    				Card.NOT_A_CARD,
    				Card.NOT_A_CARD,
    				Card.NOT_A_CARD,
    				Card.NOT_A_CARD,
    			});

        /*
        Set Player 2's field to:

                 1            2             3           4            5             6           7
           <NOT_A_CARD>,<LEGIONARIUS>,<LEGIONARIUS>,<NOT_A_CARD>,<NOT_A_CARD>,<NOT_A_CARD>,<NOT_A_CARD>

         */
    	
    	
    	gameState.setPlayerCardsOnDiscs(PLAYER_2,
    			new Card[] {
    				Card.NOT_A_CARD,
    				Card.LEGIONARIUS,
    				Card.LEGIONARIUS,
    				Card.NOT_A_CARD,
    				Card.NOT_A_CARD,
    				Card.NOT_A_CARD,
    				Card.NOT_A_CARD,
    			});
    	
    	gameState.setWhoseTurn(PLAYER_2);
    	gameState.setActionDice(new int[]{2,2,4});
    	
    	assert(gameState.getPlayerCardsOnDiscs(PLAYER_1)[1] == Card.ESSEDUM);
    	
    	/*
    
    		Attack Essedum with Legionarius
    
			Tests:
				Essedum has defence of 3

    	 */
    	
    	LegionariusActivator theEnemy = (LegionariusActivator) move.chooseCardToActivate(Rules.DICE_DISC_2);
    	theEnemy.giveAttackDieRoll(ESSEDUM_DEFENCE - 1);
    	theEnemy.complete();
    	
    	assert(gameState.getPlayerCardsOnDiscs(PLAYER_1)[1] == Card.ESSEDUM);
    	
    	theEnemy = (LegionariusActivator) move.chooseCardToActivate(Rules.DICE_DISC_2);
    	theEnemy.giveAttackDieRoll(ESSEDUM_DEFENCE);
    	theEnemy.complete();
    	
    	assert(gameState.getPlayerCardsOnDiscs(PLAYER_1)[1] == Card.NOT_A_CARD);
    	
    	move.endTurn();
    	
    	/*
        
			Places Essedum on Disc 2
			
			Tests:
				Essedum has cost of 6

    	 */
    	
    	gameState.setActionDice(new int[]{2,3,3});
    	gameState.setPlayerSestertii(PLAYER_1,ESSEDUM_COST);
    	
    	Collection<Card> hand = new LinkedList<Card>();
    	hand.add(Card.ESSEDUM);
    	
    	assert(gameState.getPlayerSestertii(PLAYER_1) == ESSEDUM_COST);
    	gameState.setPlayerHand(PLAYER_1,hand);
    	move.placeCard(Card.ESSEDUM, Rules.DICE_DISC_2);
    	assert(gameState.getPlayerCardsOnDiscs(PLAYER_1)[1] == Card.ESSEDUM);
    	assert(gameState.getPlayerSestertii(PLAYER_1) == 0);
    	
    	/* 

			Attacks Player 2's Legionarius with Player 1's Legionarius with 4 (One less than defence)
			Then activates Essedum.
    	 	Then attacks Player 2's Legionarius with Player 1's Legionarius with a 3 (Legionarius's new defence)
    	
    		Tests:
    			* Essedum reduces Legionarius' defence by 2
    	
    	 */
    	
    	LegionariusActivator legionarius = (LegionariusActivator) move.chooseCardToActivate(Rules.DICE_DISC_3);
    	legionarius.giveAttackDieRoll(LEGIONARIUS_DEFENCE - 1); //SHOULDN'T KILL
    	legionarius.complete();
    	
    	assert(gameState.getPlayerCardsOnDiscs(PLAYER_2)[2] == Card.LEGIONARIUS);

    	EssedumActivator esther = (EssedumActivator) move.chooseCardToActivate(Rules.DICE_DISC_2);
    	esther.complete();
    	
    	legionarius = (LegionariusActivator) move.chooseCardToActivate(Rules.DICE_DISC_3);
    	legionarius.giveAttackDieRoll(LEGIONARIUS_DEFENCE - 2); //SHOULD KILL
    	legionarius.complete();
    	
    	assert(gameState.getPlayerCardsOnDiscs(PLAYER_2)[2] == Card.NOT_A_CARD);
    	
    	move.endTurn();
    	
    	/*
			Returns to Player 1's turn
			Restores the Legionarius previously killed to Player 2
			
		*/
    	
    	move.endTurn();
    	
    	gameState.setPlayerCardsOnDiscs(PLAYER_2,
    			new Card[] {
    				Card.NOT_A_CARD,
    				Card.LEGIONARIUS,
    				Card.LEGIONARIUS,
    				Card.NOT_A_CARD,
    				Card.NOT_A_CARD,
    				Card.NOT_A_CARD,
    				Card.NOT_A_CARD,
    			});
    	
    	assert(gameState.getWhoseTurn() == PLAYER_1);
    	assert(gameState.getPlayerCardsOnDiscs(PLAYER_2)[2] == Card.LEGIONARIUS);
    	
    	/*
			Attacks the Legionarius again.
		
			Tests:
				* Essedum's effect will deactivate after the turn    	 
	 
    	 */
    	
    	gameState.setActionDice(new int[] {3,3,3});
    	legionarius = (LegionariusActivator) move.chooseCardToActivate(Rules.DICE_DISC_3);
    	legionarius.giveAttackDieRoll(LEGIONARIUS_DEFENCE - 1);
    	legionarius.complete();

    	assert(gameState.getPlayerCardsOnDiscs(PLAYER_2)[2] == Card.LEGIONARIUS);

    }

}
